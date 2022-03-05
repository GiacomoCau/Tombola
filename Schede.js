	console.log(new Date() /*.getTime()*/)
	
	let { R, S, T } = require('./Core.js')
	
	if (process.argv[2]) eval(process.argv[2])
	
	//writeAll( R(1, [1,0], [1,1]) ) // 1
	//writeAll( R(1, [1,1], [1,1]) ) // 2
	//writeAll( R(1, [1,1], [1,0]) ) // 1
	//writeAll( R(3, [3,3,3,3,3,3], [1,1,1,1,1,1]) ) // 20
	//writeAll( R(3, [10,10,10,10,10,10], [1,1,1,1,1,1]) ) // 20
	//writeAll( R(3, [3,3,3,3,3,3], []) ) // 20
	//writeAll( R(3, [1,1,1,1,1,1], []) ) // 20
	//writeAll( R(3, [1,0,1,0,1,1], []) ) // 4
	
	//writeAll( R(3, 3, [2,2,3,2,2,3]) ) // 4
	//writeAll( R(3, 3, [4,4,1,1,1,1]) ) // 4
	//writeAll( R(3, 3, [3,2,2,2,4,4]) ) // 1
	//writeAll( R(3, 4, [1,1,3,1,1,3]) ) // 6
	
	//writeAll( S(2, 1, [1,1]) ) // 2
	//writeAll( S(2, 1, [2,2]) ) // 2
	
	//writeAll( S(2, 2, [2,1,2]) ) // 4 
	//writeAll( S(2, 2, [2,2,2]) ) // 6 
	//writeAll( S(2, 2, [2,1,2,2]) ) // 6
	//writeAll( S(2, 2, [2,2,2,2]) ) // 6

	//writeAll( S(3, 2, [2,1,2,2]) ) // 45 
	//writeAll( S(3, 2, [2,2,2,2]) ) // 90 
	//writeAll( S(3, 3, [3,2,2,2,3]) ) // 366 
	//writeAll( S(3, 4, [3,2,2,2,2,3]) ) // 540
	//writeAll( S(3, 4, [3,2,2,2,3,3]) ) // 906
	//writeAll( S(3, 4, [3,3,2,2,3,3]) ) // 1374
	//writeAll( S(3, 4, [3,3,3,3,3,3]) ) // 2640
	//writeAll( S(3, 4, [4,4,4,4,4,4]) ) // 2640
	//writeAll( S(3, 5, [3,3,2,2,3,3]) ) // 30
	
	//writeAll( S(3, 5, [3,3,3,3,3,3]) ) // 210
	//writeAll( S(3, 5, [4,4,4,4,4,4]) ) // 210
	
	//writeAll( S(3, 5, [3,3,3,3,3,3,3]) ) // 7700
	
	//writeAll( S(3, 5, [ 3, 3, 3, 3, 3, 3, 3, 3]) ) // 107.520
	//writeAll( S(3, 5, [10,10,10,10,10,10,10,10]) ) // 107.520
	
	//writeAll( S(3, 5, [ 2, 3, 3, 3, 3, 3, 3, 3, 3]) ) // 647.640
	//writeAll( S(3, 5, [ 2, 3, 3, 3, 3, 3, 3, 3, 4]) ) // 647.640
	
	//writeAll( S(3, 5, [ 3, 3, 3, 3, 3, 3, 3, 3, 3]) ) // 735.210
	//writeAll( S(3, 5, [ 3, 4, 4, 4, 4, 4, 4, 4, 5]) ) // 735.210
	//writeAll( S(3, 5, [ 9,10,10,10,10,10,10,10,11]) ) // 735.210
	//writeAll( S(3, 5, [10,10,10,10,10,10,10,10,10]) ) // 735.210

	//writeAll( T(2, 2, 3, [3,2,2,2,3]) ) // 72
	//writeAll( T(2, 3, 4, [3,4,4,4,4,5]) ) // 36.288
	//writeAll( T(2, 3, 4, [4,4,4,4,4,4]) ) // 67.950
	//writeAll( T(3, 3, 4, [6,6,6,6,6,6]) ) // 73.113.840
	//writeAll( T(6, 3, 5, [ 9,10,10,10,10,10,10,10,11]) ) // > 4.062.363.048 < 735.210^4
	//writeAll( T(6, 3, 5, [10,10,10,10,10,10,10,10,10]) )
	
	
	function writeAll(ts) {
		var i=0, tm=new Date().getTime(), inc=5000, lim=inc
		for (var t of ts) {
			i+=1
			//console.log(i + ')')
			writeT(i, t)
			//if (i==100) break
			if (i > lim) lim += (console.log(lim, '('+(new Date().getTime()-tm)/1000+')'), inc) // logging
		}
		console.log('Totale: ' + i)
	}
	
	function writeT(i, t) {
		if (Array.isArray(t)) {
			if (!Array.isArray(t[0])) {
				console.log(i + ')', t.join(''))
			}
			else {
				console.log(i + ')')
				if (!Array.isArray(t[0][0])) {
					for (var r of t) console.log(r.join(''))
				}
				else {
					for (var m of t) {
						//for (var r of m) console.log(r.join(' '))
						for (var r of m) console.log(r.map(n=>!n?'  ':(' '+n).substr(-2)).join('|'))
						console.log()
					}
				} 
				console.log() 
			}
		}
	}
