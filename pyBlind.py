import sys

def convert2dec(s):
	deg = float(s[:2])
	mn = float(s[3:5])
	sec = float(s[6:])
	#print s[:2],s[3:5],s[6:]
	return deg + (mn + sec /60.0) / 60.0

if __name__ == "__main__":
	i = 0
	for l in sys.stdin:
		if i == 0:
			i = i + 1
			# teraz w kazdej kolejnej linii i > 0
			continue
		t = l.split(';')

		print t[1][1:-1], t[5][1:-1], t[5][1:-2], t[5][-2:-1], t[7][1:-1], t[9][1:-1], t[11][1:-1], t[12].strip()[1:-1]
