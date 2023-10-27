/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class SAP {
    private final int V;
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Input cannot be null!");
        digraph = new Digraph(G);
        V = digraph.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> listV = new ArrayList<Integer>();
        listV.add(v);
        List<Integer> listW = new ArrayList<Integer>();
        listW.add(w);
        return length(listV, listW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> listV = new ArrayList<Integer>();
        listV.add(v);
        List<Integer> listW = new ArrayList<Integer>();
        listW.add(w);
        return ancestor(listV, listW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) throw new IllegalArgumentException("Inputs cannot be null");
        int vLength = 0;
        for (Integer i : v) {
            if ((i == null) || (i < 0) || (i > V - 1))
                throw new IllegalArgumentException("Inputs outside of allowed range");
            vLength++;
        }
        int wLength = 0;
        for (Integer i : w) {
            if ((i == null) || (i < 0) || (i > V - 1))
                throw new IllegalArgumentException("Inputs outside of allowed range");
            wLength++;
        }
        if ((vLength == 0) || (wLength == 0)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            if ((pathsA.hasPathTo(i)) && (pathsB.hasPathTo(i))) {
                int ancestralDistance = pathsA.distTo(i) + pathsB.distTo(i);
                if (ancestralDistance < minDistance) minDistance = ancestralDistance;
            }
        }
        if (minDistance == Integer.MAX_VALUE)
            return -1;
        return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) throw new IllegalArgumentException("Inputs cannot be null");
        int vLength = 0;
        for (Integer i : v) {
            if ((i == null) || (i < 0) || (i > V - 1))
                throw new IllegalArgumentException("Inputs outside of allowed range");
            vLength++;
        }
        int wLength = 0;
        for (Integer i : w) {
            if ((i == null) || (i < 0) || (i > V - 1))
                throw new IllegalArgumentException("Inputs outside of allowed range");
            wLength++;
        }
        if ((vLength == 0) || (wLength == 0)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        int commonAncestor = -1;
        for (int i = 0; i < V; i++) {
            if ((pathsA.hasPathTo(i)) && (pathsB.hasPathTo(i))) {
                int ancestralDistance = pathsA.distTo(i) + pathsB.distTo(i);
                if (ancestralDistance < minDistance) {
                    minDistance = ancestralDistance;
                    commonAncestor = i;
                }
            }
        }
        return commonAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
