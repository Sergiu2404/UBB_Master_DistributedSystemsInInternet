import sys
import numpy as np
import haversine as hs
import reverse_geocoder as rg
NAN = -999999999.99
def str2float(s):
    try:    return float(s)
    except: return NAN
    if np.isnan(s): return np.nan
    return float(s)
def axa(sir, p):
    s, d, ts, td = 0, p - 1, 0, 0
    c = 0.0
    ws, wd = -1, -1
    while s <= d:
        if ts == td:
            ts += sir[s]
            td += sir[d]
            ws, wd = s, d
            s += 1
            d -= 1
        elif ts < td:
            ts += sir[s]
            ws = s
            s += 1
        else:
            td += sir[d]
            wd = d
            d -= 1
    if ts + td > 0: c = (ws * ts + wd * td) / (ts + td)
    return c

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
    m, n = int((north - south) / step) + 4, int((east - west) / step) + 4
    lines, cols, coords, lons = [0] * m, [0] * n, [(0.0,0.0)] * n, [0.0] * n
    lat, lon = north + step, west - step
    nw, ne, sw, se = (lat, lon), (lat, lon + step * n), (lat - step * m, lon), (lat - step * m, lon + step * n)
    lw = hs.haversine(nw,sw, unit=hs.Unit.KILOMETERS)
    le = hs.haversine(ne,se, unit=hs.Unit.KILOMETERS)
    ln = hs.haversine(nw,ne, unit=hs.Unit.KILOMETERS)
    ls = hs.haversine(sw,se, unit=hs.Unit.KILOMETERS)
    print(sys.argv)
    print("m=", m, "n=", n)
    print("lw:nw-sw", lw, "km, lw /", m, "=", lw / m, "km")
    print("le:ne-se", le, "km, le /", m, "=", le / m, "km")
    print("ln:nw-ne", ln, "km, ln /", n, "=", ln / n, "km")
    print("ls:sw-se", ls, "km, ls /", n, "=", ls / n, "km", flush=True)
    for i in range(m):
        lon = west - step
        for j in range(n):
            coord = (lat, lon)
            coords[j] = coord
            lon += step
        res = rg.search(coords)
        for j in range(n):
            if res[j][key] == value:
                lines[i] += 1
                cols[j] += 1
                total += 1
        lat -= step
    print("Total puncte in (", key, ",", value, ") =", total)
    c = axa(lines, m)
    lat = north + step - step * c
    print(c, lines)
    c = axa(cols, n)
    lon = west - step + step * c
    print(c, cols)
    coord = (lat, lon)
    res = rg.search(coord)
    print("Centrul:", lat, lon, res)
    
