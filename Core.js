	
	if (typeof module != 'undefined') module.exports = { R, M, T }
	
	function R(tr, tc, sc) {
		// tr: totale per riga
		// tc[]: totali per colonna
		// sc[]: somme per colonna
		//if (tr != tc.reduce((a,b)=>a+b)-sc.reduce((a,b)=>a+b)) throw 'T: totali riga != totali colonna'
		return R(0, tr, [])
		
		function* R(i, tr, r) {
			if (i == tc.length) // i ~ r.length
				yield r
			else for (var n=tr>=tc.length-i||sc[i]===0?1:0, e=tr&&tc[i]?1:0; n<=e; n+=1) {
			//else for (var n=tr<tc.length-i&&sc[i]!==0?0:1, e=tr&&tc[i]?1:0; n<=e; n+=1) {
				yield* R(i+1, tr-n, r.concat(n))
			}
		}
		/* TODO in sostituzione del precedente
		return R(tr, [])
		
		function* R(tr, r) {
			if (r.length == tc.length)
				yield r
			else for (var n=tr>=tc.length-r.length||sc[r.length]===0?1:0, e=tr&&tc[r.length]?1:0; n<=e; n+=1) {
				yield* R(tr-n, r.concat(n))
			}
		}
		*/
	}
	
	function M(nr, tr, tc) {
		// nr: numero righe per scheda
		// tr: totale per riga
		// tc[]: totali per colonna
		//if (nr*tr != tc.reduce((a,b)=>a+b)) console.log('T: totali riga != totali colonna')
		return M(tc, [])
		
		function* M(tc, m) {
			if (m.length == nr)
				yield m
			else for (var r of R(tr, tc, m.length<nr-1 ? [] : sumc(m))) {
				yield* M(subr(tc, r), m.concat([r]))
			}
			function sumc(m) { // somma le colonne di m
				var r=[]; for (var v of m) for (var i=0; i<v.length; i+=1) r[i]=(r[i]||0)+v[i]; return r
			}
			function subr(tc, r) { // clona tc[] sottraendo agli elementi gli elementi di r[]
				tc=tc.slice(0); for (var i=0; i<tc.length; i+=1) tc[i]-=r[i]; return tc
			}	
		}
	}
	
	function T(ns, nr, tr, tc) {
		// ns: numero schede
		// nr: numero righe per scheda
		// tr: totale per riga
		// tc[]: totali per colonna
		if (ns*nr*tr != tc.reduce((a,b)=>a+b)) console.log('T: totali riga != totali colonna')
		var p = new P(tc)
		return T(tc, [])
		
		function* T(tc, t) {
			if (t.length == ns)
				yield p.num(t)
			else for (var m of M(nr, tr, subn(tc, ns-1-t.length))) {
				yield* T(subm(tc, m), t.concat([m]))
			}	
			function subn(tc, n) { // clona tc[] sottranedo agli elementi n
				tc=tc.slice(0); for (var i=0; i<tc.length; i+=1) tc[i]-=n; return tc
			}
			function subm(tc, m) { // clona tc[] sottranedo agli elementi le colonne di m[,]
				tc=tc.slice(0); for (var r of m) for (var i=0; i<r.length; i+=1) tc[i]-=r[i]; return tc
			}
		}
	}

	var shuffle = false
	
	function N(tc) {
		for (var m=[], d=0, i=0; i<tc.length; d+=10, i+=1) {
			for (var v=[], n=tc[i], j=0; j<n; j+=1) v.push(d+j+(!d||tc[0]>9?1:0))
			m.push(!shuffle ? v : v.shuffle())
		}
		this.get = ()=> m.map(r => r.slice())
	}
	
	function P(tc) {
		var n = new N(tc)
		this.num = t => {
			var nn = n.get()
			t = t.map(m => m.map(r => r.slice()))
			for (var s of t) {
				for (var i=0; i<s[0].length; i+=1) {
					if (shuffle) {
						for (var v=[], j=0; j<s.length; j+=1) {
							if (s[j][i] == 0) continue
							v.push(nn[i].shift())
						}
						v.sort((a, b) => a - b)
					}
					for (var j=0; j<s.length; j+=1) {
						if (s[j][i] == 0) continue
						s[j][i] = (shuffle?v:nn[i]).shift()
					}
				}
			}
			return t
		}
	}
	
	Array.prototype.shuffle = function() {
		// While there remain elements to shuffle...
		for (var i=this.length; i!=0; ) {
			// Pick a remaining element...
			var j = Math.floor(Math.random() * i--)
			// And swap it with the current element.
			//[this[i], this[j]] = [this[j], this[i]]
			var t=this[i]
			this[i] = this[j]
			this[j] = t
		}
		return this
	}

	//console.log(new N([9,9,9]).get())
	//console.log(new N([9,10,11]).get())
	//console.log(new N([10,10,10]).get())
	/*
	var p = new P([3,2,2,2,3])
	var m = [
		[ [1,1,1,0,0], [1,0,0,1,1] ],
		[ [1,1,0,0,1], [0,0,1,1,1] ]
	]
	console.log(p.p(m))
	console.log(p.p([
		[ [1,1,1,0,0], [1,0,0,1,1] ],
		[ [1,1,0,0,1], [0,0,1,1,1] ]
	]))
	//*/
	
