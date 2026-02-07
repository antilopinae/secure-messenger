package server

import (
	"context"
	"crypto/ed25519"
	"errors"
	"fmt"
	"sync"

	"database/sql"

	_ "modernc.org/sqlite"

	pb "securemessenger/pb"
)

type NodeSession struct {
	nodeID   string
	stream   pb.Messenger_SubscribeServer
	quitChan chan struct{}
}

type MessengerServer struct {
	pb.UnimplementedMessengerServer

	db *sql.DB

	// Хранилище публичных ключей (в реале - БД)
	// NodeID -> ed25519.PublicKey
	nodes map[string]ed25519.PublicKey

	// Активные стримы: NodeID -> канал для передачи пакетов
	activeStreams map[string]chan *pb.MessagePacket

	// Временные челенджи для авторизации: NodeID -> nonce
	challenges map[string][]byte

	mu sync.RWMutex
}

func NewServer(dbPath string) (*MessengerServer, error) {
	db, err := sql.Open("sqlite", dbPath)
	if err != nil {
		return nil, err
	}

	// Создаем таблицы:
	// nodes - белый список публичных ключей
	// offline_packets - временное хранилище (TTL реализуем позже)
	query := `
    CREATE TABLE IF NOT EXISTS nodes (
        node_id TEXT PRIMARY KEY,
        pub_key BLOB
    );
    CREATE TABLE IF NOT EXISTS offline_packets (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        target_id TEXT,
        sender_id TEXT,
        payload BLOB,
        signature BLOB,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
    );`

	if _, err := db.Exec(query); err != nil {
		return nil, err
	}

	return &MessengerServer{
		db:            db,
		activeStreams: make(map[string]chan *pb.MessagePacket), // Было
		challenges:    make(map[string][]byte),                 // ДОБАВИТЬ ЭТО
		nodes:         make(map[string]ed25519.PublicKey),      // И ЭТО
	}, nil
}

// GetChallenge генерирует случайное число для проверки владения ключом
func (s *MessengerServer) GetChallenge(ctx context.Context, req *pb.ChallengeRequest) (*pb.ChallengeResponse, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// В продакшене использовать crypto/rand
	nonce := []byte("random_nonce_" + req.NodeId)
	s.challenges[req.NodeId] = nonce

	return &pb.ChallengeResponse{Nonce: nonce}, nil
}

// Authenticate проверяет подпись и "запоминает" публичный ключ узла
func (s *MessengerServer) Authenticate(ctx context.Context, req *pb.AuthRequest) (*pb.AuthResponse, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	nonce, ok := s.challenges[req.NodeId]
	if !ok {
		return nil, errors.New("challenge not found")
	}

	// Верификация Ed25519
	if !ed25519.Verify(req.PublicKey, nonce, req.Signature) {
		return nil, errors.New("invalid signature")
	}

	// Сохраняем ключ (белый список)
	s.nodes[req.NodeId] = req.PublicKey
	delete(s.challenges, req.NodeId)

	// В данном примере session_token = node_id для простоты
	return &pb.AuthResponse{SessionToken: req.NodeId}, nil
}

// Subscribe открывает стрим для получения входящих сообщений
func (s *MessengerServer) Subscribe(req *pb.SubscribeRequest, stream pb.Messenger_SubscribeServer) error {
	nodeID := req.SessionToken // Упрощение

	// 1. Проверяем наличие оффлайн сообщений
	rows, err := s.db.Query("SELECT sender_id, payload, signature FROM offline_packets WHERE target_id = ?", nodeID)
	if err == nil {
		for rows.Next() {
			var p pb.MessagePacket
			p.TargetNodeId = nodeID
			if err := rows.Scan(&p.SenderNodeId, &p.Payload, &p.Signature); err == nil {
				stream.Send(&p)
			}
		}
		s.db.Exec("DELETE FROM offline_packets WHERE target_id = ?", nodeID)
	}

	s.mu.Lock()
	packetChan := make(chan *pb.MessagePacket, 100)
	s.activeStreams[nodeID] = packetChan
	s.mu.Unlock()

	defer func() {
		s.mu.Lock()
		delete(s.activeStreams, nodeID)
		close(packetChan)
		s.mu.Unlock()
	}()

	fmt.Printf("Node %s subscribed\n", nodeID)

	for {
		select {
		case <-stream.Context().Done():
			return nil
		case packet := <-packetChan:
			if err := stream.Send(packet); err != nil {
				return err
			}
		}
	}
}

// SendPacket принимает пакет и перекладывает его в канал получателя
func (s *MessengerServer) SendPacket(ctx context.Context, packet *pb.MessagePacket) (*pb.SendResponse, error) {
	s.mu.RLock()
	targetChan, online := s.activeStreams[packet.TargetNodeId]
	s.mu.RUnlock()

	if online {
		targetChan <- packet
		return &pb.SendResponse{Delivered: true}, nil
	}

	// Если оффлайн — сохраняем в базу
	_, err := s.db.Exec(`
		INSERT INTO offline_packets (target_id, sender_id, payload, signature) 
		VALUES (?, ?, ?, ?)`,
		packet.TargetNodeId, packet.SenderNodeId, packet.Payload, packet.Signature)

	if err != nil {
		return &pb.SendResponse{Delivered: false, Error: "db error"}, err
	}

	return &pb.SendResponse{Delivered: false, Error: "stored offline"}, nil
}
