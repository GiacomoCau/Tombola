import static java.lang.Integer.parseInt;
import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Tombola extends Core {

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
		var schede = new Tombola();
		
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
		
		//out.println("\n"); printFogli(8, f-> boxed(f, 2, 3, fmt(1, 7))); // ultraedit - 2 pagine	
		//printFogli(8, f-> boxed(f, 2, 3, fmt(1, 7)), fmt(1, 4, 0)); // word portrait normal consolas 9 - 2 pagine
		//printFogli(8, f-> boxed(f, 3, 2, fmt(1, 7)), fmt(0)); // word landscape narrow consolas 20 - 8 pagine
		
		//for (var f=fogli(); f.hasNext(); ) out.println(boxed(f.next()));
		//var f=fogli(); while (f.hasNext()) out.println(boxed(f.next()));
		
		//for (var f: iterable(fogli())) out.println(boxed(f));
		//for (var s: iterable(schede())) out.println(boxed(s));
		
		//out.println();
		//out.println(StreamSupport.stream(iterable(numeri()).spliterator(), false).map(i-> ""+i).collect(Collectors.joining(" "))); 
		//for (var n: (Iterable<Integer>) ()-> numeri()) out.print(n + " "); out.println();
		//for (var n: iterable(numeri())) out.print(n + " "); out.println();
		//numeri().forEachRemaining(n-> out.print(n + " ")); out.println();
		//for (var n: iterable(numeri())) { out.println(n); while (System.in.read() != '\n'); }
		//loop: for (var n: iterable(numeri())) { out.println(n); for (int c; (c = System.in.read()) != '\n';) if (c=='q') break loop; }
		//smorfia();
		
		try {
			//for (;;) cli(args.length > 0 ? args : getLine());
			if (args.length > 0)
				cli(args);
			else {
				var sintassi = syntax + "\n\t   help | h";
				out.println(sintassi);
				loop: for (;;) {
					switch (getLine()) { 
						case "": break loop;
						case "h", "help" : out.println(sintassi); break;
						case String s: cli(s.split(" +"));
					}
				}
			}
		}
		finally {
			out.println("\nfinito!");
		}
	}
	
	private static String getLine() throws IOException {
		out.print("> ");
		String s = ""; for (int c; (c = in.read()) != '\n';) if (c >= 32) s += (char)c;
		//out.println(s);
		return s;
	}
	
	static String syntax = """
		Sintassi:
			smorfia [compact | boxed]
			 schede [-n n [vs [pb ps]]] [-m m [os]] compact | boxed
			  fogli [-n n [vs [pb ps]]] [-m m [os]] compact | boxed [-r r [vs [pb ps]]] [-c c [os]]\
		"""
	;
	
	private static void syntax(String[] args, int i) {
		String line = ""; for (int j=0; j<args.length; j+=1) line += (j==0 ? "" : j!=i ? " " : " |> ") + args[j]; 
		throw new IllegalArgumentException(line + "\n" + syntax.replaceAll("(?m)^", "\t"));
	}	
	
	private static class Vd {int n=1, vs, pb, ps; Vd(int vs) {this.vs=vs;}} 
	private static class Od {int m=1, os; Od(int os) {this.os=os;}}
	
	private static void cli(String[] args) throws Exception {
		//if (args.length == 0 || args.length == 1 && args[0]=="") return;
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
		
		i = set(n, args, i, "-n", true);
		i = set(m, args, i, "-m", true);
		if (!matches(args, i, "boxed|compact")) syntax(args, i);
		boolean boxed = args[i++].equals("boxed");
		if (fogli && boxed) { 
			i = set(r, args, i, "-r", true);
			i = set(c, args, i, "-c", true);
		}
		if (args.length > i) syntax(args, i);
		
		if (fogli)
			printFogli(n.n, m.m, boxed ? f-> boxed(f, r.n, c.m, fmt(r.vs, c.os, r.pb, r.ps)) : Tombola::compact, fmt(n.vs, m.os, n.pb, n.ps));
		else
			printSchede(n.n, m.m, boxed ? Tombola::boxed : Tombola::compact, fmt(n.vs, m.os, n.pb, n.ps));
	}

	private static boolean matches(String[] args, int i, String regex) {
		return args.length > i && args[i].matches(regex);
	}
		
	private static int set(Vd vd, String[] args, int i, String regex, boolean opt) {
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
	
	private static int set(Od od, String[] args, int i, String regex, boolean opt) {
		if (!matches(args, i, regex))  if (opt) return i; else syntax(args, i);
		i+=1;
		if (!matches(args, i, "\\d+")) syntax(args, i);
		od.m = parseInt(args[i++]);
		if (matches(args, i, "\\d+")) od.os = parseInt(args[i++]);
		return i;
	}
		
	public static Iterator<int[][][]> fogli() {
		return new Iterator<int[][][]>() {
			private Tombola schede = new Tombola();
			@Override public boolean hasNext() { return true; }
			@Override public int[][][] next() { return schede.getFoglio(); }
		};
	}

	public static Iterator<int[][]> schede() {
		return new Iterator<int[][]>() {
			private Tombola schede = new Tombola();			
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
			List<Integer> numeri = new LinkedList(range(1,91).boxed().toList());
			@Override public boolean hasNext() { return numeri.size() > 0; }
			@Override public Integer next() { return numeri.remove(random(numeri.size())); }
		};
	}
	
	public static <T> Iterable<T> iterable(Iterator<T> iterator) {
		return ()-> iterator;
	}
	
	public record Fmt(String vs, String os, int pb, String ps) {
		public String vs(int i) { return i == 0 ? "" : pb > 0 && i % pb == 0 ? ps : vs; }		
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
		if (m == 1)
			print(n, iterator, fn, fmt);
		else for (int i=0; i<n; i+=1) {
			out.println(fmt.vs(i) + merge(fmt.os, range(0, m).mapToObj(j-> fn.apply(iterator.next()).split("\n")).toArray(String[][]::new)));
		}
	}
	private static String merge(String os, String[] ... args) {
		if (args.length < 2) throw new IllegalArgumentException();
		int len = args[0].length;
		if (stream(args).skip(1).anyMatch(s-> s.length != len)) throw new IllegalArgumentException();
		return range(0, len).mapToObj(i-> range(0, args.length).mapToObj(j-> args[j][i]).collect(joining(os))).collect(joining("\n"));
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
		var nf = numbers();
		for (var s: f) {
			for (int j=0, ej=s[0].length; j<ej; j+=1) {
				var ns = numbers(nf[j], s, j);
				for (int i=0, ei=s.length; i<ei; i+=1) {
					if (s[i][j] == 0) continue;
					s[i][j] = ns.remove(0);  
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
			numbers = range(0, z.length).mapToObj(i-> new ArrayList(range(0, z[i]).map(j-> (i==0?1:i*10)+j).boxed().toList())).toArray(List[]::new);
			if (shuffle) for (var l: numbers) shuffle(l);
		}
		return !clone ? numbers : stream(numbers).map(ArrayList::new).toArray(List[]::new);
	}
	private List<Integer> numbers(List<Integer> nfj, int[][] s, int j) {
		if (!shuffle) return nfj;
		var ns = new ArrayList();
		for (int i=0, e=s.length; i<e; i+=1) if (s[i][j] != 0) ns.add(nfj.remove(0));
		//range(0, s.length).forEach(i->{ if (s[i][j] != 0) ns.add(nfj.remove(0));});
		//range(0, s.length).filter(i-> s[i][j] != 0).mapToObj(i-> nfj.remove(0)).toList();
		if (order) ns.sort(naturalOrder());
		return ns;
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
		int cc = c;
		return range(0, r)
			.mapToObj(i-> fmt.vs(i) + merge(fmt.os,	range(0, cc).mapToObj(j-> boxed(f[i*cc+j]).split("\n")).toArray(String[][]::new)))
			.collect(joining("\n"));
	}
	
	public static String boxed(int[][] s) {
		String r = ""; 
		r += "┌"+ "──┬".repeat(8) + "──┐\n";
		for (int i=0;; i+=1) {
			r += "│" + boxed(s[i]) + "│\n";
			if (i==2) break;
			r += "├" + "──┼".repeat(8) + "──┤\n";
		}
		return r += "└" + "──┴".repeat(8) + "──┘\n";
	}
	public static String boxed(int[] r) {
		return stream(r).mapToObj(i-> format(i==0, i)).collect(joining("│"));
	}
	
	private static int random(int max) {
		return (int)(Math.random() * max);
	}
	private int[][] random() {
		return schede[random(schede.length)];
	}
	private int[][] random(int[] mn, int[] mx) {
		Entry<Key,int[][][]>[] fsbs = schedeBySum.entrySet().stream().filter(e-> ge(e.getKey().a, mn) && ge(mx, e.getKey().a)).toArray(Entry[]::new);
		int idx = random(stream(fsbs).mapToInt(e-> e.getValue().length).sum());
		for (var e: fsbs) {
			int length = e.getValue().length;
			if (idx < length) return schedeBySum.get(e.getKey())[idx];
			idx -= length;
		}
		throw new RuntimeException();
	}
	
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
		//env.forEach((k,v)-> System.out.println(k + "=" + v));
		pb.redirectError(INHERIT);
		pb.redirectInput(INHERIT);
		return pb;
	}
	
	@SuppressWarnings("unused")
	private static void read(String fn) throws Exception {
		var schede = new ArrayList<int[][]>();
		row = new LinkedHashMap<>();
		try (
			var br = new BufferedReader(fn != null ? new FileReader(fn) : new InputStreamReader(node().start().getInputStream()))
		) {
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = parseInt(line.substring(0, line.indexOf(")")));
				schede.add(range(0, 3).mapToObj(i-> 
						row.computeIfAbsent(new Key(stream(unchecked(()-> br.readLine()).split("")).mapToInt(Integer::parseInt).toArray()), k-> k.a)
					).toArray(int[][]::new)
				);
			}
		}
		row = null;
		schede.stream().collect(groupingBy(Key::new)).forEach((k,v)-> schedeBySum.put(k, v.toArray(int[][][]::new)));
		Tombola.schede = schede.toArray(int[][][]::new);
	}
	
	private static void init() {
		row = new LinkedHashMap<>();
		var schede = S(3, 5, new int[] {3,3,3,3,3,3,3,3,3});
		row = null;
		schede.stream().collect(groupingBy(Key::new)).forEach((k,v)-> schedeBySum.put(k, v.toArray(int[][][]::new)));
		Tombola.schede = schede.toArray(int[][][]::new);
	}
	
	private static void smorfia(Boolean compact) throws Exception {
		record Numero (String descrizione, String traduzione, String altriSignificati) {}
		var smorfia = new LinkedHashMap<Integer, Numero>(); 
		try (
			var br = new BufferedReader(new FileReader("Smorfia.txt", Charset.forName("UTF-8")))
		) {
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+.*")) continue;
				//out.println(line);
				int s = line.indexOf(" – ");
				int numero = parseInt(line.substring(0, s));
				var nome = line.substring(s+3, line.length()-1);
				line = br.readLine();
				var traduzione = line.substring(0, line.length()-1);
				var altriSignificati = br.readLine();
				//out.printf("%2d|%s|%s\n  |%s\n\n", numero, nome, traduzione, altriSignificati);
				smorfia.put(numero, new Numero(nome, traduzione, altriSignificati));
			}
		}
		out.println("enter: nuovo numero, t+enter: tabellone, f+enter: fine\n");
		boolean[] numeri = new boolean[91]; 
		loop: for (var n: iterable(numeri())) {
			numeri[n] = true;
			var numero = smorfia.get(n);
			out.printf("%2d - %s - %s\n", n, numero.descrizione, numero.traduzione);
			//out.write("%2d - %s - %s\n".formatted(n, numero.descrizione, numero.traduzione).getBytes(Charset.forName("CP850")));
			for (int c; (c = in.read()) != '\n';) {
				if (c == 'f') {
					while (System.in.read() != '\n');
					break loop;
				}
				if (c != 't') continue;
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
		for (int i=0;; i+=10) {
			for (int j=1; j<=10; j+=1) {
				int n = i+j; out.print((j==1 ? "\t" : "") + "│" + format(!numeri[n], n));
			}
			out.println("│");
			if (i == 80) break;
			out.println("\t├" + "──┼".repeat(9)+ "──┤");
		}
		out.println("\t└"+"──┴".repeat(9)+ "──┘");
	}
	
	private static String format(boolean b, int n) {
		return b ? "  " : String.format("%2d", n);
	}
	
	private static <T> T unchecked(Callable<T> t) {
		try {
			return t.call();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
