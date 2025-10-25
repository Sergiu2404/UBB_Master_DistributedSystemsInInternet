import os
import sys
import math
import struct
import numpy
import haversine
import reverse_geocoder
NAN = numpy.double(-999999999.99)
ARIA = numpy.double(0.0)
def str2float(s):
    try:    return numpy.double(s)
    except: return NAN
    if numpy.isnan(s): return numpy.nan
    return numpy.double(s)
def dist(a, b):
    return numpy.double(haversine.haversine(a, b, unit=haversine.Unit.KILOMETERS))
def arie3(A, B, C):
    a = dist(B, C)
    b = dist(A, C)
    c = dist(A, B)
    p = (a + b + c) / 2
    ar = numpy.double(math.sqrt(p * (p - a) * (p - b) * (p - c)))
    xg = (A[0] + B[0] + C[0]) / 3
    yg = (A[1] + B[1] + C[1]) / 3
    return [ar, xg, yg]
def arie(nw, ne, se, sw):
    nans = 0
    if nw[0] == NAN: nans += 1
    if ne[0] == NAN: nans += 1
    if se[0] == NAN: nans += 1
    if sw[0] == NAN: nans += 1
    if nans >= 2: return [NAN]
    if nw[0] == NAN: return arie3(ne, se, sw)
    if ne[0] == NAN: return arie3(se, sw, nw)
    if se[0] == NAN: return arie3(sw, nw, ne)
    if sw[0] == NAN: return arie3(nw, ne, se)
    ts = arie3(nw, ne, se)
    tj = arie3(nw, se, sw)
    ar = ts[0] + tj[0]
    xg = (ts[0] * ts[1] + tj[0] * tj[1]) / ar
    yg = (ts[0] * ts[2] + tj[0] * tj[2]) / ar
    return [ar, xg, yg]
if __name__=="__main__":
    parIn = ['east=', 'west=', 'south=', 'north=', 'step=', 'key=', 'value=']
    parOut= ['lat', 'lon', 'name', 'admin1', 'admin2', 'cc']
    east,west,south,north,step = NAN, NAN, NAN, NAN, NAN
    key, value = "", ""
    i, j, m, n, total = 0, 0, 0, 0, 0
    lat, lon = 0.0, 0.0
    for i in range(len(sys.argv)):
        for j in range(len(parIn)):
            if sys.argv[i].startswith(parIn[j]): break
        if    j == 0: east  = str2float(sys.argv[i][5:])
        elif  j == 1: west  = str2float(sys.argv[i][5:])
        elif  j == 2: south = str2float(sys.argv[i][6:])
        elif  j == 3: north = str2float(sys.argv[i][6:])
        elif  j == 4: step  = str2float(sys.argv[i][5:])
        elif  j == 6: value = sys.argv[i][6:]
        elif  j == 5: 
            key = sys.argv[i][4:]
            if parOut.count(key) == 0: key = ""
        else: continue
    print()
    if east==NAN or west==NAN or south==NAN or north==NAN or step==NAN or key=="" or value=="":
        print(sys.argv, "ERR or Incorrect!")
        sys.exit()
    m, n, lf = int((north - south) / step) + 4, int((east - west) / step) + 4, struct.calcsize("d")
    wline, line =  [(NAN, NAN)] * n, [(NAN, NAN)] * n
    lat, lon = north + step, west - step
    nw, ne, sw, se = (lat, lon), (lat, lon + step * n), (lat - step * m, lon), (lat - step * m, lon + step * n)
    print(sys.argv)
    print("m", m, "n", n)
    f = open("_coords_", "wb")
    for i in range(m):
        lon = west - step
        for j in range(n):
            line[j] = (lat, lon)
            lon += step
        res = reverse_geocoder.search(line)
        for j in range(n):
            if res[j][key] != value:
                line[j] = (NAN, NAN)
        for j in range(n - 1):
            rez = arie(wline[j], wline[j + 1], line[j + 1], line[j])
            if rez[0] == NAN: continue
            ARIA += rez[0]
            b = struct.pack("ddd", rez[0], rez[1], rez[2])
            f.write(b)
        for j in range(n): wline[j] = line[j]
        lat -= step
    print("Aria:", ARIA)
    f.close()
    f = open("_coords_","rb")
    xg, yg = numpy.double(0.0), numpy.double(0.0)
    while True:
        b = f.read(3 * lf)
        if not b: break
        t = struct.unpack("ddd", b)
        xg += t[0] / ARIA * t[1]
        yg += t[0] / ARIA * t[2]
    f.close()
    os.remove("_coords_")
    res = reverse_geocoder.search((xg, yg))
    print("Centrul:", xg, yg, res)
    