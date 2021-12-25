
	function shift1(v) { // clona v[] dal secondo elemento
		//for (var r=[], i=1, e=v.length; i<e; i+=1) r[i-1] = v[i]; return r
		return v.slice(1)
	}
	//console.log(shift1([1,2,3])) // [2,3]
	
	function subn(z, s, n) { // clona z[] sottranedo agli elementi s
		if (s==0) return z
		//for (var r=[], i=0, e=z.length; i<e; i+=1) r[i] = Math.max(0, z[i]-s); return r
		//var r=[]; for (var i in z) r[i] = Math.max(0, z[i]-s); return r
		//var r=[]; for (var i in z) if ((r[i] = z[i] - s) < (n||0)) return; return r
		var r=z.slice(0); for (var i in z) if ((r[i] -= s) < (n||0)) return; return r
	}
	//console.log(subn([3,6,9],0)) // [3,6,9]
	//console.log(subn([3,6,9],3)) // [0,3,6]
	//console.log(subn([4,6,9],3)) // [0,3,6]
	
	function subv(a, b) { // clona a[] sottraendo agli elementi b[]
		//for (var r=[], i=0, e=a.length; i<e; i+=1) if ((r[i] = a[i] - b[i]) < 0) return; return r
		//var r=[]; for (var i in a) if ((r[i] = a[i] - b[i]) < 0) return; return r
		var r=a.slice(0); for (var i in a) if ((r[i] -= b[i]) < 0) return; return r
	}
	//console.log(subv([3,6,9],[3,6,9])) // [0,0,0]
	//console.log(subv([3,6,9],[4,6,9])) // undefined
	
	function subm(z, m, n) { // clona z[] sottranedo le colonne di m[,]
		//var r=[]; for (var n of z) r.push(n)
		//for (var v of m) for (var i=0, e=v.length; i<e; i+=1) if ((r[i] -= v[i]) < 0) return
		//return r
		var r=z.slice(0); for (var v of m) for (var i in v) if ((r[i] -= v[i]) < (n||0)) return; return r
	}
	//console.log(subm([3,6,9],[[1,2,3],[1,2,3],[1,2,3]])) // [0,0,0]
	//console.log(subm([3,6,9],[[1,2,3],[1,2,3],[1,2,4]])) // undefined
	
	/* non più necessari, eliminare
	function sum(a, b) { // somma a[] a b[]
		//for (var r=[], i=0, e=a.length; i<e; i+=1) r[i] = a[i] + b[i]; return r
		var r=[]; for (var i in a) r[i]=(a[i]||0)+b[i]; return r
	}
	//console.log(sum([1,2,3],[1,2,3])) // [2,4,6]
	
	function min(v, n) { // clona z[] sottranedo agli elementi s
		//for (var r=[], i=0, e=v.length; i<e; i+=1) r[i] = Math.min(v[i], n); return r
		//var r=[]; for (i in v) r.push(Math.min(v[i],n)); return r
		//var r=[]; for (i in v) r[i]=Math.min(v[i],n); return r
		var r=[]; for (e of v) r.push(Math.min(e,n)); return r
	}
	//console.log(min([1,2,3,4,5,6,7], 3)) // [1,2,3,3,3,3,3]
	*/
	
	
	
	//for (var i in [1,2,3]) console.log(i) // 1 2 3 
	//var v=[1,2,3]; console.log([].concat([v])) // [[1,2,3]]
	//console.log(2!=NaN)
	//console.log(3!=3)
	//console.log(2<NaN)
	
	var r1=0	
	function R(r, rs, z) {
		// rs: elementi per riga
		// z[]: elementi per colonna
		//console.log('start', r, rs, z.join(''))
		return R(rs, z, [])
		
		function* R(rs, z, v) {
			if (z.length == 0) {
				//console.log('finish',rs,v.join(''))
				yield v
			}
			else {
				var nr=0; for (var i in z) if (i!=0 && z[i]===r) nr+=1
				for (var n=z.length>rs/*&&z[0]!=r/*&&z.length>nr*/?0:1, e=z[0]>0&&rs>0?1:0; n<=e; n+=1) {
					//console.log('loop', rs, z.join(''), v.join('')+n)
					var nrs = rs-n
					if (nrs < 0) { r1+=1; continue }
					yield* R(nrs, shift1(z), v.concat(n))
				}
			}
		}
	}
	
	var m1=0,m2=0,m3=0 
	function M(r, rs, z) {
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return M(z, [])
		
		function* M(z, m) {
			if (m.length == r) {
				// se ci sono totali colonna < 1 (|| == 0)  scarta
				for (var i in z) {
					var n=0; for (var j in m) n+=m[j][i];
					if (n<1) {
						m2+=1;
						//console.log('*** ', z.join('')); writeT('*** '+m2,m);
						return
					}
				}
				yield m
			}
			else {
				//console.log(1+m.length, z.join())
				//var lst = m.length==r-1 
				//for (var v of R(lst?r/*-m.length*/:null, rs, z)) {
				//var nz = subn(z,m.length<=r-1?1:0)
				//if (!nz) { m3+=1; return }
				for (var v of R(r, rs, z)) {
					var nz = subv(z, v)
					if (!nz) { m1+=1; continue }
					yield* M(nz, m.concat([v]))
				}
			}
		}
	}
	
	var t1=0,t2=0,t3=0
	function T(s, r, rs, z) {
		// s: numero schede
		// r: righe per scheda
		// rs: elementi per riga
		// z[]: elementi per colonna
		return T(z, [])
		
		function* T(z, t) {
			if (t.length == s) {
				// se ci sono totali di colonna > zero scarta
				for (var n of z) if (n > 0) { /*t3+=1;*/ return }
				yield t
			}
			else {
				var sr = s-t.length-1 // schede successive
				var nz = subn(z, sr, 1) // sottraendo da z 1 per ogni colonna di ogni scheda successiva
				//console.log(1+t.length, z.join(), sr, !nz?nz:nz.join())
				if (!nz) { /*t1+=1;*/ return } // deve residuare almeno 1 per ogni colonna della corrente
				for (var m of M(r, rs, nz)) {
					var nz = subm(z, m, sr) // sottraendo da z m per ogni colonna
					if (!nz) { /*t2+=1;*/ continue } // deve residuare almeno 1 per ogni colonna di ogni scheda successiva
					yield* T(nz, t.concat([m]))
				}
			}	
		}
	}
	
	module.exports = { R,r1, M,m1,m2,m3, T,t1,t2,t3 }
