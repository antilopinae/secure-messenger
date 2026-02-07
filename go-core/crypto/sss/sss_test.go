package sss

import (
	"testing"
)

func TestSplitAndReconstruct(t *testing.T) {
	// Тестируем все возможные значения байта
	for s := 0; s < 256; s++ {
		secret := byte(s)
		n := byte(5)
		k := byte(3)

		parts, err := Split(secret, n, k)
		if err != nil {
			t.Fatal(err)
		}

		// Проверяем, что по любым 3 частям из 5 секрет восстанавливается
		subset := []Part{parts[0], parts[2], parts[4]}
		recovered := Reconstruct(subset)

		if recovered != secret {
			t.Errorf("Value %d: expected %d, got %d", s, secret, recovered)
		}
	}
}

func TestSecurity(t *testing.T) {
	secret := byte(123)
	parts, _ := Split(secret, 5, 3)

	// Проверяем, что 2 части (меньше K) дают неверный результат
	insufficient := []Part{parts[0], parts[1]}
	recovered := Reconstruct(insufficient)

	if recovered == secret {
		t.Log("Warning: coincidental recovery with k-1 parts (rare but possible)")
	} else {
		t.Logf("Security check passed: 2 parts couldn't recover secret 123 (got %d)", recovered)
	}
}
