package gf256

var (
	exp [256]byte
	log [256]byte
)

func init() {
	var x byte = 1

	for i := 0; i < 255; i++ {
		exp[i] = x
		log[x] = byte(i)
		if x&0x80 != 0 {
			x = (x << 1) ^ 0x1D
		} else {
			x <<= 1
		}
	}

	exp[255] = exp[0]
}

func Add(a, b byte) byte {
	return a ^ b
}

func Mul(a, b byte) byte {
	if a == 0 || b == 0 {
		return 0
	}
	return exp[(int(log[a])+int(log[b]))%255]
}

func Div(a, b byte) byte {
	if b == 0 {
		panic("div by 0")
	}

	if a == 0 {
		return 0
	}

	d := int(log[a]) - int(log[b])

	if d < 0 {
		d += 255
	}

	return exp[d]
}
