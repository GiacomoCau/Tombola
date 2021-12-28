	
	module.exports = { R, M, T }
	
	function R(rs, sm, z) {
		// rs: elementi per riga
		// sm[]: somme per colonna
		// z[]: elementi per colonna
		return R(0, rs, [])
		
		function* R(i, rs, v) {
			if (i == z.length)
				yield v
			else for (var n=rs>=z.length-i||sm[i]===0?1:0, e=rs&&z[i]?1:0; n<=e; n+=1) {
				yield* R(i+1, rs-n, v.concat(n))
			}
		}
	}
	
	function M(r, rs, z) {
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return M(z, [])
		
		function* M(z, m) {
			if (m.length == r)
				yield m
			else for (var v of R(rs, m.length<r-1 ? [] : sum(m), z)) {
				yield* M(subv(z, v), m.concat([v]))
			}
			function sum(m) {
				var r=[]; for (var v of m) for (var i in v) r[i]=(r[i]||0)+v[i]; return r
			}
			function subv(z, v) { // clona a[] sottraendo agli elementi b[]
				var r=z.slice(0); for (var i in z) r[i]-=v[i]; return r
			}	
		}
	}
	
	function T(s, r, rs, z) {
		// s: numero schede
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return T(z, [])
		
		function* T(z, t) {
			if (t.length == s)
				yield t
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
	

