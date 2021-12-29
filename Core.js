	
	module.exports = { R, M, T }
	
	function R(tr, tc, sc) {
		// tr: totale per riga
		// tc[]: totali per colonna
		// sc[]: somme per colonna
		return R(0, tr, [])
		
		function* R(i, tr, r) {
			if (i == tc.length)
				yield r
			else for (var n=tr>=tc.length-i||sc[i]===0?1:0, e=tr&&tc[i]?1:0; n<=e; n+=1) {
				yield* R(i+1, tr-n, r.concat(n))
			}
		}
	}
	
	function M(nr, tr, tc) {
		// nr: righe per scheda
		// tr: totali per riga
		// tc[]: totali per colonna
		return M(tc, [])
		
		function* M(tc, m) {
			if (m.length == nr)
				yield m
			else for (var r of R(tr, tc, m.length<nr-1 ? [] : sumc(m))) {
				yield* M(subr(tc, r), m.concat([r]))
			}
			function sumc(m) { // somma le colonne di m
				var r=[]; for (var v of m) for (var i in v) r[i]=(r[i]||0)+v[i]; return r
			}
			function subr(tc, r) { // clona tc[] sottraendo agli elementi gli elementi di r[]
				tc=tc.slice(0); for (var i in tc) tc[i]-=r[i]; return tc
			}	
		}
	}
	
	function T(ns, nr, tr, tc) {
		// ns: numero schede
		// nr: numero righe per scheda
		// tr: totale per riga
		// tc[]: totali per colonna
		return T(tc, [])
		
		function* T(tc, t) {
			if (t.length == ns)
				yield t
			else for (var m of M(nr, tr, subn(tc, ns-t.length-1))) {
				yield* T(subm(tc, m), t.concat([m]))
			}	
			function subn(tc, n) { // clona tc[] sottranedo agli elementi n
				tc=tc.slice(0); for (var i in tc) tc[i]-=n; return tc
			}
			function subm(tc, m) { // clona tc[] sottranedo agli elementi le colonne di m[,]
				tc=tc.slice(0); for (var r of m) for (var i in r) tc[i]-=r[i]; return tc
			}
		}
	}
