package sss

import (
	"crypto/rand"
	"fmt"
	"securemessenger/crypto/gf256"
)

// Part представляет собой координату на графике (x, y)
type Part struct {
	X byte
	Y byte
}

// Split разделяет байт (секрет) на N частей, из которых K достаточно для восстановления
func Split(secret byte, n, k byte) ([]Part, error) {
	if k > n {
		return nil, fmt.Errorf("K cannot be greater than N")
	}

	// Создаем коэффициенты случайного полинома степени K-1
	// P(x) = secret + a1*x + a2*x^2 + ... + a(k-1)*x^(k-1)
	coeffs := make([]byte, k)
	coeffs[0] = secret // Свободный член — наш секрет

	randomCoeffs := make([]byte, k-1)
	if _, err := rand.Read(randomCoeffs); err != nil {
		return nil, err
	}
	copy(coeffs[1:], randomCoeffs)

	// Генерируем N точек
	parts := make([]Part, n)
	for i := byte(0); i < n; i++ {
		x := i + 1 // X не должен быть 0, так как P(0) = secret
		parts[i] = Part{X: x, Y: evaluate(coeffs, x)}
	}

	return parts, nil
}

// evaluate вычисляет значение полинома в точке x используя схему Горнера в GF(2^8)
func evaluate(coeffs []byte, x byte) byte {
	var result byte
	for i := len(coeffs) - 1; i >= 0; i-- {
		result = gf256.Add(gf256.Mul(result, x), coeffs[i])
	}
	return result
}

// Reconstruct восстанавливает секрет по K частям (Интерполяция Лагранжа)
func Reconstruct(parts []Part) byte {
	var secret byte

	for i := 0; i < len(parts); i++ {
		// Вычисляем базисный полином Лагранжа L_i(0)
		num := byte(1)
		den := byte(1)

		for j := 0; j < len(parts); j++ {
			if i == j {
				continue
			}
			// num = num * (0 - x_j) = num * x_j (в GF256 минус — это плюс)
			num = gf256.Mul(num, parts[j].X)
			// den = den * (x_i - x_j)
			den = gf256.Mul(den, gf256.Add(parts[i].X, parts[j].X))
		}

		term := gf256.Mul(parts[i].Y, gf256.Div(num, den))
		secret = gf256.Add(secret, term)
	}

	return secret
}
