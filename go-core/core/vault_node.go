package core

import (
	"crypto/ed25519"
	"fmt"
	"time"

	"securemessenger/crypto/secure"
	"securemessenger/crypto/sss"
	"securemessenger/pb"

	"google.golang.org/protobuf/proto"
)

type RemotePeer struct {
	ID    string
	XPub  []byte // Curve25519 для E2EE частиц
	EdPub []byte // Ed25519 для проверки подписи Слоя 3
}

type VaultNode struct {
	ID             string
	EdPriv         ed25519.PrivateKey
	XPriv          []byte
	LocalSecretKey byte
}

// PreparePackets создает набор пакетов (Слой 4) для отправки участникам
func (n *VaultNode) PreparePackets(msg string, chatId string, peers []RemotePeer, k int) ([]*pb.MessagePacket, error) {
	rawData := []byte(msg)
	numPeers := len(peers)
	msgID := fmt.Sprintf("%d", time.Now().UnixNano())

	// Генерируем частицы для каждого участника
	peerParticles := make([][]*pb.Particle, numPeers)
	for _, b := range rawData {
		points, _ := sss.Split(b, byte(numPeers), byte(k))
		for i, p := range points {
			ephem, encY, _ := secure.EncryptPart(peers[i].XPub, p.Y)
			peerParticles[i] = append(peerParticles[i], &pb.Particle{
				X:            uint32(p.X),
				EphemeralPub: ephem,
				EncryptedY:   []byte{encY},
			})
		}
	}

	packets := make([]*pb.MessagePacket, numPeers)
	for i, peer := range peers {
		// 1. Формируем MessageBundle (Слой 3)
		bundle := &pb.MessageBundle{
			ChatId:    chatId,
			MessageId: msgID,
			SenderId:  n.ID,
			Timestamp: time.Now().Unix(),
			Particles: peerParticles[i],
		}

		// Подписываем Слой 3 (Bundle)
		bundleBuf, _ := proto.Marshal(bundle)
		bundle.Signature = ed25519.Sign(n.EdPriv, bundleBuf)

		// 2. Формируем MessagePacket (Слой 4)
		finalBundleBuf, _ := proto.Marshal(bundle)
		packet := &pb.MessagePacket{
			TargetNodeId: peer.ID,
			SenderNodeId: n.ID,
			Payload:      finalBundleBuf,
		}

		// Подписываем Слой 4 (для Сервера)
		packetBuf, _ := proto.Marshal(packet)
		packet.Signature = ed25519.Sign(n.EdPriv, packetBuf)

		packets[i] = packet
	}

	return packets, nil
}
