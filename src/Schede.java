import static java.lang.String.join;
import static java.util.Arrays.stream;

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

	public static void main(String[] args) throws Exception {
		var schede = new Schede();
		for (int i=0; i<3; i+=1) System.out.println(toString(schede.get()) + "\n\n");		  	
		System.out.println("finito!");
	}

	private List<int[][]> all = new ArrayList(/*735_210*/);
	private Map<Sum,List<int[][]>> sum = new TreeMap<>();
	
	public Schede() throws Exception {
		read();
	}
	
	public int[][][] get() {
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			sub(z, t[i] = ge(zmx, 3) ? random(all) : random(sum, zmx, zmn));
		}
		return t;
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
		return join("\n\n", stream(t).map(m-> toString(m)).toArray(String[]::new));
	}	
	private static String toString(int[][] s) {
		return join("\n", stream(s).map(v-> toString(v)).toArray(String[]::new));
	}	
	private static String toString(int[] s) {
		return join(",", stream(s).mapToObj(i-> i+"").toArray(String[]::new));
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
			br.readLine();
			for (String line; (line = br.readLine()) != null; ) {
				if (!line.matches("\\d+\\)")) continue;
				int id = Integer.parseInt(line.substring(0,line.indexOf(")")));
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
