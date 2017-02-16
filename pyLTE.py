import sys

def convert2dec(s):
	deg = float(s[:2])
	mn = float(s[3:5])
	sec = float(s[6:])
	#print s[:2],s[3:5],s[6:]
	return deg + (mn + sec /60.0) / 60.0

if __name__ == "__main__":
	for l in sys.stdin:
		t = l.split(';')
		lt = t[4][1:-3]
		lg = t[5][1:-3]
		#convert2dec(lt)
		print t[-1].strip()[:-1], t[-1].strip(), t[-1].strip()[-1:], t[-2].strip(), convert2dec(lt), convert2dec(lg)
