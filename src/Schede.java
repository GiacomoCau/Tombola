import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.Comparator.naturalOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Schede implements Serializable{
	private static final long serialVersionUID = 1L;

	private static boolean number = true;
	private static boolean shuffle = true;
	
	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		System.out.println(schede.getString());
		//for (int i=0; i<3; i+=1) System.out.println(schede.getString()) + "\n\n");		  	
		System.out.println("finito!");
	}

	private List<int[][]> all = new ArrayList(/*735_210*/);
	private Map<Sum,List<int[][]>> sum = new TreeMap<>();
	
	public Schede() throws Exception {
		read();
	}
	
	public String getString() {
		return toString(get());
	}
	
	public int[][][] get() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			sub(z, t[i] = ge(zmx, 3) ? random(all) : random(sum, zmx, zmn));
		}
		if (!number) return t;
		var ns = numbers();
		for (int[][] s: t) {
			for (int j=0,ee=s[0].length; j<ee; j+=1) {
				List<Integer> n = numbers(ns.get(j), s, j);
				for (int i=0, e=s.length; i<e; i+=1) {
					if (s[i][j] == 0) continue;
					s[i][j] = n.remove(0);  
				}
			}
		}
		return t;
	}

	private List<List<Integer>> numbers() {
		List<List<Integer>> ls = new ArrayList<>(9);
		int[] z = {9,10,10,10,10,10,10,10,11};
		for (int d=0, i=0; i<z.length; d+=10, i+=1) {
			int n = z[i];
			List<Integer> l = new ArrayList<>(n);
			for (int j=0; j<n; j+=1) l.add(d+j+(d==0?1:0));
			if (shuffle) shuffle(l);
			ls.add(l);
		}
		return ls;
	}

	private List<Integer> numbers(List<Integer> nsj, int[][] s, int j) {
		if (!shuffle) return nsj;
		var n = new ArrayList();
		for (int i=0, e=s.length; i<e; i+=1) if (s[i][j] != 0) n.add(nsj.remove(0));
		n.sort(naturalOrder());
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
		return join("\n\n", stream(t).map(s-> toString(s)).toArray(String[]::new));
	}	
	private static String toString(int[][] s) {
		return join("\n", stream(s).map(r-> toString(r)).toArray(String[]::new));
	}	
	private static String toString(int[] r) {
		if (!number)
			return join(",", stream(r).mapToObj(i-> i+"").toArray(String[]::new));
		else
			return join("|", stream(r).mapToObj(i-> i==0 ? "  " : i<10 ? " "+i : ""+i).toArray(String[]::new));
	}	
	
	private static int random(int max) {
		return (int)(max * Math.random());
	}
	private static int[][] random(List<int[][]> l) {
		return l.get(random(l.size()));
	}
	private static int[][] random(Map<Sum,List<int[][]>> sum, int[] mx, int[] mn) {
		Sum[] fsum = sum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> sum.get(k).size()).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return sum.get(fsum[i]).get(idx);
		}
		throw new RuntimeException();
	}
	
	private void read() throws Exception {
		try (
			var br = new BufferedReader(new FileReader("Schede.txt"));
		) {
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				int id = Integer.parseInt(line.substring(0, line.indexOf(")")));
				int[][] m = new int[3][];
				for (int i=0; i<3; i+=1) m[i] = stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray();
				all.add(m);
				sum.computeIfAbsent(new Sum(m), k-> new ArrayList()).add(m);
				br.readLine();
			}
		}
	}
	
	class Sum implements Serializable, Comparable<Sum> {
		private static final long serialVersionUID = 1L;
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
