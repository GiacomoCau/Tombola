import static java.util.Arrays.stream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Schede {
	public static void main(String[] args) throws Exception {
		List<int[][]> all = new ArrayList(/*735_210*/);
		Map<Sum,List<int[][]>> sum = new TreeMap<>();
		read(all, sum);
		//System.out.println("all: " + all.size());
		//System.out.println("parz: " + tpe.size());
		//parz.forEach((k,l)-> System.out.println(k + ": " + l.size()));
		int[] z = {9,10,10,10,10,10,10,10,11};
		int[][][] t = table(all, sum, z /*new int[] {* /9,10,10,10,10,10,10,10,11/*}*/);
		System.out.println(toString(t));		  	
		System.out.println("finito!");
	}

	private static int[][][] table(List<int[][]> all, Map<Sum, List<int[][]>> sum, int ... z) {
		int[][][] t = new int[6][][];
		for (int s=t.length-1, i=0; i<t.length; s-=1, i+=1) {
			int[] zmx = sub(z, s), zmn = sub(z, s*3);
			//System.out.println((i+1)+"\n"+ toString(z));
			/*z =*/ sub(z, t[i] = ge(zmx, 3) ? random(all) : random(sum, zmx, zmn));
			//System.out.println(toString(zmx));
			//System.out.println(toString(zmn));
			//System.out.println(toString(t[i]));
			//System.out.println(toString(sum(t[i])));
			//System.out.println();
		}
		//System.out.println(toString(z)+"\n");
		return t;
	}

	private static int[] sum(int[][] m) {
		int[] s = new int[m[0].length]; for (int i=0; i<s.length; i+=1) for (int j=0; j<m.length; j+=1) s[i] += m[j][i];
		return s;
	}
	
	private static int[] sub(int[] z, int n) {
		int[] s = Arrays.copyOf(z, z.length); for (int i=0; i<s.length; i+=1) s[i] -= n;
		return s;
	}
	/*
	private static int[] sub(int[] z, int[][] m) {
		int[] s = copyOf(z); for (int i=0; i<9; i+=1) for (int j=0; j<3; j+=1) s[i] -= m[j][i];
		return s;
	}*/
	private static void /*int[]*/ sub(int[] z, int[][] m) {
		for (int i=0; i<z.length; i+=1) for (int j=0; j<m.length; j+=1) z[i] -= m[j][i];
		//return z;
	}
	
	/*
	private static int[] copyOf(int[] v) {
		return Arrays.copyOf(v, v.length);
	}
	*/
	
	private static boolean ge(int[] v, int n) {
		for (int i=0; i<v.length; i+=1) if (v[i] < n) return false;
		return true;
	}
	private static boolean ge(int[] v, int[] v2) {
		for (int i=0; i<v.length; i+=1) if (v[i] < v2[i]) return false;
		return true;
	}

	private static String toString(int[][][] t) {
		return String.join("\n\n", stream(t).map(m-> toString(m)).toArray(String[]::new));
	}	
	private static String toString(int[][] s) {
		return String.join("\n", stream(s).map(v-> toString(v)).toArray(String[]::new));
	}	
	private static String toString(int[] s) {
		return String.join(",", stream(s).mapToObj(i-> i+"").toArray(String[]::new));
	}	
	
	private static int random(int max) {
		return (int)(Math.random() * (max-1));
	}
	private static int[][] random(List<int[][]> l) {
		//System.out.println("all");
		return l.get(random(l.size()));
	}
	private static int[][] random(Map<Sum,List<int[][]>> sum, int[] mx, int[] mn) {
		//System.out.println("parz");
		Sum[] fsum = sum.keySet().stream().filter(k-> ge(mx, k.a) && ge(k.a, mn)).toArray(Sum[]::new);
		int[] size = stream(fsum).mapToInt(k-> sum.get(k).size()).toArray();
		for (int idx=random(stream(size).sum()), i=0; i<size.length; idx-=size[i], i+=1) {
			if (idx < size[i]) return sum.get(fsum[i]).get(idx);
		}
		throw new RuntimeException();
	}
	
	private static void read(List<int[][]> all, Map<Sum, List<int[][]>> sum) throws Exception {
		//Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		try (
			var br = new BufferedReader(new FileReader("Schede.txt"));
			//Connection connection = DriverManager.getConnection("jdbc:derby:SchedeDb;create=true");
			//Statement statement = connection.createStatement();
		) {
			br.readLine();
			for (String line; (line = br.readLine()) != null; ) {
				//System.out.println(line);
				if (!line.matches("\\d+\\)")) continue;
				int id = Integer.parseInt(line.substring(0,line.indexOf(")")));
				//System.out.println(id);
				int[][] m = new int[3][];
				for (int i=0; i<3; i+=1) m[i] = stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray();
				all.add(m);
				//parz.putIfAbsent(toKey(s), new ArrayList()).add(m);
				sum.computeIfAbsent(new Sum(m), k->new ArrayList()).add(m);
				//System.out.println(toKey(s));
				//Arrays.stream(m).forEach(r->System.out.println(toKey(r)));
				br.readLine();
			}
		}
		/*
		try (
			var oos = new ObjectOutputStream(new FileOutputStream("Schede.obj"));
		) {
			oos.writeObject(all);
			oos.writeObject(sum);
		}
		try (
			var ois = new ObjectInputStream(new FileInputStream("Schede.obj"));
		) {
			all = (List<int[][]>) ois.readObject();
			sum = (Map<Schede.Sum, List<int[][]>>) ois.readObject();
		}
		*/
	}
	
	static class Sum implements Serializable, Comparable<Sum> {
		private static final long serialVersionUID = 1L;
		private final int[] a;
		
		Sum(int[] ts) {	a = ts; }
		Sum(int[][] ts) { a = sum(ts); }
		
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
