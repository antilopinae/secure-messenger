package secure

import (
	"crypto/rand"
	"io"

	"golang.org/x/crypto/curve25519"
)

func EncryptPart(targetPub []byte, data byte) (ephemPub []byte, encData byte, err error) {
	var priv, pub, target, shared [32]byte
	if _, err := io.ReadFull(rand.Reader, priv[:]); err != nil {
		return nil, 0, err
	}
	curve25519.ScalarBaseMult(&pub, &priv)
	copy(target[:], targetPub)
	curve25519.ScalarMult(&shared, &priv, &target)
	return pub[:], data ^ shared[0], nil
}

func DecryptPart(myPriv, ephemPub []byte, encData byte) byte {
	var priv, ephem, shared [32]byte
	copy(priv[:], myPriv)
	copy(ephem[:], ephemPub)
	curve25519.ScalarMult(&shared, &priv, &ephem)
	return encData ^ shared[0]
}
