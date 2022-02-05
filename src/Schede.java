import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
			read();
			//System.out.println(System.currentTimeMillis()-tm);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		//System.out.println(schede.getString());
		for (int i=0; i<1; i+=1) System.out.println(schede.getString() + "\n\n");
		System.out.println("finito!");
	}

	public String getString() {
		return toString(get());
	}
	
	public int[][][] get() {
		//long tm = System.currentTimeMillis();
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			sub(z, t[i] = ge(zmx, 3) ? random(all) : random(sum, zmx, zmn));
		}
		//System.out.println(toString(z));
		if (!number) return t;
		var ns = numbers();
		for (int[][] s: t) {
			for (int j=0,ee=s[0].length; j<ee; j+=1) {
				List<Integer> n = numbers(ns[j], s, j);
				for (int i=0, e=s.length; i<e; i+=1) {
					if (s[i][j] == 0) continue;
					s[i][j] = n.remove(0);  
				}
			}
		}
		//System.out.println(System.currentTimeMillis()-tm);
		return t;
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
	private static int[][] random(Map<Sum,int[][][]> sum, int[] mx, int[] mn) {
		Sum[] fsum = sum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> sum.get(k).length).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return sum.get(fsum[i])[idx];
		}
		throw new RuntimeException();
	}
	
	private static void read() throws Exception {
		try (
			var br = new BufferedReader(new FileReader("Schede.txt"));
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
		}
		
		@Override
		public String toString() {
			return Schede.toString(a);
		}
	}
}
