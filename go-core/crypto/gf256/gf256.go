package gf256

// В GF(2^8) сложение — это просто XOR.
// Примитивный полином 0x1D (x^8 + x^4 + x^3 + x^2 + 1) используется для умножения.

var (
	expTable [256]byte // Таблица экспонент для быстрого умножения
	logTable [256]byte // Таблица логарифмов
)

func init() {
	// Инициализируем таблицы для ускорения вычислений (как в AES)
	var x byte = 1
	for i := 0; i < 255; i++ {
		expTable[i] = x
		logTable[x] = byte(i)

		// Умножение на x в поле
		if x&0x80 != 0 {
			x = (x << 1) ^ 0x1D // 0x1D — твой примитивный полином
		} else {
			x <<= 1
		}
	}
	// Зацикливаем для удобства
	expTable[255] = expTable[0]
}

// Add складывает два числа в поле (XOR)
func Add(a, b byte) byte {
	return a ^ b
}

// Mul умножает два числа в поле через таблицы логарифмов
func Mul(a, b byte) byte {
	if a == 0 || b == 0 {
		return 0
	}
	res := int(logTable[a]) + int(logTable[b])
	return expTable[res%255]
}

// Div делит a на b в поле
func Div(a, b byte) byte {
	if b == 0 {
		panic("division by zero in GF(2^8)")
	}
	if a == 0 {
		return 0
	}
	// Вычитаем логарифмы
	diff := int(logTable[a]) - int(logTable[b])
	if diff < 0 {
		diff += 255
	}
	return expTable[diff]
}
