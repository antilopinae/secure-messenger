package sss

import (
	"crypto/rand"
	"securemessenger/crypto/gf256"
)

type Point struct {
	X, Y byte
}

func Split(secret byte, n, k byte) ([]Point, error) {
	coeffs := make([]byte, k)
	coeffs[0] = secret
	if _, err := rand.Read(coeffs[1:]); err != nil {
		return nil, err
	}

	points := make([]Point, n)
	for i := range points {
		x := byte(i + 1)
		var y byte
		for j := int(k) - 1; j >= 0; j-- {
			y = gf256.Add(gf256.Mul(y, x), coeffs[j])
		}
		points[i] = Point{X: x, Y: y}
	}
	return points, nil
}

func Recover(points []Point) byte {
	var secret byte
	for i := range points {
		num, den := byte(1), byte(1)
		for j := range points {
			if i == j {
				continue
			}
			num = gf256.Mul(num, points[j].X)
			den = gf256.Mul(den, gf256.Add(points[i].X, points[j].X))
		}
		secret = gf256.Add(secret, gf256.Mul(points[i].Y, gf256.Div(num, den)))
	}
	return secret
}
