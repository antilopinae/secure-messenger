package mobile

import (
	"context"
	"crypto/ed25519"
	"encoding/json"
	"fmt"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"

	"securemessenger/core"
	"securemessenger/pb"
)

type MessengerCallback interface {
	OnPacketReceived(from string, payload []byte)
	OnLog(msg string)
}

type MobileClient struct {
	node         *core.VaultNode
	grpcClient   pb.MessengerClient
	conn         *grpc.ClientConn
	sessionToken string
	callback     MessengerCallback
}

// NewClient выполняет полную процедуру авторизации: Challenge -> Sign -> Authenticate
func NewClient(nodeID string, privX, privEd []byte, localKey int, serverAddr string, cb MessengerCallback) (*MobileClient, error) {
	conn, err := grpc.Dial(serverAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return nil, err
	}

	client := pb.NewMessengerClient(conn)
	vault := &core.VaultNode{
		ID: nodeID, EdPriv: privEd, XPriv: privX, LocalSecretKey: byte(localKey),
	}

	// 1. Получаем Challenge
	resp, err := client.GetChallenge(context.Background(), &pb.ChallengeRequest{NodeId: nodeID})
	if err != nil {
		return nil, err
	}

	// 2. Подписываем nonce
	sig := ed25519.Sign(privEd, resp.Nonce)

	// 3. Авторизуемся
	authResp, err := client.Authenticate(context.Background(), &pb.AuthRequest{
		NodeId:    nodeID,
		Signature: sig,
		PublicKey: privEd[32:], // Публичный ключ из второй половины приватного
	})
	if err != nil {
		return nil, err
	}

	return &MobileClient{
		node: vault, grpcClient: client, conn: conn, sessionToken: authResp.SessionToken, callback: cb,
	}, nil
}

// SendMessage нарезает текст и шлет пакеты через SendPacket
func (m *MobileClient) SendMessage(text, chatId, peersJSON string, k int) {
	var peers []core.RemotePeer
	json.Unmarshal([]byte(peersJSON), &peers)

	packets, err := m.node.PreparePackets(text, chatId, peers, k)
	if err != nil {
		m.callback.OnLog("Preparation failed: " + err.Error())
		return
	}

	for _, p := range packets {
		resp, err := m.grpcClient.SendPacket(context.Background(), p)
		if err != nil || !resp.Delivered {
			m.callback.OnLog(fmt.Sprintf("Failed to send to %s", p.TargetNodeId))
		}
	}
}

// Listen запускает Subscribe и ждет входящие MessagePacket
func (m *MobileClient) Listen() {
	go func() {
		stream, err := m.grpcClient.Subscribe(context.Background(), &pb.SubscribeRequest{
			SessionToken: m.sessionToken,
		})
		if err != nil {
			return
		}

		for {
			packet, err := stream.Recv()
			if err != nil {
				break
			}

			// Передаем в Kotlin зашифрованный Слой 3 (Payload)
			// Kotlin решит, нужно ли делать Tangle или это финальное сообщение
			m.callback.OnPacketReceived(packet.SenderNodeId, packet.Payload)
		}
	}()
}
