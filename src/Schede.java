import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;

public class Schede extends Core {

	private static boolean number = true;
	private static boolean clone = false;
	private static boolean shuffle = true;
	private static boolean order = true;
		
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
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		//long tm = System.currentTimeMillis();
		//for (int i=0; i<100000; i+=1) schede.get();
		//System.out.println(System.currentTimeMillis()-tm);
		System.out.println("\n");
		//System.out.println(schede.compact());
		//System.out.println(schede.boxed());
		//System.out.println(schede.boxed(2, 3));
		//for (int i=0; i<4; i+=1) System.out.println((i==0 ? "" : "\n\n") + schede.boxed(2, 3));
		//printTavole(2, 7, Schede::compact, 3, 2);
		//printTavole(2, 7, t-> boxed(1, 5, t, 3, 2), 2, 2);
		printTavole(2, t-> boxed(t, 2, 3), 12);
		//printSchede(Schede::boxed, 8, 3);
		
		System.out.println("\nfinito!");
	}

	public static void printSchede(Function<int[][],String> f, int n) {
		printSchede(1, f, n);
	}
	public static void printSchede(int vs, Function<int[][],String> f, int n) {
		printSchede("\n".repeat(vs), f, n);
	}
	public static void printSchede(String vs, Function<int[][],String> f, int n) {
		Schede schede = new Schede();
		int[][][] t = schede.get();
		for (int z=0, i=0; i<n; i+=1) {
			System.out.println((i==0 ? "" : vs) + f.apply(t[z++]));
			if (z < t.length) continue;
			t=schede.get(); z=0;			
		}
	}
	public static void printSchede(Function<int[][],String> f, int r, int c) {
		printSchede(1, 7, f, r, c);
	}
	public static void printSchede(int vs, int os, Function<int[][],String> f, int r, int c) {
		printSchede("\n".repeat(vs), " ".repeat(os), f, r, c);
	}
	public static void printSchede(String vs, String os, Function<int[][],String> f, int r, int c) {
		if (c == 1) {
			printSchede(vs, f, r);
			return;
		}	
		Schede schede = new Schede();
		int[][][] t = schede.get();
		for (int z=0, i=0; i<r; i+=1) {
			String[][] p = new String[c][];
			for (int j=0; j<c; j+=1) {
				p[j] = f.apply(t[z++]).split("\n");
				if (z < t.length) continue;
				t=schede.get(); z=0;
			}
			System.out.println((i==0 ? "" : vs) + merge(os, p));
		}	
	}
	
	public static void printTavole(Function<int[][][],String> f, int n) {
		printTavole(2, f, n);
	}
	public static void printTavole(int vs, Function<int[][][],String> f, int n) {
		printTavole("\n".repeat(vs), f, n);
	}
	public static void printTavole(String vs, Function<int[][][],String> f, int n) {
		Schede schede = new Schede();
		for (int i=0; i<n; i+=1) System.out.println((i==0?"":vs) + f.apply(schede.get()));
	}
	public static void printTavole(Function<int[][][],String> f, int m, int n) {
		printTavole(1, 7, f, m, n);
	}
	public static void printTavole(int vs, int os, Function<int[][][],String> f, int m, int n) {
		printTavole("\n".repeat(vs), " ".repeat(os), f, m, n);
	}
	public static void printTavole(String vs, String os, Function<int[][][],String> f, int m, int n) {
		if (n == 1) { 
			printTavole(vs, f, m);
			return;
		}
		Schede schede = new Schede();
		String[][] p = new String[m][];
		for (int i=0; i<m; i+=1) p[i] = f.apply(schede.get()).split("\n");
		for (int i=0; i<n; i+=1) System.out.println((i==0 ? "" : vs) + merge(os, p));
	}
	
	private static String merge(String os, String[] ... ss) {
		if (ss.length < 2) throw new IllegalArgumentException();
		int len = ss[0].length;
		for (int i=1; i<ss.length; i+=1) if (ss[i].length != len) throw new IllegalArgumentException(); 
		String[] r = new String[len];
		for (int i=0; i<len; i+=1) {
			String s = ""; for (int j=0; j<ss.length; j+=1) s += (s=="" ? "" : os) + ss[j][i]; r[i] = s;
		}		
		return String.join("\n", r);
	}

	public String compact() {
		return compact(get());
	}
	
	public String boxed() {
		return boxed(get());
	}
	public String boxed(int r, int c) {
		return boxed(get(), r, c);
	}
	
	public int[][][] get() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmn = sub(z, s*3), zmx = sub(z, s);
			z = sub(z, t[i] = clone(ge(zmx, 3) ? random() : random(zmn, zmx)));
		}
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
	public static String compact(int[][] s) {
		return stream(s).map(r-> compact(r)).collect(joining("\n"));
	}	
	public static String compact(int[] r) {
		return stream(r).mapToObj(i-> !number ? ""+i :  i==0 ? "  " : "%2d".formatted(i)).collect(joining(!number ? "," : "|"));
	}
	
	public static String boxed(int[][][] t, int r, int c) {
		return boxed(2, 7, t, r, c); 
	}
	public static String boxed(int vs, int os, int[][][] t, int r, int c) {
		return boxed("\n".repeat(vs), " ".repeat(os), t, r, c);
	}
	public static String boxed(String vs, String os, int[][][] t, int r, int c) {
		if (r * c != t.length) throw new IllegalArgumentException();
		if (c == 1) return boxed(vs, t);
		String s = "";
		for (int z=0, i=0; i<r; i+=1) {
			String[][] p = new String[c][];
			for (int j=0; j<c; j+=1) p[j]= boxed(t[z++]).split("\n");
			s += (s.length()==0 ? "" : vs) + merge(os, p);
		}	
		return s;
	}
	public static String boxed(int[][][] t) {
		return stream(t).map(s-> boxed(s)).collect(joining("\n"));
	}
	public static String boxed(String vs, int[][][] t) {
		return stream(t).map(s-> boxed(s)).collect(joining(vs));
	}
	public static String boxed(int[][] s) {
		String r = "", l = "Ä".repeat(2); 
		r += "Ú"+ (l + "Â").repeat(8) + l + "¿\n";
		for (int i=0; true; i+=1) {
			r += "³" + boxed(s[i]) + "³\n";
			if (i==2) break;
			r+= "Ã" + (l + "Å").repeat(8) + l + "´\n";
		}
		r += "À" + (l + "Á").repeat(8) + l + "Ù\n";
		return r;
	}
	public static String boxed(int[] r) {
		return stream(r).mapToObj(i-> i==0 ? "  " : "%2d".formatted(i)).collect(joining("³"));
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
	private int[][] random(int[] mn, int[] mx) {
		Entry<Key,int[][][]>[] fsum = sum.entrySet().stream().filter(e-> ge(e.getKey().a, mn) && ge(mx, e.getKey().a)).toArray(Entry[]::new);
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
		pb.redirectError(INHERIT);
		pb.redirectInput(INHERIT);
		return pb;
	}
	
	@SuppressWarnings("unused")
	private static void read(String fn) throws Exception {
		try (
			var br = new BufferedReader(fn != null ? new FileReader(fn) : new InputStreamReader(schede().start().getInputStream()))
		) {
			var all = new ArrayList<int[][]>();
			row = new TreeMap<>();
			//Map<Key, int[][]> card = new TreeMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				int[][] c = new int[3][];
				for (int i=0; i<c.length; i+=1) {
					c[i] = row.computeIfAbsent(new Key(stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray()), k-> k.a);
				}
				all.add(c);
				//all.add(card.computeIfAbsent(new Key(stream(c).flatMapToInt(Arrays::stream).toArray()), k->c));
			}
			row = null;
			Schede.all = all.toArray(int[][][]::new);
			all.stream().collect(groupingBy(Key::new)).forEach((k,v)-> sum.put(k, v.toArray(int[][][]::new)));
		}
	}
	
	private static void init() {
		row = new TreeMap<>();
		var all = S(3, 5, new int[] {3,3,3,3,3,3,3,3,3});
		row = null;
		Schede.all = all.toArray(int[][][]::new);
		all.stream().collect(groupingBy(Key::new)).forEach((k,v)-> sum.put(k, v.toArray(int[][][]::new)));
	}
}
