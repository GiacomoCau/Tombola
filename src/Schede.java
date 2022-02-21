import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;

public class Schede {

	private static boolean number = true;
	private static boolean clone = false;
	private static boolean shuffle = true;
	private static boolean order = true;
		
	private static Map<Key, int[]> row;
	private static int[][][] all;
	private static Map<Key,int[][][]> sum = new TreeMap<>();
	private static List<Integer>[] numbers;
	
	static {
		try {
			//long tm = System.currentTimeMillis();
			//write("Schede.txt");
			//read("Schede.txt");
			init(); 
			//System.out.println(System.currentTimeMillis()-tm);
			//System.out.println(size());
			//row.forEach((k,v)-> System.out.println(k));
			//System.out.println(row.size() + " " + size(row)); System.out.println();
			//sum.forEach((k,v)-> System.out.println(k));
			//System.out.println(sum.size() + " " + size(sum)); System.out.println();
			//System.out.println(size(row, sum)); System.out.println();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		//long tm = System.currentTimeMillis();
		//for (int i=0; i<10000; i+=1) schede.get();
		//System.out.println(System.currentTimeMillis()-tm);
		//System.out.println("\n" + schede.compact() + "\n");
		//System.out.println("\n" + schede.boxed() + "\n");
		//for (int i=0; i<4; i+=1) System.out.println(schede.boxed(2,3));
		println(schede, Schede::compact, 3, 2);
		//println(schede, s-> boxed(s,3,2), 2, 2);
		System.out.println("finito!");
	}

	public static void println(Schede schede, Function<int[][][],String> f, int m, int n) {
		String[][] p = new String[m][];
		for (int i=0; i<m; i+=1) p[i] = f.apply(schede.get()).split("\n");
		for (int i=0; i<n; i+=1) System.out.println("\n" + merge("\n", " ".repeat(7), p) + "\n");
	}
	
	private static String merge(String s1, String s2, String[] ... ss) {
		if (ss.length < 2) throw new IllegalArgumentException();
		int length = ss[0].length;
		for (int i=1; i<ss.length; i+=1) if (ss[i].length != length) throw new IllegalArgumentException(); 
		String[] r = new String[length];
		for (int i=0; i<length; i+=1) {
			String s = ""; for (int j=0; j<ss.length; j+=1) s += (s=="" ? "" : s2) + ss[j][i]; r[i] = s;
		}		
		return String.join(s1, r);
	}

	public String boxed() {
		return boxed(get());
	}
	public String boxed(int r, int c) {
		return boxed(get(), r, c);
	}
	public String compact() {
		return compact(get());
	}
	
	public int[][][] get() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			sub(z, t[i] = clone(ge(zmx, 3) ? random() : random(zmx, zmn)));
		}
		//System.out.println(toString(z));
		if (!number) return t;
		var ns = numbers();
		for (int[][] s: t) {
			for (int j=0, ej=s[0].length; j<ej; j+=1) {
				List<Integer> n = numbers(ns[j], s, j);
				for (int i=0, ei=s.length; i<ei; i+=1) {
					if (s[i][j] == 0) continue;
					s[i][j] = n.remove(0);  
				}
			}
		}
		//System.out.println(System.currentTimeMillis()-tm);
		return t;
	}
	
	private int[][] clone(int[][] m) {
		return stream(m).map(int[]::clone).toArray(int[][]::new);
	}

	private List<Integer>[] numbers() {
		if (numbers == null || !clone) {
			int[] z = {9,10,10,10,10,10,10,10,11};
			numbers = new List[z.length];
			for (int d=0, i=0; i<z.length; d+=10, i+=1) {
				int zi = z[i];
				List<Integer> l = new ArrayList<>(zi);
				for (int j=0; j<zi; j+=1) l.add(d+j+(d==0?1:0));
				if (shuffle) shuffle(l);
				numbers[i] = l;
			}
			if (!clone) return numbers;
		}
		List<Integer>[] clone = new List[numbers.length];
		for (int i=0; i<numbers.length; i+=1) clone[i] = new ArrayList(numbers[i]);
		return clone;
	}

	private List<Integer> numbers(List<Integer> nsj, int[][] s, int j) {
		if (!shuffle) return nsj;
		var n = new ArrayList();
		for (int i=0, e=s.length; i<e; i+=1) if (s[i][j] != 0) n.add(nsj.remove(0));
		if (order) n.sort(naturalOrder());
		return n;
	}
	
	private int[] sub(int[] z, int n) {
		z = z.clone(); for (int i=0; i<z.length; i+=1) z[i] -= n;
		return z;
	}
	private void sub(int[] z, int[][] m) {
		for (int i=0; i<z.length; i+=1) for (int j=0; j<m.length; j+=1) z[i] -= m[j][i];
	}
	
	private boolean ge(int[] v, int n) {
		for (int i=0; i<v.length; i+=1) if (v[i] < n) return false;
		return true;
	}
	private boolean ge(int[] v, int[] v2) {
		for (int i=0; i<v.length; i+=1) if (v[i] < v2[i]) return false;
		return true;
	}

	public static String compact(int[][][] t) {
		return stream(t).map(s-> compact(s)).collect(joining("\n\n"));
	}	
	private static String compact(int[][] s) {
		return stream(s).map(r-> compact(r)).collect(joining("\n"));
	}	
	private static String compact(int[] r) {
		return stream(r).mapToObj(i-> !number ? ""+i : i==0 ? "  " : i<10 ? " "+i : ""+i).collect(joining(!number ? "," : "�"));
	}
	
	public static String boxed(int[][][] t) {
		return stream(t).map(s-> boxed(s)).collect(joining("\n"));
	}
	public static String boxed(int[][][] t, int r, int c) {
		if (r * c != t.length) throw new IllegalArgumentException();
		String s = "";
		for (int z=0, i=0; i<r; i+=1) {
			String[][] p = new String[c][];
			for (int j=0; j<c; j+=1) p[j]= boxed(t[z++]).split("\n");
			s += "\n" + merge("\n", " ".repeat(7), p) + "\n";
		}	
		return s;
	}
	private static String boxed(int[][] s) {
		String r = "��������������������������Ŀ\n";
		for (int i=0; true; i+=1) {
			r += "�" + compact(s[i]) + "�\n";
			if (i==2) break;
			r+= "��������������������������Ĵ\n";
		}
		r += "����������������������������\n";
		return r;
	}
	
	private int random(int max) {
		return (int)(max * Math.random());
	}
	private int[][] random() {
		return all[random(all.length)];
	}
	/*
	private int[][] random(int[] mx, int[] mn) {
		Sum[] fsum = sum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> sum.get(k).length).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return sum.get(fsum[i])[idx];
		}
		throw new RuntimeException();
	}
	*/
	private int[][] random(int[] mx, int[] mn) {
		Entry<Key,int[][][]>[] fsum = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).toArray(Entry[]::new);
		int idx = random(stream(fsum).mapToInt(e->e.getValue().length).sum());
		for (var e: fsum) {
			int length = e.getValue().length;
			if (idx < length) return sum.get(e.getKey())[idx];
			idx -= length;
		}
		throw new RuntimeException();
	}
	/*
	private int[][] random3(int[] mx, int[] mn) {
		record SumLength (Sum sum, int length) {}
		SumLength[] suml = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).map(e->new SumLength(e.getKey(),e.getValue().length)).toArray(SumLength[]::new);
		int idx = random(stream(suml).mapToInt(s->s.length).sum());
		for (var e: suml) {
			if (idx < e.length) return sum.get(e.sum)[idx];
			idx -= e.length;
		}
		throw new RuntimeException();
	}
	private int[][] random4(int[] mx, int[] mn) {
		record SumLength (Sum sum, int length) {
			public SumLength(Entry<Sum,int[][][]> e) { this(e.getKey(), e.getValue().length); }
		}
		SumLength[] suml = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).map(SumLength::new).toArray(SumLength[]::new);
		int idx = random(stream(suml).mapToInt(s->s.length).sum());
		for (var e: suml) {
			if (idx < e.length) return sum.get(e.sum)[idx];
			idx -= e.length;
		}
		throw new RuntimeException();
	}
	private int[][] random5(int[] mx, int[] mn) {
		Map<Sum,Integer> fsum = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).collect(Collectors.toMap(e->e.getKey(), e->e.getValue().length));
		int idx = random(fsum.values().stream().mapToInt(i->i).sum());
		for (var e: fsum.entrySet()) {
			if (idx < e.getValue()) return sum.get(e.getKey())[idx];
			idx -= e.getValue();
		}
		throw new RuntimeException();
	}
	*/
	
	@SuppressWarnings("unused")
	private static void write(String fn) throws Exception {
		ProcessBuilder pb = schede();
		pb.redirectOutput(new File(fn));
		pb.start().waitFor();
	}

	private static ProcessBuilder schede() {
		ProcessBuilder pb = new ProcessBuilder("node", "Schede.js", "writeAll( M(3, 5, [3, 3, 3, 3, 3, 3, 3, 3, 3]) )");
		var env = pb.environment();
		env.put("NODE_DISABLE_COLORS", "1");
		env.put("NODE_SKIP_PLATFORM_CHECK", "1");
		String nodeDir = "D:\\Programmi\\Node-v13.14.0-win-x64";
		env.put("Path", nodeDir + ";" + env.get("Path")); 
		env.put("NODE_PATH", nodeDir + "\\node_module");
		//env.forEach((k,v)->System.out.println(k + "=" + v));
		pb.redirectError(ProcessBuilder.Redirect.INHERIT);
		pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
		return pb;
	}
	
	@SuppressWarnings("unused")
	private static void read(String fn) throws Exception {
		try (
			var br = new BufferedReader(fn != null ? new FileReader(fn) : new InputStreamReader(schede().start().getInputStream()))
		) {
			var all = new ArrayList<int[][]>();
			Map<Key, int[]> row = new TreeMap<>();
			//Map<Key, int[][]> card = new TreeMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				int[][] m = new int[3][];
				for (int i=0; i<m.length; i+=1) {
					//m[i] = stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray();
					m[i] = row.computeIfAbsent(new Key(stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray()), k->k.a);
				}
				all.add(m);
				//all.add(card.computeIfAbsent(new Key(stream(m).flatMapToInt(Arrays::stream).toArray()), k->m));
			}
			Schede.all = all.toArray(int[][][]::new);
			all.stream().collect(groupingBy(Key::new)).forEach((k,v)-> sum.put(k, v.toArray(int[][][]::new)));
		}
	}
	
	private static void init() {
		row = new TreeMap<>();
		var all = M(3, 5, new int[] {3,3,3,3,3,3,3,3,3});
		Schede.all = all.toArray(int[][][]::new);
		all.stream().collect(groupingBy(Key::new)).forEach((k,v)-> sum.put(k, v.toArray(int[][][]::new)));
		row = null;
	}
	
	private static List<int[][]> M(int nr, int tr, int[] tc) {
		// nr: numero righe per scheda
		// tr: totale per riga 
		// tc[]: totali per colonna
		return M(nr, tr, tc, new ArrayList<>());
	}
	private static List<int[][]> M(int nr, int tr, int[] tc, List<int[]> m) {
		if (m.size() == nr)
			return list(m.stream().toArray(int[][]::new));
		else {
			List<int[][]> list = new ArrayList<>();
			for (int[] r: R(tr, tc, m.size()<nr-1 ? null : gt0(m))) {
				list.addAll( M(nr, tr, sub(tc, r), add(m, r)) );
			}
			return list;
		}
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
	private static boolean[] gt0(List<int[]> m) { // colonne di m maggiori di zero
		var r = new boolean[m.get(0).length];
		for (var v: m) for (var i=0; i<v.length; i+=1) r[i] |= v[i] > 0;
		return r;
	}
	private static int[] sub(int[] v, int[] v2) {
		v = v.clone(); for (int i=0; i<v.length; i+=1) v[i] -= v2[i]; return v;
	}
	
	private static List<int[]> R(int tr, int[] tc, boolean[] gt0) {
		// tr: totale per riga
		// tc[]: totali per colonna
		// gt0[]: colonne maggiori di zero
		return R(0, tr, tc, gt0, new ArrayList<Integer>());
	}
	private static List<int[]> R(int i, int tr, int[] tc, boolean[] gt0, List<Integer> r) {
		if (i == tc.length) { // i ~ r.length
			//return list(r.stream().mapToInt(Integer::intValue).toArray());
			int[] m = r.stream().mapToInt(Integer::intValue).toArray();
			return list(row.computeIfAbsent(new Key(m), k->m));
		}
		else {
			List<int[]> list = new ArrayList<>();
			for (int n=toint(tr>=tc.length-i || gt0!=null && !gt0[i]), e=toint(tr!=0 && tc[i]!=0); n<=e; n+=1) {
				list.addAll( R(i+1, tr-n, tc, gt0, add(r, n)) );
			}
			return list;
		}
	}
	private static int toint(boolean b) {
		return b ? 1 : 0;
	}
	
	static class Key implements Comparable<Key>, Serializable {
		private static final long serialVersionUID = 1L;
		private int[] a;
		
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
			return Schede.compact(a);
		}
	}	
}
