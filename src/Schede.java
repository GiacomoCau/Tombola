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
		
	private static int[][][] schede;
	private static Map<Key,int[][][]> schedeBySum = new TreeMap<>();
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
			//schedeBySum.forEach((k,v)-> System.out.println(k));
			//System.out.println(schedeBySum.size() + " " + size(schedeBySum)); System.out.println();
			//System.out.println(size(row, schedeBySum)); System.out.println();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		
		//long tm = System.currentTimeMillis();
		//for (int i=0; i<100000; i+=1) schede.getFoglio();
		//System.out.println(System.currentTimeMillis()-tm);
		
		//System.out.println("\n");
		//System.out.println(schede.compact());
		//System.out.println(schede.boxed());
		//System.out.println(schede.boxed(3, 2));
		//for (int i=0; i<4; i+=1) System.out.println((i==0 ? "" : "\n\n") + schede.boxed(2, 3));
		
		//printFogli(2, 3, Schede::compact, fmt(2, 7));
		//printFogli(3, 2, f-> boxed(2, 3, f, fmt(1, 5)), fmt(2, 7));
		printFogli(12, f-> boxed(3, 2, f), fmt(2));
		//printSchede(3, 6, Schede::compact);
		
		//printFogli(8, f-> boxed(2, 3, f), fmt(1, 4, 0)); // word portrait normal consolas 9
		//printFogli(8, f-> boxed(3, 2, f), fmt(1)); // word landscape narrow consolas 19 
		
		System.out.println("\nfinito!");
	}
	
	/*
	public static class Fmt {
		int pb; String os, vs, ps;
		public Fmt(int vs) {
			this.vs = "\n".repeat(vs);
		}
		public Fmt(int vs, int os) {
			this(vs);
			this.os = " ".repeat(os);
		}
		public Fmt(int vs, int pb, int ps) {
			this(vs, 0, pb, ps);
		}
		public Fmt(int vs, int os, int pb, int ps) {
			this(vs, os);
			this.pb = pb;
			this.ps = "\n".repeat(ps);
		}
		public String vs(int i) {
			return i==0 ? "" : pb>0 && i%pb==0 ? ps : vs;
		}
	}
	*/
	public record Fmt(String vs, String os, int pb, String ps) {
		public String vs(int i) {
			return i==0 ? "" : pb>0 && i%pb==0 ? ps : vs;
		}		
	};
	public static Fmt fmt(int vs) { return fmt(vs, 0, 0, 0); }; 
	public static Fmt fmt(int vs, int os) { return fmt(vs, os, 0, 0); }; 
	public static Fmt fmt(int vs, int pb, int ps) { return fmt(vs, 0, pb, ps); }; 
	public static Fmt fmt(int vs, int os, int pb, int ps) { return new Fmt("\n".repeat(vs), " ".repeat(os), pb, "\n".repeat(ps)); }; 


	public static void printSchede(int n, Function<int[][],String> fn) {
		printSchede(n, fn, fmt(1));
	}
	public static void printSchede(int n, Function<int[][],String> fn, Fmt ft) {
		Schede schede = new Schede();
		int[][][] f = schede.getFoglio();
		for (int z=0, i=0; i<n; i+=1) {
			System.out.println(ft.vs(i) + fn.apply(f[z++]));
			if (z < f.length) continue;
			f=schede.getFoglio(); z=0;			
		}
	}
	
	public static void printSchede(int r, int c, Function<int[][],String> fn) {
		printSchede(r, c, fn, fmt(1, 7));
	}
	public static void printSchede(int r, int c, Function<int[][],String> fn, Fmt ft) {
		if (c == 1) {
			printSchede(r, fn, ft);
			return;
		}	
		Schede schede = new Schede();
		int[][][] f = schede.getFoglio();
		for (int z=0, i=0; i<r; i+=1) {
			String[][] p = new String[c][];
			for (int j=0; j<c; j+=1) {
				p[j] = fn.apply(f[z++]).split("\n");
				if (z < f.length) continue;
				f=schede.getFoglio(); z=0;
			}
			System.out.println(ft.vs(i) + merge(ft.os, p));
		}	
	}
	
	public static void printFogli(int n, Function<int[][][],String> fn) {
		printFogli(n, fn, fmt(2));
	}
	public static void printFogli(int n, Function<int[][][],String> fn, Fmt ft) {
		Schede schede = new Schede();
		for (int i=0; i<n; i+=1) System.out.println(ft.vs(i) + fn.apply(schede.getFoglio()));
	}
	
	public static void printFogli(int m, int n, Function<int[][][],String> fn) {
		printFogli(m, n, fn, fmt(1, 7));
	}
	public static void printFogli(int m, int n, Function<int[][][],String> fn, Fmt ft) {
		if (n == 1) { 
			printFogli(m, fn, ft);
			return;
		}
		Schede schede = new Schede();
		String[][] p = new String[n][];
		for (int i=0; i<n; i+=1) p[i] = fn.apply(schede.getFoglio()).split("\n");
		for (int i=0; i<m; i+=1) System.out.println(ft.vs(i) + merge(ft.os, p));
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
		return compact(getFoglio());
	}
	
	public String boxed() {
		return boxed(getFoglio());
	}
	public String boxed(int r, int c) {
		return boxed(r, c, getFoglio());
	}
	
	public int[][][] getFoglio() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] f = new int[6][][];
		for (int s=f.length-1, i=0; i<f.length; s-=1, i+=1) {
			int[] zmn = sub(z, s*3), zmx = sub(z, s);
			z = sub(z, f[i] = clone(ge(zmx, 3) ? random() : random(zmn, zmx)));
		}
		if (!number) return f;
		var ns = numbers();
		for (int[][] s: f) {
			for (int j=0, ej=s[0].length; j<ej; j+=1) {
				List<Integer> n = numbers(ns[j], s, j);
				for (int i=0, ei=s.length; i<ei; i+=1) {
					if (s[i][j] == 0) continue;
					s[i][j] = n.remove(0);  
				}
			}
		}
		return f;
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
	
	public static String compact(int[][][] f) {
		return stream(f).map(s-> compact(s)).collect(joining("\n\n"));
	}	
	public static String compact(int[][] s) {
		return stream(s).map(r-> compact(r)).collect(joining("\n"));
	}	
	public static String compact(int[] r) {
		return stream(r).mapToObj(i-> !number ? ""+i :  i==0 ? "  " : "%2d".formatted(i)).collect(joining(!number ? "," : "|"));
	}
	
	public static String boxed(int r, int c, int[][][] f) {
		return boxed(r, c, f, fmt(2, 7)); 
	}
	public static String boxed(int r, int c, int[][][] f, Fmt ft) {
		if (r * c != f.length) throw new IllegalArgumentException();
		if (c == 1) return boxed(f, ft);
		String s = "";
		for (int z=0, i=0; i<r; i+=1) {
			String[][] p = new String[c][];
			for (int j=0; j<c; j+=1) p[j]= boxed(f[z++]).split("\n");
			s += ft.vs(i) + merge(ft.os, p);
		}	
		return s;
	}
	
	public static String boxed(int[][][] f) {
		return boxed(f, fmt(1));
	}
	public static String boxed(int[][][] f, Fmt ft) {
		return stream(f).map(s-> boxed(s)).collect(joining(ft.vs));
	}
	
	public static String boxed(int[][] s) {
		String r = "", l = "─".repeat(2); 
		r += "┌"+ (l + "┬").repeat(8) + l + "┐\n";
		for (int i=0; true; i+=1) {
			r += "│" + boxed(s[i]) + "│\n";
			if (i==2) break;
			r+= "├" + (l + "┼").repeat(8) + l + "┤\n";
		}
		r += "└" + (l + "┴").repeat(8) + l + "┘\n";
		return r;
	}
	public static String boxed(int[] r) {
		return stream(r).mapToObj(i-> i==0 ? "  " : "%2d".formatted(i)).collect(joining("│"));
	}
	
	private int random(int max) {
		return (int)(max * Math.random());
	}
	private int[][] random() {
		return schede[random(schede.length)];
	}
	/*
	private int[][] random0(int[] mx, int[] mn) {
		Sum[] fsum = schedeBySum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> schedeBySum.get(k).length).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return schedeBySum.get(fsum[i])[idx];
		}
		throw new RuntimeException();
	}
	*/
	private int[][] random(int[] mn, int[] mx) {
		Entry<Key,int[][][]>[] fsum = schedeBySum.entrySet().stream().filter(e-> ge(e.getKey().a, mn) && ge(mx, e.getKey().a)).toArray(Entry[]::new);
		int idx = random(stream(fsum).mapToInt(e->e.getValue().length).sum());
		for (var e: fsum) {
			int length = e.getValue().length;
			if (idx < length) return schedeBySum.get(e.getKey())[idx];
			idx -= length;
		}
		throw new RuntimeException();
	}
	/*
	private int[][] random3(int[] mx, int[] mn) {
		record SumLength (Key sum, int length) {}
		SumLength[] suml = schedeBySum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).map(e->new SumLength(e.getKey(),e.getValue().length)).toArray(SumLength[]::new);
		int idx = random(stream(suml).mapToInt(s->s.length).sum());
		for (var e: suml) {
			if (idx < e.length) return schedeBySum.get(e.sum)[idx];
			idx -= e.length;
		}
		throw new RuntimeException();
	}
	private int[][] random4(int[] mx, int[] mn) {
		record SumLength (Key sum, int length) {
			public SumLength(Entry<Key,int[][][]> e) { this(e.getKey(), e.getValue().length); }
		}
		SumLength[] suml = schedeBySum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).map(SumLength::new).toArray(SumLength[]::new);
		int idx = random(stream(suml).mapToInt(s->s.length).sum());
		for (var e: suml) {
			if (idx < e.length) return schedeBySum.get(e.sum)[idx];
			idx -= e.length;
		}
		throw new RuntimeException();
	}
	private int[][] random5(int[] mx, int[] mn) {
		Map<Key,Integer> fsum = schedeBySum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).collect(Collectors.toMap(e->e.getKey(), e->e.getValue().length));
		int idx = random(fsum.values().stream().mapToInt(i->i).sum());
		for (var e: fsum.entrySet()) {
			if (idx < e.getValue()) return schedeBySum.get(e.getKey())[idx];
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
			var schede = new ArrayList<int[][]>();
			row = new TreeMap<>();
			//Map<Key, int[][]> card = new TreeMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				int[][] c = new int[3][];
				for (int i=0; i<c.length; i+=1) {
					c[i] = row.computeIfAbsent(new Key(stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray()), k-> k.a);
				}
				schede.add(c);
				//all.add(card.computeIfAbsent(new Key(stream(c).flatMapToInt(Arrays::stream).toArray()), k->c));
			}
			row = null;
			schede.stream().collect(groupingBy(Key::new)).forEach((k,v)-> schedeBySum.put(k, v.toArray(int[][][]::new)));
			Schede.schede = schede.toArray(int[][][]::new);
		}
	}
	
	private static void init() {
		row = new TreeMap<>();
		var schede = S(3, 5, new int[] {3,3,3,3,3,3,3,3,3});
		row = null;
		schede.stream().collect(groupingBy(Key::new)).forEach((k,v)-> schedeBySum.put(k, v.toArray(int[][][]::new)));
		Schede.schede = schede.toArray(int[][][]::new);
	}
}
