var min = Math.min, max = Math.max

function sum(v) { // somma tutti gli elementi di v
	for (var r=0, i=0, e=v.length; i<e; i+=1) r+=v[i]; return r
}

function sum1(mx, v) { // somma tutti gli elementi di v tranne il primo
	for (var r=0, i=1, e=v.length; i<e; i+=1) r+=min(mx,v[i]); return r
}

function shift1(v) { // clona v dal secondo elemento
	for (var r=[], i=1, e=v.length; i<e; i+=1) r[i-1]=v[i]; return r
}

function sub0(v, a) { // clona v valorizzando il primo elemento con la differenza tra il primo elemento ed a
	for (var r=[v[0]-a], i=1, e=v.length; i<e; i+=1) r[i]=v[i]; return r
}

function subn(c, a) { // crea un vettore con la differenza fra i corrispondenti elementi del primo e del secondo vettore
	for (var r=[], i=0, e=c.length; i<e; i+=1) r[i]=c[i]-a[i]; return r
}

function R(mx, r, c) {
	var r1 = sum1(mx, r)
	return R(r[0], c)
	
	function R(r0, c) {
		if (c.length == 2) {
			for (var v=[], a0=max(0,r0-mx,c[0]-r1); a0<=mx; a0+=1) {
				var a1 = r0 - a0
				if (a1 < 0) break
				v.push( [a0, a1] ) // aggiunge [a0, a1] in fondo alla riga v
			}
			return v
		}
		for (var v=[], a0=max(0,c[0]-r1); a0<=mx; a0+=1) {
			var a1 = r0 - a0 
			if (a1 < 0) break
			var vs = R(a1, shift1(c))
			for (var i in vs) {
				var vi = vs[i]
				vi.unshift(a0) // aggiunge a0 in testa a vi
				v.push(vi) // aggiunge vi in fondo a v
			}
		}
		return v
	}
}

function M(mx, r, c) {
	if (sum(r) != sum(c)) return
	for (var mxr=mx*c.length, i=0; i<r.length; i+=1) if (r[i]> mxr) return 
	for (var mxc=mx*r.length, i=0; i<c.length; i+=1) if (c[i]> mxc) return 
	return M(r, c)
	
	function M(r, c) {
		if (r.length == 2) { // 2 righe
			if (c.length == 2) { // 2 righe, 2 colonne
				for (var m=[], a00=max(0,c[0]-mx,r[0]-sum1(mx,c)); a00<=mx; a00+=1) {
					var a01 = r[0] - a00
					if (a01 < 0) break
					var a10 = c[0] - a00
					if (a10 < 0) break
					var a11 = r[1] - a10 // = c[1] - a01
					if (a11 < 0 || a11 > mx) break
 					m.push( [[a00, a01], [a10, a11]] ) // genera la matrice 2x2
				}
				return m
			}
			// 2 righe, più di 2 colonne
			for (var m=[], a0=max(0,c[0]-mx,r[0]-sum1(mx,c)); a0<=mx; a0+=1) {
				var a1 = c[0] - a0
				if (a1 < 0) break
				var ms = M( [r[0]-a0, r[1]-a1], shift1(c) )
				for (var i in ms) {
					var mi = ms[i]
					mi[0].unshift(a0) // aggiunge a0 in testa alla prima riga di mi
					mi[1].unshift(a1) // aggiunge a1 in testa alla seconda riga di mi
					m.push(mi) // aggiunge mi in fondo a m
				}
			}
			return m
		}
		// più di 2 righe
		for (var m=[], v=R(mx,r,c), i=0, e=v.length; i<e; i+=1) {
			var vi = v[i]
			var ms = M( shift1(r), subn(c, vi) )
			for (var j in ms) {
				var mj = ms[j]
				mj.unshift(vi) // aggiunge vi in testa mj
				m.push(mj) // aggiunge mj in fondo a m
			}
		}
		return m
	}
}

function Mn(mx, r, c) {
	if (sum(r) != sum(c)) return
	for (var mxr=mx*c.length, i=0; i<r.length; i+=1) if (r[i]> mxr) return 
	for (var mxc=mx*r.length, i=0; i<c.length; i+=1) if (c[i]> mxc) return 
	return Mn(r, c)
	
	function Mn(r, c) {
		if (r.length == 2) {
			if (c.length == 2) {
				for (var n=0, a00=max(0,c[0]-mx,r[0]-sum1(mx,c)); a00<=mx ; a00+=1) {
					var a01 = r[0] - a00
					if (a01 < 0) break
					var a10 = c[0] - a00
					if (a10 < 0) break
					var a11 = r[1] - a10 // = c[1] - a01
					if (a11 < 0 || a11 > mx) break
 					n += 1
				}
				return n
			}
			for (var n=0, a0=max(0,c[0]-mx,r[0]-sum1(mx,c)); a0<=mx; a0+=1) {
				var a1 = c[0] - a0
				if (a1 < 0) break
				n += Mn( [r[0]-a0, r[1]-a1], shift1(c) )
			}
			return n
		}
		for (var n=0, v=R(mx,r,c), i=0, e=v.length; i<e; i+=1) {
			var vi = v[i]
			n += Mn( shift1(r), subn(c, vi) )
		}
		return n
	}
}

// sostituisce la precedente, aggiunge il logging
function Mn(mx, r, c) {
	if (sum(r) != sum(c)) return
	for (var mxr=mx*c.length, i=0; i<r.length; i+=1) if (r[i]> mxr) return 
	for (var mxc=mx*r.length, i=0; i<c.length; i+=1) if (c[i]> mxc) return 
	var n=0, inc=100000, lim=inc // counting, logging
	Mn(r, c)
	return n
	
	function Mn(r, c) {
		if (r.length == 2) { // 2 righe
			if (c.length == 2) { // 2 righe, 2 colonne
				for (var a00=max(0,c[0]-mx,r[0]-sum1(mx,c)); a00<=mx; a00+=1) {
					var a01 = r[0] - a00
					if (a01 < 0) break
					var a10 = c[0] - a00
					if (a10 < 0) break
					var a11 = r[1] - a10 // = c[1] - a01
					if (a11 < 0 || a11 > mx) break
 					n += 1 // counting
				}
				return
			}
			// 2 righe, più di 2 colonne
			for (var a0=max(0,c[0]-mx,r[0]-sum1(mx,c)); a0<=mx; a0+=1) {
				var a1 = c[0] - a0
				if (a1 < 0) break
				Mn( [r[0]-a0, r[1]-a1], shift1(c) )
			}
			return
		}
		// più di 2 righe
		for (var v=R(mx,r,c), i=0, e=v.length; i<e; i+=1) {
			var vi = v[i]
			Mn( shift1(r), subn(c, vi) )
			if (n > lim) lim += (writeN(lim), inc) // logging
		}
	}
}

function check(m, mx, r, c) {
	if (m.length != r.length) return false
	for (var i=0; i<r.length; i+=1) {
		var rn = m[i]
		if (rn.length != c.length) return false
		for (var tr=0, j=0; j<c.length; j+=1)  {
			if (rn[j] < 0 || rn[j]> mx) return false 
			tr += rn[j]
		}
		if (tr != r[i]) return false
	}
	for (var i=0; i<c.length; i+=1) {
		for (var tc=0, j=0; j<r.length; j+=1) tc += m[j][i]
		if (tc != c[i]) return false
	}
	return true
}

// for node.js
if (typeof exports != 'undefined') {
	exports.M = M
	exports.Mn = Mn
	exports.check = check
	function writeN(n) { // logging
		console.log( '%d', n )
	}
}
