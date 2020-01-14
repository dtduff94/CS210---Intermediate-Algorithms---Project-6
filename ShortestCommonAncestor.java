import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {
    private Digraph G;

    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
	    throw new NullPointerException();
	}
	this.G = new Digraph(G);
    }

    // Length of the shortest ancestral path between v and w.
    public int length(int v, int w) {
	if (v < 0 || w < 0) {
	    throw new java.lang.IndexOutOfBoundsException();
	}
        int ancestor = ancestor(v, w);
	return distFrom(v).get(ancestor) + distFrom(w).get(ancestor);
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0) {
            throw new java.lang.IndexOutOfBoundsException();
	}
        SeparateChainingHashST<Integer, Integer> vDistFrom = distFrom(v);
	SeparateChainingHashST<Integer, Integer> wDistFrom = distFrom(w);
	double shortestDistance = Double.POSITIVE_INFINITY;
	int shortestAncestor = -1;
	for (int x : wDistFrom.keys()) {
	    if (vDistFrom.contains(x)) {
		int newShortestDistance = vDistFrom.get(x) + wDistFrom.get(x);
		if (newShortestDistance < shortestDistance) {
		    shortestDistance = newShortestDistance;
		    shortestAncestor = x;
		}
	    }
	}
	return shortestAncestor;
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
	int[] sap = triad(A, B);
	int v = sap[1];
	int w = sap[2];
	SeparateChainingHashST<Integer, Integer> distv = distFrom(v);
	SeparateChainingHashST<Integer, Integer> distw = distFrom(w);
	int totalDistance = (distv.get(sap[0]) + distw.get(sap[0]));
	return totalDistance;
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        int[] sca = triad(A, B);
	return sca[0];
    }

    // Helper: Return a map of vertices reachable from v and their 
    // respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> Vertices = new SeparateChainingHashST<Integer, Integer>();
	LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
	Vertices.put(v, 0);
	queue.enqueue(v);
	while (!queue.isEmpty()) {
	    int i = queue.dequeue();
	    for (int j : G.adj(i)) {
		if (!Vertices.contains(j)) {
		    Vertices.put(j, Vertices.get(i) + 1);
		    queue.enqueue(j);
		}
	    }
	}
	return Vertices;
    }

    // Helper: Return an array consisting of a shortest common ancestor a 
    // of vertex subsets A and B, and vertex v from A and vertex w from B 
    // such that the path v-a-w is the shortest ancestral path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        double shortestDistance = Double.POSITIVE_INFINITY;
	int shortestAncestor = 0;
	int v = 0;
	int w = 0;
	for (int a : A) {
	    for (int b : B) {
		int newShortestDistance = length(a, b);
		if (newShortestDistance< shortestDistance) {
		    shortestDistance = newShortestDistance;
		    shortestAncestor = ancestor(a, b);
		    v = a;
		    w = b;
		}
	    }
	}
	return new int[]{shortestAncestor, v, w};
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
