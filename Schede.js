	console.log(new Date() /*.getTime()*/)

	let { R,r1, M,m1,m2, T,t1,t2,t3 } = require('./Core.js')
	
	//writeAll( R(1, [1,0]) ) // 1
	//writeAll( R(3, 3, [10,10,10,10,10,10]) ) // 20
	//writeAll( R(3, 3, [3,1,1,1,1,3]) ) // 4
	//writeAll( R(3, 3, [2,2,3,2,2,3]) ) // 4
	//writeAll( R(3, 3, [4,4,1,1,1,1]) ) // 4
	//writeAll( R(3, 3, [3,2,2,2,4,4]) ) // 1
	//writeAll( R(3, 4, [1,1,3,1,1,3]) ) // 6

	//writeAll( M(2, 2, [1,2,2]) ) // 4 (m2:1)
	//writeAll( M(3, 2, [2,1,2,2]) ) // 45 (m2:6)
	//writeAll( M(3, 3, [3,2,2,2,3]) ) // 366 (m2:66)
	//writeAll( M(3, 4, [3,2,2,2,2,3]) ) // 540 (m2:24)
	//writeAll( M(3, 4, [3,2,2,2,3,3]) ) // 906 (m2:90)
	//writeAll( M(3, 4, [3,3,2,2,3,3]) ) // 1374 (m2:217)
	//writeAll( M(3, 4, [4,4,4,4,4,4]) ) // 2600 (m2:735)
	//writeAll( M(3, 5, [3,3,2,2,3,3]) ) // 30
	
	//writeAll( M(3, 5, [3,3,3,3,3,3]) ) // 210 (m2:-6)
	//writeAll( M(3, 5, [4,4,4,4,4,4]) ) // 210 (m2:-6)
	
	//writeAll( M(3, 5, [3,3,3,3,3,3,3]) ) // 7700 (m2:-6)
	
	//writeAll( M(3, 5, [ 3, 3, 3, 3, 3, 3, 3, 3]) ) // 107.520
	//writeAll( M(3, 5, [10,10,10,10,10,10,10,10]) ) // 107.520
	
	//writeAll( M(3, 5, [ 2, 3, 3, 3, 3, 3, 3, 3, 3]) ) // 647.640
	//writeAll( M(3, 5, [ 2, 3, 3, 3, 3, 3, 3, 3, 4]) ) // 647.640
	
	//writeAll( M(3, 5, [ 3, 3, 3, 3, 3, 3, 3, 3, 3]) ) // 735.210
	//writeAll( M(3, 5, [ 3, 4, 4, 4, 4, 4, 4, 4, 5]) ) // 735.210
	//writeAll( M(3, 5, [ 9,10,10,10,10,10,10,10,11]) ) // 735.210
	//writeAll( M(3, 5, [10,10,10,10,10,10,10,10,10]) ) // 735.210
	
	//writeAll( T(2, 3, 4, [3,4,4,4,4,5]) ) // 36.288 (m2:425)
	//writeAll( T(2, 3, 4, [4,4,4,4,4,4]) ) // 67.950 (m2:735)
	//writeAll( T(3, 3, 4, [6,6,6,6,6,6]) ) // 73.113.840 (m2:829.635)
	//writeAll( T(6, 3, 5, [ 9,10,10,10,10,10,10,10,11]) ) // > 25.575.000
	writeAll( T(6, 3, 5, [10,10,10,10,10,10,10,10,10]) )
	
	
	function writeAll(ts) {
		var i=0, tm=new Date().getTime(), inc=5000, lim=inc	
		for (var t of ts) {
			i+=1
			//console.log(i + ')')
			writeT(i, t)
			//if (i==100) break
			if (i > lim) lim += (console.log(lim, '('+((new Date().getTime()-tm)/1000)+')', 'Scartati:',t1,t2,t3,m1,m2,r1), inc) // logging
		}
		console.log('Totale: ' + i, '\nScartati:',t1,t2,t3,m1,m2,r1)
	}
	
	function writeT(i, t) {
		if (Array.isArray(t)) {
			if (Array.isArray(t[0])) {
				console.log(i + ')')
				if (Array.isArray(t[0][0])) {
					for (var m of t) {
						for (var r of m) console.log(r.join(''))
						console.log()
					}
				}
				else {
					for (var r of t) console.log(r.join(''))
					//console.log()
				} 
				console.log()
				return 
			}
			console.log(i + ')', t.join(''))
		}
	}
