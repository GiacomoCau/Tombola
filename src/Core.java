import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Core {
	
	public static void main(String[] args) throws Exception {
		
		//System.out.println( Rn(3, new int[] {1,1,1,1,1,1}, new boolean[] {true,true,true,true,true,true}) ); // 20
		//int i=1; for (var v: R(3, new int[] {1,1,1,1,1,1}, new boolean[] {true,true,true,true,true,true})) System.out.println(i++ + ") " + compact(v)); // 20
		
		//System.out.println( Cn(3, 5, new int[] {3,3,3,3,3,3}) ); // 210
		//int i=1; for (var c: C(3, 5, new int[] {3,3,3,3,3,3})) System.out.println(i++ + ")\n" + compact(c) + "\n"); // 210
		
		//System.out.println( Cn(3, 5, new int[] {3,3,3,3,3,3,3,3,3}) ); // 735.210
		//int i=1; for (var c: C(3, 5, new int[] {3,3,3,3,3,3,3,3,3})) System.out.println(i++ + ")\n" + compact(c) + "\n"); // 735.210
		
		//System.out.println( Tn(2, 2, 3, new int[] {3,2,2,2,3}) ); // 72
		//int i=1; for (var t: T(2, 2, 3, new int[] {3,2,2,2,3})) System.out.println(i++ + ")\n" + compact(t) + "\n"); // 72
		
		//System.out.println( Tn(3, 3, 4, new int[] {6,6,6,6,6,6}) ); // 73.113.840
		//System.out.println( Tn(6, 3, 5, new int[] {9,10,10,10,10,10,10,10,11}) ); // > 4.062.363.048 < 735.210^4
		
		System.out.println("finito!");
	}
	
	
	// Rows
	// tr: totale per riga
	// tc[]: totali per colonna
	// gt0[]: colonne maggiori di zero
	
	static Map<Key, int[]> row = new TreeMap<>();

	public static List<int[]> R(int tr, int[] tc, boolean[] gt0) {
		return R(0, tr, tc, gt0, new ArrayList<>());
	}
	private static List<int[]> R(int i, int tr, int[] tc, boolean[] gt0, List<Integer> r) {
		if (i == tc.length) {
			return list(row.computeIfAbsent(new Key(r.stream().mapToInt(Integer::intValue).toArray()), k-> k.a));
		}
		List<int[]> list = new ArrayList<>();
		for (int n=toint(tr>=tc.length-i || gt0!=null && !gt0[i]), e=toint(tr!=0 && tc[i]!=0); n<=e; n+=1) {
			list.addAll( R(i+1, tr-n, tc, gt0, add(r, n)) );
		}
		return list;
	}
	
	public static long Rn(int tr, int[] tc, boolean[] gt0) {
		return Rn(0, tr, tc, gt0);
	}
	private static long Rn(int i, int tr, int[] tc, boolean[] gt0) {
		if (i == tc.length)	return 1;
		long rn = 0;
		for (int n=toint(tr>=tc.length-i || gt0!=null && !gt0[i]), e=toint(tr!=0 && tc[i]!=0); n<=e; n+=1) {
			rn += Rn(i+1, tr-n, tc, gt0);
		}
		return rn;
	}
	
	private static int toint(boolean b) {
		return b ? 1 : 0;
	}
	private static <T> List<T> list(T r) {
		var l = new ArrayList<T>();
		l.add(r);
		return l;
	}
	private static <T> List<T> add(List<T> l, T t) {
		l = new ArrayList<T>(l);
		l.add(t);
		return l;
	}
	
	
	// Cards
	// nr: numero righe per scheda
	// tr: totale per riga 
	// tc[]: totali per colonna
	
	public static List<int[][]> C(int nr, int tr, int[] tc) {
		return C(nr, tr, tc, new ArrayList<>());
	}
	private static List<int[][]> C(int nr, int tr, int[] tc, List<int[]> c) {
		if (c.size() == nr) {
			return list(c.stream().toArray(int[][]::new));
		}
		List<int[][]> list = new ArrayList<>();
		for (int[] r: R(tr, tc, c.size()<nr-1 ? null : gt0(c))) {
			list.addAll( C(nr, tr, sub(tc, r), add(c, r)) );
		}
		return list;
	}

	public static List<int[]> Cs(int nr, int tr, int[] tc) {
		return Cs(nr, tr, tc, 0, new int[tc.length]);
	}
	private static List<int[]> Cs(int nr, int tr, int[] tc, int ml, int[] s) {
		if (ml == nr) return list(s);
		List<int[]> list = new ArrayList<>();
		for (int[] r: R(tr, tc, ml<nr-1 ? null : gt0(new boolean[s.length],s))) {
			list.addAll( Cs(nr, tr, sub(tc, r), ml+1, sum(s, r)) );
		}
		return list;
	}

	public static long Cn(int nr, int tr, int[] tc) {
		// tc = min(tc, 3);
		return Cn(nr, tr, tc, new boolean[tc.length], 0);
	}
	private static long Cn(int nr, int tr, int[] tc, boolean[] b, int ml) {
		if (ml == nr) return 1;
		long mn = 0;
		for (int[] r: R(tr, tc, ml<nr-1 ? null : b)) {
			mn += Cn(nr, tr, sub(tc, r), gt0(b.clone(), r), ml+1);
		}
		return mn;
	}
	
	private static boolean[] gt0(List<int[]> m) { // colonne di m maggiori di zero
		var r = new boolean[m.get(0).length];
		for (var v: m) for (var i=0; i<v.length; i+=1) r[i] |= v[i] > 0;
		return r;
	}
	private static boolean[] gt0(boolean[] b, int[] v) { // b or elementi di v maggiori di zero
		/*b.clone();*/ for (var i=0; i<v.length; i+=1) b[i] |= v[i] > 0;
		return b;
	}
	private static int[] sum(int[] s, int[] v) { // somma di s e v
		s = s.clone(); for (var i=0; i<v.length; i+=1) s[i] += v[i];
		return s;
	}
	
	
	// Tables
	// ns: numero schede
	// nr: numero righe per scheda
	// tr: totale per riga
	// tc[]: totali per colonna
	
	public static List<int[][][]> T(int ns, int nr, int tr, int[] tc) {
		if (ns*nr*tr != stream(tc).sum()) throw new  IllegalArgumentException();
		return T(ns, nr, tr, tc, new ArrayList<>());	
	}
	private static List<int[][][]> T(int ns, int nr, int tr, int[] tc, List<int[][]> t) {
		if (t.size() == ns) {
			return list(t.stream().toArray(int[][][]::new));
		}
		List<int[][][]> list = new ArrayList<>();
		for (int[][] c: C(nr, tr, sub(tc, ns-1-t.size()))) {
			list.addAll( T(ns, nr, tr, sub(tc, c), add(t, c)));
		}
		return list;
	}
	
	
	private static long inc=1000000, tmax=inc;

	public static long Tn(int ns, int nr, int tr, int[] tc) {
		if (ns*nr*tr != stream(tc).sum()) throw new  IllegalArgumentException();
		return Tn(ns, nr, tr, tc, 0);	
	}	
	private static long Tn(int ns, int nr, int tr, int[] tc, int tl) {
		if (tl == ns) return 1;
		long tn = 0;
		//for (int[][] s: C(nr, tr, sub(tc, ns-1-tl))) {
		//	tn += Tn(ns, nr, tr, sub(tc, s), tl+1);
		for (int[] s: Cs(nr, tr, sub(tc, ns-1-tl))) {
			tn += Tn(ns, nr, tr, sub(tc, s), tl+1);
			if (tn <= tmax) continue;
			System.out.println(tn);
			do tmax += inc;	while (tn > tmax); 
		}
		return tn;
	}

	static int[] sub(int[] v, int n) { // clona v[] sottranedo agli elementi n
		v=v.clone(); for (var i=0; i<v.length; i+=1) v[i]-=n; return v;
	}
	static int[] sub(int[] v, int[] v2) { // clona v[] sottranedo agli elementi v2
		v=v.clone(); for (var i=0; i<v.length; i+=1) v[i]-=v2[i]; return v;
	}
	static int[] sub(int[] v, int[][] m) { // clona v[] sottranedo agli elementi le colonne di m[,]
		v=v.clone(); for (var r: m) for (var i=0; i<r.length; i+=1) v[i]-=r[i]; return v;
	}

	
	static String compact(int[][][] t) {
		return stream(t).map(s-> compact(s)).collect(joining("\n\n"));
	}	
	static String compact(int[][] s) {
		return stream(s).map(r-> compact(r)).collect(joining("\n"));
	}	
	static String compact(int[] r) {
		return stream(r).mapToObj(i-> ""+i).collect(joining(","));
	}
	
	
	static class Key implements Comparable<Key>, Serializable {
		private static final long serialVersionUID = 1L;
		int[] a;
		
		Key(int[] a) {
			this.a = a;
		}
		Key(int[][] m) {
			a = new int[m[0].length];
			for (int i=0; i<a.length; i+=1) for (int j=0; j<m.length; j+=1) a[i] += m[j][i];
		}
		
		@Override
		public int hashCode() { 
			return Arrays.hashCode(a);
		}
		
		@Override
		public boolean equals(Object other) {
			if (this == other) return true;
			if (!(other instanceof Key)) return false;
			return Arrays.equals(a, ((Key) other).a);
		}
		
		@Override
		public int compareTo(Key other) {
			if (this == other) return 0;
			return Arrays.compare(a, other.a);
		}
		
		@Override
		public String toString() {
			return Core.compact(a);
		}
	}
	
	
	static long size() {
		var rt = Runtime.getRuntime();
		rt.gc();
		return rt.totalMemory() - rt.freeMemory();
		//return binarySize(rt.totalMemory() - rt.freeMemory());
	}
	
	static String binarySize(long size) {
		if (size <= 0) return "0";
		int groups = (int) (Math.log10(size) / Math.log10(1024));
		final String[] units = new String[] { "b", "Kb", "Mb", "Gb", "Tb", "Pb" };
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, groups)) + "  " + units[groups];
	}
	
	static int size(Object ... objects) throws IOException {
		try (
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
		) {
			objectStream.writeObject(objects);
			return byteArrayStream.size();
		}
	}
}
