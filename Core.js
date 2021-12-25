	
	function R(rs, sm, z) {
		// rs: elementi per riga
		// z[]: elementi per colonna
		return R(rs, sm, z, [])
		
		function* R(rs, sm, z, v) {
			if (z.length == 0)
				yield v
			else for (var n=rs>=z.length||sm[0]===0?1:0, e=rs&&z[0]?1:0; n<=e; n+=1) {
				yield* R(rs-n, shift(sm), shift(z), v.concat(n))
			}
			function shift(v) { // clona v[] dal secondo elemento
				return v.slice(1)
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
			function subv(a, b) { // clona a[] sottraendo agli elementi b[]
				var r=a.slice(0); for (var i in a) r[i]-=b[i]; return r
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
	
	module.exports = { R, M, T }
