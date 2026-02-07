package server

import (
	"context"
	"crypto/ed25519"
	"errors"
	"fmt"
	"sync"

	pb "securemessenger/pb"
)

type NodeSession struct {
	nodeID   string
	stream   pb.Messenger_SubscribeServer
	quitChan chan struct{}
}

type MessengerServer struct {
	pb.UnimplementedMessengerServer

	// Хранилище публичных ключей (в реале - БД)
	// NodeID -> ed25519.PublicKey
	nodes map[string]ed25519.PublicKey

	// Активные стримы: NodeID -> канал для передачи пакетов
	activeStreams map[string]chan *pb.MessagePacket

	// Временные челенджи для авторизации: NodeID -> nonce
	challenges map[string][]byte

	mu sync.RWMutex
}

func NewServer() *MessengerServer {
	return &MessengerServer{
		nodes:         make(map[string]ed25519.PublicKey),
		activeStreams: make(map[string]chan *pb.MessagePacket),
		challenges:    make(map[string][]byte),
	}
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
	defer s.mu.RUnlock()

	// 1. Проверяем подпись отправителя (Слой 4)
	pubKey, ok := s.nodes[packet.SenderNodeId]
	if !ok {
		return &pb.SendResponse{Delivered: false, Error: "unknown sender"}, nil
	}

	// В реальности подпись проверяется от payload + метаданные
	if !ed25519.Verify(pubKey, packet.Payload, packet.Signature) {
		return &pb.SendResponse{Delivered: false, Error: "bad L4 signature"}, nil
	}

	// 2. Ищем получателя и доставляем в его стрим
	targetChan, online := s.activeStreams[packet.TargetNodeId]
	if !online {
		return &pb.SendResponse{Delivered: false, Error: "target offline"}, nil
	}

	targetChan <- packet
	return &pb.SendResponse{Delivered: true}, nil
}
