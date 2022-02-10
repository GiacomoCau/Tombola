import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Schede {

	private static boolean number = true;
	private static boolean clone = false;
	private static boolean shuffle = true;
	private static boolean order = true;
		
	private static int[][][] all;
	private static Map<Sum,int[][][]> sum = new TreeMap<>();
	private static List<Integer>[] numbers;
	
	static {
		try {
			//long tm = System.currentTimeMillis();
			//write("Schede.txt");
			read("Schede.txt");
			//sum.forEach((k,v)-> System.out.println(k));
			//System.out.println(System.currentTimeMillis()-tm);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		//System.out.println(schede.getString());
		//for (int i=0; i<1000; i+=1) schede.getString();
		for (int i=0; i<1; i+=1) System.out.println(schede.getString() + "\n\n");
		System.out.println("finito!");
	}

	public String getString() {
		return toString(get());
	}
	
	//long tm;
	
	public int[][][] get() {
		//long tm = System.currentTimeMillis();
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			sub(z, t[i] = clone(ge(zmx, 3) ? random(all) : random(sum, zmx, zmn)));
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
		//System.out.println(this.tm +=(System.currentTimeMillis()-tm));
		return t;
	}
	
	private int[][] clone (int[][] m) {
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
		int[] s = Arrays.copyOf(z, z.length); for (int i=0; i<s.length; i+=1) s[i] -= n;
		return s;
	}
	private void sub(int[] z, int[][] m) {
		for (int i=0; i<z.length; i+=1) for (int j=0; j<m.length; j+=1) z[i] -= m[j][i];
	}
	
	private boolean ge(int[] v, int n) {
		for (int i=0; i<v.length; i+=1) if (v[i] < n) return false;
		return true;
	}
	private static boolean ge(int[] v, int[] v2) {
		for (int i=0; i<v.length; i+=1) if (v[i] < v2[i]) return false;
		return true;
	}

	private static String toString(int[][][] t) {
		return stream(t).map(s-> toString(s)).collect(joining("\n\n"));
	}	
	private static String toString(int[][] s) {
		return stream(s).map(r-> toString(r)).collect(joining("\n"));
	}	
	private static String toString(int[] r) {
		return stream(r).mapToObj(i-> !number ? ""+i : i==0 ? "  " : i<10 ? " "+i : ""+i).collect(joining(!number ? "," : "|"));
	}	
	
	private static int random(int max) {
		return (int)(max * Math.random());
	}
	private static int[][] random(int[][][] l) {
		return l[random(l.length)];
	}
	/*
	private static int[][] random(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
		Sum[] fsum = sum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> sum.get(k).length).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return sum.get(fsum[i])[idx];
		}
		throw new RuntimeException();
	}
	*/
	private static int[][] random(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
		Entry<Sum,int[][][]>[] fsum = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).toArray(Entry[]::new);
		int idx = random(stream(fsum).mapToInt(e->e.getValue().length).sum());
		for (var e: fsum) {
			int length = e.getValue().length;
			if (idx < length) return sum.get(e.getKey())[idx];
			idx -= length;
		}
		throw new RuntimeException();
	}
	/*
	private static int[][] random3(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
		record SumLength (Sum sum, int length) {}
		SumLength[] suml = sum.entrySet().stream().filter(e-> ge(mx, e.getKey().a) && ge(e.getKey().a, mn)).map(e->new SumLength(e.getKey(),e.getValue().length)).toArray(SumLength[]::new);
		int idx = random(stream(suml).mapToInt(s->s.length).sum());
		for (var e: suml) {
			if (idx < e.length) return sum.get(e.sum)[idx];
			idx -= e.length;
		}
		throw new RuntimeException();
	}
	private static int[][] random4(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
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
	private static int[][] random5(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
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
		ProcessBuilder pb = new ProcessBuilder("node", "Schede.js", "writeAll( M(3, 5, [ 3, 3, 3, 3, 3, 3, 3, 3, 3]) )");
		var env = pb.environment();
		env.put("NODE_DISABLE_COLORS", "1");
		env.put("NODE_SKIP_PLATFORM_CHECK", "1");
		String nodeDir = "D:\\Programmi\\Node-v13.14.0-win-x64";
		env.put("Path", nodeDir + ";" + env.get("Path")); 
		env.put("NODE_PATH", nodeDir + "\\node_module");
		//env.forEach((k,v)->System.out.println(k + "=" + v));
		pb.redirectError(ProcessBuilder.Redirect.INHERIT);
		pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
		pb.redirectOutput(new File(fn));
		pb.start().waitFor();
	}
	
	private static void read(String fn) throws Exception {
		try (
			var br = new BufferedReader(new FileReader(fn));
		) {
			var a = new ArrayList<int[][]>();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				//int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				int[][] m = new int[3][];
				for (int i=0; i<m.length; i+=1) m[i] = stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray();
				a.add(m);
			}
			all = a.toArray(int[][][]::new);
			a.stream().collect(groupingBy(Sum::new)).forEach((k,v)-> sum.put(k, v.toArray(int[][][]::new)));
		}
	}
	
	static class Sum implements Comparable<Sum> {
		private int[] a;
		
		Sum(int[][] m) {
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
			if (!(other instanceof Sum)) return false;
			return Arrays.equals(a, ((Sum) other).a);
		}
		
		@Override
		public int compareTo(Sum other) {
			if (this == other) return 0;
			return Arrays.compare(a, other.a);
			//if (Arrays.equals(this.a, other.a)) return 0;
			//return ge(this.a, other.a) ? 1 : -1;
		}
		
		@Override
		public String toString() {
			return Schede.toString(a);
		}
	}
}
