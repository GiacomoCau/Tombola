import static java.lang.Integer.parseInt;
import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
			//out.println(System.currentTimeMillis()-tm);
			
			//out.println(size());
			//row.forEach((k,v)-> out.println(k));
			//out.println(row.size() + " " + size(row)); out.println();
			//schedeBySum.forEach((k,v)-> out.println(k));
			//out.println(schedeBySum.size() + " " + size(schedeBySum)); out.println();
			//out.println(size(row, schedeBySum)); out.println();
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
		//out.println(System.currentTimeMillis()-tm);
		
		//out.println();
		//out.println(schede.compact());
		//out.println(schede.boxed());
		//out.println(schede.boxed(2, 3));
		//out.println(schede.boxed(2, 3, fmt(2, 7)));
		//for (int i=0; i<4; i+=1) System.out.println((i==0 ? "" : "\n\n") + schede.boxed(2, 3));
		
		//printFogli(2, 3, Schede::compact);
		//printFogli(2, 3, Schede::boxed);
		//printFogli(2, 3, Schede::boxed, fmt(2, 7));
		//printFogli(2, 3, f-> boxed(f, 3, 2));
		//printFogli(2, 3, f-> boxed(f, 3, 2, fmt(1, 5)));
		//printFogli(2, 3, f-> boxed(f, 3, 2, fmt(1, 5)), fmt(2, 7));
		//printFogli(12, f-> boxed(f, 3, 2), fmt(2));
		//printSchede(3, 6, Schede::compact);
		//printSchede(3, 6, Schede::boxed);
		//out.println("\n"); printFogli(8, f-> boxed(f, 2, 3, fmt(1, 7))); // ultraedit  2 pagine
		
		//printFogli(8, f-> boxed(f, 2, 3, fmt(1, 7)), fmt(1, 4, 0)); // word portrait normal consolas 9  2 pagine
		//printFogli(8, f-> boxed(f, 3, 2, fmt(1, 7)), fmt(0)); // word landscape narrow consolas 20  8 pagine
		
		//for (var f=fogli(); f.hasNext(); ) out.println(boxed(f.next()));
		//var f=fogli(); while (f.hasNext()) out.println(boxed(f.next()));
		
		//for (var f: iterable(fogli())) out.println(boxed(f));
		//for (var s: iterable(schede())) out.println(boxed(s));
		
		//out.println();
		//out.println(StreamSupport.stream(iterable(numeri()).spliterator(), false).map(i->""+i).collect(Collectors.joining(" "))); 
		//for (var n: (Iterable<Integer>) ()-> numeri()) out.print(n + " "); out.println();
		//for (var n: iterable(numeri())) out.print(n + " "); out.println();
		//numeri().forEachRemaining(n-> out.print(n + " ")); out.println();
		//for (var n: iterable(numeri())) { out.println(n); while (System.in.read() != '\n'); }
		//loop: for (var n: iterable(numeri())) { out.println(n); for (int c; (c = System.in.read()) != '\n';) if (c=='q') break loop; }
		//smorfia();
		
		cli(args);
		
		out.println("\nfinito!");
	}
	
	private static void syntax(String[] args, int i) {
		String line = ""; for (int j=0; j<args.length; j+=1) line += (j==0 ? "" : j!=i ? " " : " |> ") + args[j]; 
		throw new IllegalArgumentException(
				line + "\n" + """
				Syntax:
					smorfia [compact | boxed]
					 schede [-n n [vs [pb ps]]] [-m m [os]] compact | boxed
					  fogli [-n n [vs [pb ps]]] [-m m [os]] compact | boxed [-r r [vs [pb ps]]] [-c c [os]]\
			"""
		);
	}	
	
	private static class Vd {int n=1, vs, pb, ps; Vd(int vs) {this.vs=vs;}} 
	private static class Od {int m=1, os; Od(int os) {this.os=os;}}
	
	private static void cli(String[] args) throws Exception {
		int i=0;
		if (!matches(args, i, "smorfia|schede|fogli")) syntax(args, i);
		String type = args[i++];	
		if (type.equals("smorfia")) {
			Boolean compact = !matches(args, i, "boxed|compact") ? null : args[i++].equals("compact");
			if (args.length > i) syntax(args, i);
			smorfia(compact);
			return;
		}
		
		boolean fogli = type.equals("fogli");
		Vd n = new Vd(fogli ? 2 : 1), r = new Vd(1);
		Od m = new Od(7), c = new Od(3);
		
		i = get(n, args, i, "-n", true);
		i = get(m, args, i, "-m", true);
		if (!matches(args, i, "boxed|compact")) syntax(args, i);
		boolean boxed = args[i++].equals("boxed");
		if (fogli && boxed) { 
			i = get(r, args, i, "-r", true);
			i = get(c, args, i, "-c", true);
		}
		if (args.length > i) syntax(args, i);
		
		if (fogli)
			printFogli(n.n, m.m, boxed ? f-> boxed(f, r.n, c.m, fmt(r.vs, c.os, r.pb, r.ps)) : Schede::compact, fmt(n.vs, m.os, n.pb, n.ps));
		else
			printSchede(n.n, m.m, boxed ? Schede::boxed : Schede::compact, fmt(n.vs, m.os, n.pb, n.ps));
	}

	private static boolean matches(String[] args, int i, String regex) {
		return args.length > i && args[i].matches(regex);
	}
		
	private static int get(Vd vd, String[] args, int i, String regex, boolean opt) {
		if (!matches(args, i, regex)) if (opt) return i; else syntax(args, i);
		i+=1;
		if (!matches(args, i, "\\d+")) syntax(args, i);
		vd.n = parseInt(args[i++]);
		if (matches(args, i, "\\d+")) vd.vs = parseInt(args[i++]);
		if (matches(args, i, "\\d+")) {
			vd.pb = parseInt(args[i++]);
			if (!matches(args, i, "\\d+")) syntax(args, i);
			vd.ps = parseInt(args[i++]);
		}
		return i;
	}
	
	private static int get(Od od, String[] args, int i, String regex, boolean opt) {
		if (!matches(args, i, regex))  if (opt) return i; else syntax(args, i);
		i+=1;
		if (!matches(args, i, "\\d+")) syntax(args, i);
		od.m = parseInt(args[i++]);
		if (matches(args, i, "\\d+")) od.os = parseInt(args[i++]);
		return i;
	}
		
	public static Iterator<int[][][]> fogli() {
		return new Iterator<int[][][]>() {
			private Schede schede = new Schede();
			@Override public boolean hasNext() { return true; }
			@Override public int[][][] next() { return schede.getFoglio(); }
		};
	}

	public static Iterator<int[][]> schede() {
		return new Iterator<int[][]>() {
			private Schede schede = new Schede();			
			@Override public boolean hasNext() { return true; }
			private int i=0, f[][][]=schede.getFoglio();
			@Override public int[][] next() {
				if (i == f.length) { i=0; f=schede.getFoglio(); }
				return f[i++];
			}
		};
	}
	
	public static Iterator<Integer> numeri() {
		return new Iterator<Integer> () {
			List<Integer> numeri = new LinkedList(); {range(1,91).forEach(numeri::add);}
			//List<Integer> numeri = new LinkedList() {{range(1,91).forEach(this::add);}};
			@Override public boolean hasNext() { return numeri.size() > 0; }
			@Override public Integer next() { return numeri.remove((int)(numeri.size() * Math.random())); }
		};
	}
	
	public static <T> Iterable<T> iterable(Iterator<T> iterator) {
		/*
		return new Iterable<T>() {
			@Override public Iterator<T> iterator() { return iterator; }
		};
		*/
		return ()-> iterator;
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
	public static Fmt fmt() { return fmt(0); }; 
	public static Fmt fmt(int vs) { return fmt(vs, 0); }; 
	public static Fmt fmt(int vs, int os) { return fmt(vs, os, 0, 0); }; 
	public static Fmt fmt(int vs, int pb, int ps) { return fmt(vs, 0, pb, ps); }; 
	public static Fmt fmt(int vs, int os, int pb, int ps) { return new Fmt("\n".repeat(vs), " ".repeat(os), pb, "\n".repeat(ps)); }; 


	public static void printSchede(int n, Function<int[][],String> fn) {
		printSchede(n, fn, fmt(1));
	}
	public static void printSchede(int n, Function<int[][],String> fn, Fmt fmt) {
		print(n, schede(), fn, fmt);
	}
	
	public static void printSchede(int n, int m, Function<int[][],String> fn) {
		printSchede(n, m, fn, fmt(1, 7));
	}
	public static void printSchede(int n, int m, Function<int[][],String> fn, Fmt fmt) {
		print(n, m, schede(), fn, fmt);
	}
	
	public static void printFogli(int n, Function<int[][][],String> fn) {
		printFogli(n, fn, fmt(2));
	}
	public static void printFogli(int n, Function<int[][][],String> fn, Fmt fmt) {
		print(n, fogli(), fn, fmt);
	}
	
	public static void printFogli(int n, int m, Function<int[][][],String> fn) {
		printFogli(n, m, fn, fmt(2, 7));
	}
	public static void printFogli(int n, int m, Function<int[][][],String> fn, Fmt fmt) {
		print(n, m, fogli(), fn, fmt);
	}
	
	private static <T> void print(int n, Iterator<T> iterator, Function<T,String> fn, Fmt fmt) {
		for (int i=0; i<n; i+=1) out.println(fmt.vs(i) + fn.apply(iterator.next()));
	}
	private static <T> void print(int n, int m, Iterator<T> iterator, Function<T,String> fn, Fmt fmt) {
		if (m == 1) {
			print(n, iterator, fn, fmt);
			return;
		}	
		for (int i=0; i<n; i+=1) {
			var	args = new String[m][];
			for (int j=0; j<m; j+=1) args[j] = fn.apply(iterator.next()).split("\n");
			out.println(fmt.vs(i) + merge(fmt.os, args));
		}	
	}
	private static String merge(String os, String[] ... args) {
		if (args.length < 2) throw new IllegalArgumentException();
		int len = args[0].length;
		for (int i=1; i<args.length; i+=1) if (args[i].length != len) throw new IllegalArgumentException();
		var r = new String[len];
		for (int i=0; i<len; i+=1) {
			String s = ""; for (int j=0; j<args.length; j+=1) s += (j==0 ? "" : os) + args[j][i]; r[i] = s;
		}		
		return String.join("\n", r);
	}

	public String compact() {
		return compact(getFoglio());
	}
	
	public String boxed() {
		return boxed(fmt(1));
	}
	public String boxed(Fmt fmt) {
		return boxed(getFoglio(), fmt);
	}
	public String boxed(int r, int c) {
		return boxed(r, c, fmt(1, 3));
	}
	public String boxed(int r, int c, Fmt fmt) {
		return boxed(getFoglio(), r, c, fmt);
	}
	
	private int[][][] getFoglio() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		var f = new int[6][][];
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
		return stream(r).mapToObj(i-> !number ? ""+i : format(i==0, i)).collect(joining(!number ? "," : "|"));
	}
	
	public static String boxed(int[][][] f) {
		return boxed(f, fmt(1));
	}
	public static String boxed(int[][][] f, Fmt fmt) {
		return stream(f).map(s-> boxed(s)).collect(joining(fmt.vs));
	}
	
	public static String boxed(int[][][] f, int r, int c) {
		return boxed(f, r, c, fmt(1, 3)); 
	}
	public static String boxed(int[][][] f, int r, int c, Fmt fmt) {
		if (r == 0) r = 1;
		if (c == 0) c = 1;
		if (r * c == 1) return boxed(f, fmt);
		if (r * c != f.length) throw new IllegalArgumentException();
		if (c == 1) return boxed(f, fmt);
		String s = "";
		for (int z=0, i=0; i<r; i+=1) {
			var args = new String[c][];
			for (int j=0; j<c; j+=1) args[j]= boxed(f[z++]).split("\n");
			s += (i==0 ? "" : "\n") + fmt.vs(i) + merge(fmt.os, args);
		}	
		return s;
	}
	
	public static String boxed(int[][] s) {
		String r = ""; 
		r += "┌"+ "──┬".repeat(8) + "──┐\n";
		for (int i=0; true; i+=1) {
			r += "│" + boxed(s[i]) + "│\n";
			if (i==2) break;
			r += "├" + "──┼".repeat(8) + "──┤\n";
		}
		return r += "└" + "──┴".repeat(8) + "──┘\n";
	}
	public static String boxed(int[] r) {
		return stream(r).mapToObj(i-> format(i==0, i)).collect(joining("│"));
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
		var pb = node();
		pb.redirectOutput(new File(fn));
		pb.start().waitFor();
	}

	private static ProcessBuilder node() {
		var pb = new ProcessBuilder("node", "Schede.js", "writeAll( S(3, 5, [3, 3, 3, 3, 3, 3, 3, 3, 3]) )");
		var env = pb.environment();
		env.put("NODE_DISABLE_COLORS", "1");
		env.put("NODE_SKIP_PLATFORM_CHECK", "1");
		var nodeDir = "D:\\Programmi\\Node-v13.14.0-win-x64";
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
			var br = new BufferedReader(fn != null ? new FileReader(fn) : new InputStreamReader(node().start().getInputStream()))
		) {
			var schede = new ArrayList<int[][]>();
			row = new LinkedHashMap<>();
			//Map<Key, int[][]> card = new TreeMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				var c = new int[3][];
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
		row = new LinkedHashMap<>();
		var schede = S(3, 5, new int[] {3,3,3,3,3,3,3,3,3});
		row = null;
		schede.stream().collect(groupingBy(Key::new)).forEach((k,v)-> schedeBySum.put(k, v.toArray(int[][][]::new)));
		Schede.schede = schede.toArray(int[][][]::new);
	}
	
	private static void smorfia(Boolean compact) throws IOException, FileNotFoundException {
		record Numero (String descrizione, String traduzione, String altriSignificati) {}
		var smorfia = new LinkedHashMap<Integer, Numero>(); 
		try (
			var br = new BufferedReader(new FileReader("Smorfia.txt"))
		) {
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+.*")) continue;
				int s = line.indexOf(" – ");
				int numero = parseInt(line.substring(0, s));
				//if (!line.substring(s+3).endsWith(".")) out.println("n"+numero); 
				String nome = line.substring(s+3, line.length()-1);
				line = br.readLine();
				//if (!traduzione.endsWith(".")) out.println("t"+numero); 
				String traduzione = line.substring(0, line.length()-1);
				String altriSignificati = br.readLine();
				//out.printf("%2d|%s|%s\n  |%s\n\n", numero, nome, traduzione, altriSignificati);
				smorfia.put(numero, new Numero(nome, traduzione, altriSignificati));
			}
		}
		out.println("enter: next number, t+enter: numbers table, q+enter: quit\n");
		boolean[] numeri = new boolean[91]; 
		loop: for (var n: iterable(numeri())) {
			numeri[n] = true;
			var numero = smorfia.get(n);
			out.printf("%2d - %s - %s\n", n, numero.descrizione, numero.traduzione);
			for (int c; (c = System.in.read()) != '\n';) {
				if (c=='q') break loop;
				if (c!='t') continue;
				if (compact == null || compact) compact(numeri); else boxed(numeri);
				while (System.in.read() != '\n');
			}
		}
	}

	private static void compact(boolean[] numeri) {
		for (int i=0; i<90; i+=10) {
			for (int j=1; j<=10; j+=1) {
				int n = i+j; out.print((j==1 ? "\t" : "|")  + format(!numeri[n], n));
			}
			out.println();
		}
	}

	private static void boxed(boolean[] numeri) {
		out.println("\t┌"+"──┬".repeat(9)+ "──┐");
		for (int i=0; true; i+=10) {
			for (int j=1; j<=10; j+=1) {
				int n = i+j; out.print((j==1 ? "\t" : "") + "|" + format(!numeri[n], n));
			}
			out.println("|");
			if (i==80) break;
			out.println("\t├" + "──┼".repeat(9)+ "──┤");
		}
		out.println("\t└"+"──┴".repeat(9)+ "──┘");
	}
	
	private static String format(boolean b, int n) {
		return b ? "  " : String.format("%2d", n);
	}
}
