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
		
		//System.out.println( Rn(3, new byte[] {1,1,1,1,1,1}, new boolean[] {true,true,true,true,true,true}) ); // 20
		//int i=1; for (var v: R(3, new byte[] {1,1,1,1,1,1}, new boolean[] {true,true,true,true,true,true})) System.out.println(i++ + ") " + compact(v)); // 20
		
		//System.out.println( Sn(3, 5, new byte[] {3,3,3,3,3,3}) ); // 210
		//int i=1; for (var s: S(3, 5, new byte[] {3,3,3,3,3,3})) System.out.println(i++ + ")\n" + compact(s) + "\n"); // 210
		
		//System.out.println( Sn(3, 5, new byte[] {3,3,3,3,3,3,3,3,3}) ); // 735.210
		//int i=1; for (var s: S(3, 5, new byte[] {3,3,3,3,3,3,3,3,3})) System.out.println(i++ + ")\n" + compact(s) + "\n"); // 735.210
		
		//System.out.println( Fn(2, 2, 3, new byte[] {3,2,2,2,3}) ); // 72
		//int i=1; for (var t: F(2, 2, 3, new byte[] {3,2,2,2,3})) System.out.println(i++ + ")\n" + compact(t) + "\n"); // 72
		
		//System.out.println( Fn(3, 3, 4, new int[] {6,6,6,6,6,6}) ); // 73.113.840
		//System.out.println( Fn(6, 3, 5, new int[] {9,10,10,10,10,10,10,10,11}) ); // < 735.210^4 == 1,57931381000812636020908057121e+35
		
		System.out.println("finito!");
	}
	
	
	// Righe
	// tr: totale per riga
	// tc[]: totali per colonna
	// gt0[]: colonne maggiori di zero
	
	static Map<Key, byte[]> row = new TreeMap<>();

	public static List<byte[]> R(int tr, byte[] tc, boolean[] gt0) {
		return R(tr, tc, gt0, 0, new ArrayList<>());
	}
	private static List<byte[]> R(int tr, byte[] tc, boolean[] gt0, int ci, List<Integer> r) {
		if (ci == tc.length) {
			return list(row.computeIfAbsent(new Key(tobyte(r.stream().mapToInt(Integer::intValue).toArray())), k-> k.a));
		}
		List<byte[]> rl = new ArrayList<>();
		for (int n=toint(tr>=tc.length-ci || gt0!=null && !gt0[ci]), e=toint(tr!=0 && tc[ci]!=0); n<=e; n+=1) {
			rl.addAll( R(tr-n, tc, gt0, ci+1, add(r, n)) );
		}
		return rl;
	}
	
	public static long Rn(int tr, byte[] tc, boolean[] gt0) {
		return Rn(tr, tc, gt0, 0);
	}
	private static long Rn(int tr, byte[] tc, boolean[] gt0, int ci) {
		if (ci == tc.length) return 1;
		long rn = 0;
		for (int n=toint(tr>=tc.length-ci || gt0!=null && !gt0[ci]), e=toint(tr!=0 && tc[ci]!=0); n<=e; n+=1) {
			rn += Rn(tr-n, tc, gt0, ci+1);
		}
		return rn;
	}
	
	private static int toint(boolean b) {
		return b ? 1 : 0;
	}
	static int[] toint(byte[] f) {
		int[] t = new int[f.length];
		for (int i=0; i<f.length; i+=1) t[i] = f[i];
		return t;
	}
	static byte[] tobyte(int[] f) {
		byte[] t = new byte[f.length];
		for (int i=0; i<f.length; i+=1) t[i] = (byte) f[i];
		return t;
	}
	private static <T> List<T> list(T t) {
		var l = new ArrayList<T>();
		l.add(t);
		return l;
		//return new ArrayList() {{add(t);}};
	}
	private static <T> List<T> add(List<T> l, T t) {
		l = new ArrayList<T>(l);
		l.add(t);
		return l;
		//return new ArrayList(l) {{add(t);}};
	}
	
	
	// Schede
	// nr: numero righe per scheda
	// tr: totale per riga 
	// tc[]: totali per colonna
	
	public static List<byte[][]> S(int nr, int tr, byte[] tc) {
		return S(nr, tr, tc, new ArrayList<>());
	}
	private static List<byte[][]> S(int nr, int tr, byte[] tc, List<byte[]> s) {
		if (s.size() == nr) {
			return list(s.stream().toArray(byte[][]::new));
		}
		List<byte[][]> sl = new ArrayList<>();
		for (byte[] r: R(tr, tc, s.size()<nr-1 ? null : gt0(s))) {
			sl.addAll( S(nr, tr, sub(tc, r), add(s, r)) );
		}
		return sl;
	}

	public static List<byte[]> Ss(int nr, int tr, byte[] tc) {
		return Ss(nr, tr, tc, 0, new byte[tc.length]);
	}
	private static List<byte[]> Ss(int nr, int tr, byte[] tc, int ss, byte[] s) {
		if (ss == nr) return list(s);
		List<byte[]> sl = new ArrayList<>();
		for (byte[] r: R(tr, tc, ss<nr-1 ? null : gt0(new boolean[s.length], s))) {
			sl.addAll( Ss(nr, tr, sub(tc, r), ss+1, sum(s, r)) );
		}
		return sl;
	}

	public static long Sn(int nr, int tr, byte[] tc) {
		return Sn(nr, tr, tc, 0, new boolean[tc.length]);
	}
	private static long Sn(int nr, int tr, byte[] tc, int ss, boolean[] b) {
		if (ss == nr) return 1;
		long sn = 0;
		for (byte[] r: R(tr, tc, ss<nr-1 ? null : b)) {
			sn += Sn(nr, tr, sub(tc, r), ss+1, gt0(b.clone(), r));
		}
		return sn;
	}
	
	private static boolean[] gt0(List<byte[]> m) { // colonne di m maggiori di zero
		var r = new boolean[m.get(0).length];
		for (var v: m) for (var i=0; i<v.length; i+=1) r[i] |= v[i] > 0;
		return r;
	}
	private static boolean[] gt0(boolean[] b, byte[] v) { // b or elementi di v maggiori di zero
		/*b.clone();*/ for (var i=0; i<v.length; i+=1) b[i] |= v[i] > 0;
		return b;
	}
	private static byte[] sum(byte[] s, byte[] v) { // somma di s e v
		s = s.clone(); for (var i=0; i<v.length; i+=1) s[i] += v[i];
		return s;
	}
	
	
	// Fogli
	// ns: numero schede
	// nr: numero righe per scheda
	// tr: totale per riga
	// tc[]: totali per colonna
	
	public static List<byte[][][]> F(int ns, int nr, int tr, byte[] tc) {
		if (ns*nr*tr != stream(toint(tc)).sum()) throw new  IllegalArgumentException();
		return F(ns, nr, tr, tc, new ArrayList<>());	
	}
	private static List<byte[][][]> F(int ns, int nr, int tr, byte[] tc, List<byte[][]> f) {
		if (f.size() == ns) {
			return list(f.stream().toArray(byte[][][]::new));
		}
		List<byte[][][]> fl = new ArrayList<>();
		for (byte[][] s: S(nr, tr, sub(tc, ns-1-f.size()))) {
			fl.addAll( F(ns, nr, tr, sub(tc, s), add(f, s)));
		}
		return fl;
	}
	
	
	private static long inc=1000000, tmax=inc;

	public static long Fn(int ns, int nr, int tr, byte[] tc) {
		if (ns*nr*tr != stream(toint(tc)).sum()) throw new  IllegalArgumentException();
		return Fn(ns, nr, tr, tc, 0);	
	}	
	private static long Fn(int ns, int nr, int tr, byte[] tc, int ts) {
		if (ts == ns) return 1;
		long tn = 0;
		for (byte[] s: Ss(nr, tr, sub(tc, ns-1-ts))) {
			tn += Fn(ns, nr, tr, sub(tc, s), ts+1);
			if (tmax > 0 && tn <= tmax) continue;
			System.out.println(tn);
			do tmax += inc;	while (tn > tmax); 
		}
		return tn;
	}

	static byte[] sub(byte[] z, int n) { // clona v[] sottranedo agli elementi n
		z=z.clone(); for (var i=0; i<z.length; i+=1) z[i]-=n; return z;
	}
	static byte[] sub(byte[] v, byte[] v2) { // clona v[] sottranedo agli elementi v2
		v=v.clone(); for (var i=0; i<v.length; i+=1) v[i]-=v2[i]; return v;
	}
	static byte[] sub(byte[] v, byte[][] m) { // clona v[] sottranedo agli elementi le colonne di m[,]
		v=v.clone(); for (var r: m) for (var i=0; i<r.length; i+=1) v[i]-=r[i]; return v;
	}

	
	static String compact(byte[][][] t) {
		return stream(t).map(s-> compact(s)).collect(joining("\n\n"));
	}	
	static String compact(byte[][] s) {
		return stream(s).map(r-> compact(r)).collect(joining("\n"));
	}	
	static String compact(byte[] r) {
		return stream(toint(r)).mapToObj(i-> ""+i).collect(joining(","));
	}
	
	
	static class Key implements Comparable<Key>, Serializable {
		private static final long serialVersionUID = 1L;
		byte[] a;
		
		Key(byte[] a) {
			this.a = a;
		}
		Key(byte[][] m) {
			a = new byte[m[0].length];
			for (int i=0; i<a.length; i+=1) for (int j=0; j<m.length; j+=1) a[i] += m[j][i];
			//range(0, a.length).forEach(i-> range(0, m.length).forEach(j-> a[i]=m[j][i]));
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
