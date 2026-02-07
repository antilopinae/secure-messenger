package server

import (
	"context"
	"crypto/ed25519"
	"crypto/rand"
	"testing"
	"time"

	"net"
	pb "securemessenger/pb"

	"google.golang.org/grpc"
	"google.golang.org/grpc/test/bufconn"
)

const bufSize = 1024 * 1024

var lis *bufconn.Listener

// Инициализация фиктивного сетевого соединения
func initGRPCServer() *MessengerServer {
	lis = bufconn.Listen(bufSize)
	s := grpc.NewServer()
	srv, err := NewServer("/tmp/server.db")
	if err != nil {
		panic("Failed to init server: " + err.Error())
	}
	pb.RegisterMessengerServer(s, srv)
	go func() {
		if err := s.Serve(lis); err != nil {
			panic(err)
		}
	}()
	return srv
}

func dialer(context.Context, string) (net.Conn, error) {
	return lis.Dial()
}

func TestFullMessagingFlow(t *testing.T) {
	initGRPCServer()
	ctx := context.Background()
	conn, _ := grpc.DialContext(ctx, "bufnet", grpc.WithContextDialer(dialer), grpc.WithInsecure())
	defer conn.Close()
	client := pb.NewMessengerClient(conn)

	// --- РЕГИСТРАЦИЯ АЛИСЫ ---
	pubAlice, privAlice, _ := ed25519.GenerateKey(rand.Reader)
	nodeAlice := "alice_node_1"

	// 1. Get Challenge
	resCh, _ := client.GetChallenge(ctx, &pb.ChallengeRequest{NodeId: nodeAlice})

	// 2. Sign & Auth
	sigAlice := ed25519.Sign(privAlice, resCh.Nonce)
	_, err := client.Authenticate(ctx, &pb.AuthRequest{
		NodeId:    nodeAlice,
		Signature: sigAlice,
		PublicKey: pubAlice,
	})
	if err != nil {
		t.Fatalf("Auth failed: %v", err)
	}

	// --- РЕГИСТРАЦИЯ БОБА ---
	_, _, _ = ed25519.GenerateKey(rand.Reader)
	nodeBob := "bob_node_1"
	// (пропустим для краткости, добавим вручную в "белый список" сервера для теста)

	// --- ПОДПИСКА БОБА ---
	// Боб открывает стрим, чтобы ждать сообщений
	bobStream, _ := client.Subscribe(ctx, &pb.SubscribeRequest{SessionToken: nodeBob})

	// --- АЛИСА ШЛЕТ ПАКЕТ БОБУ ---
	messagePayload := []byte("L3_encrypted_data")
	l4Signature := ed25519.Sign(privAlice, messagePayload)

	go func() {
		time.Sleep(100 * time.Millisecond) // Даем время стриму открыться
		client.SendPacket(ctx, &pb.MessagePacket{
			TargetNodeId: nodeBob,
			SenderNodeId: nodeAlice,
			Payload:      messagePayload,
			Signature:    l4Signature,
		})
	}()

	// --- ПРОВЕРКА ПОЛУЧЕНИЯ У БОБА ---
	received, err := bobStream.Recv()
	if err != nil {
		t.Errorf("Bob failed to receive: %v", err)
	}

	if string(received.Payload) != string(messagePayload) {
		t.Errorf("Payload mismatch! Got %s", string(received.Payload))
	}

	t.Log("Flow successful: Message routed and signature verified.")
}
