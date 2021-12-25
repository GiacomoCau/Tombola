	
	//var r1=0
	function R(rs, sm, z) {
		// rs: elementi per riga
		// z[]: elementi per colonna
		return R(rs, sm, z, [])
		
		function* R(rs, sm, z, v) {
			if (z.length == 0) {
				yield v
			}
			/*else {
				for (var n=rs>=z.length||sm[0]===0?1:0, e=rs&&z[0]?1:0; n<=e; n+=1) {
					var nrs = rs-n
					if (nrs < 0) { r1+=1; continue }
					yield* R(nrs, shift1(sm), shift1(z), v.concat(n))
				}
			}*/
			else for (var n=rs>=z.length||sm[0]===0?1:0, e=rs&&z[0]?1:0; n<=e; n+=1) {
				yield* R(rs-n, shift(sm), shift(z), v.concat(n))
			}
		}
	}
	
	//var m1=0,m2=0
	function M(r, rs, z) {
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return M(z, [])
		
		function* M(z, m) {
			if (m.length == r) {
				// se ci sono totali colonna < 1 (|| == 0)  scarta
				//for (var i in z) { var n=0; for (var j in m) n+=m[j][i]; if (n<1) { m2+=1; return }}
				yield m
			}
			/*else {
				var sm = m.length < r-1 ? [] : sum(m) 
				for (var v of R(rs, sm, z)) {
					var nz = subv(z, v)
					if (!nz) { m1+=1; continue }
					yield* M(nz, m.concat([v]))
				}
			}*/
			else for (var v of R(rs, m.length<r-1 ? [] : sum(m), z)) {
				yield* M(subv(z, v), m.concat([v]))
			}
			function sum(m) {
				var r=[]; for (var v of m) for (var i in v) r[i]=(r[i]||0)+v[i]; return r
			}
			function subv(a, b) { // clona a[] sottraendo agli elementi b[]
				var r=a.slice(0); for (var i in a) r[i]-=b[i]; return r
			}	
		}
	}
	
	//var t1=0,t2=0,t3=0
	function T(s, r, rs, z) {
		// s: numero schede
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return T(z, [])
		
		function* T(z, t) {
			if (t.length == s) {
				// se ci sono totali di colonna > zero scarta
				//for (var n of z) if (n > 0) { t3+=1; return }
				yield t
			}
			/*else {
				var sr = s-t.length-1 // schede successive
				var nz = subn(z, sr, 1) // sottraendo da z 1 per ogni colonna di ogni scheda successiva
				if (!nz) { t1+=1; return } // deve residuare almeno 1 per ogni colonna della corrente
				for (var m of M(r, rs, nz)) {
					var nz = subm(z, m, sr) // sottraendo da z m per ogni colonna
					if (!nz) { t2+=1; continue } // deve residuare almeno 1 per ogni colonna di ogni scheda successiva
					yield* T(nz, t.concat([m]))
				}
			}*/	
			else for (var m of M(r, rs, subn(z, s-t.length-1))) {
				yield* T(subm(z, m), t.concat([m]))
			}	
			function subn(z, n) { // clona z[] sottranedo agli elementi s
				var r=z.slice(0); for (var i in z) r[i]-=n; return r
			}
			function subm(z, m) { // clona z[] sottranedo le colonne di m[,]
				var r=z.slice(0); for (var v of m) for (var i in v) r[i]-=v[i]; return r
			}
		}
	}
	
	module.exports = { R/*,r1*/, M/*,m1,m2*/, T/*,t1,t2,t3*/ }
