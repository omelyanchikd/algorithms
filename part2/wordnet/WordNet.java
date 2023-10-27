/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    private int V;
    private final HashMap<String, SET<Integer>> vertices;
    private final Digraph wordnet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null))
            throw new IllegalArgumentException("Input files cannot be empty");
        V = 0;
        vertices = new HashMap<String, SET<Integer>>();
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String[] line = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] nouns = line[1].split(" ");
            for (String noun : nouns) {
                if (!vertices.containsKey(noun)) vertices.put(noun, new SET<Integer>());
                SET<Integer> vertexIds = vertices.get(noun);
                vertexIds.add(id);
                vertices.put(noun, vertexIds);
            }
            V += 1;
        }
        wordnet = new Digraph(V);
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String[] line = hypernymsIn.readLine().split(",");
            if (line.length > 1) {
                int vertex = Integer.parseInt(line[0]);
                for (int i = 1; i < line.length; i++) {
                    wordnet.addEdge(vertex, Integer.parseInt(line[i]));
                }
            }
        }
        DirectedCycle cycle = new DirectedCycle(wordnet);
        if (cycle.hasCycle()) throw new IllegalArgumentException("Input should be a rooted DAG!");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        List<String> protectedVertices = new ArrayList<String>();
        for (String noun : vertices.keySet()) {
            protectedVertices.add(noun);
        }
        return protectedVertices;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Input word cannot be null!");
        return vertices.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null))
            throw new IllegalArgumentException("Input nouns cannot be null!");
        if ((!isNoun(nounA)) || (!isNoun(nounB))) {
            throw new IllegalArgumentException("Inputs should be known to DAG!");
        }
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(wordnet,
                                                                         vertices.get(nounA));
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(wordnet,
                                                                         vertices.get(nounB));
        int minDistance = Integer.MAX_VALUE;
        for (int v = 0; v < V; v++) {
            if ((pathsA.hasPathTo(v)) && (pathsB.hasPathTo(v))) {
                int ancestralDistance = pathsA.distTo(v) + pathsB.distTo(v);
                if (ancestralDistance < minDistance) minDistance = ancestralDistance;
            }
        }
        return minDistance;

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null))
            throw new IllegalArgumentException("Input nouns cannot be null!");
        if ((!isNoun(nounA)) || (!isNoun(nounB)))
            throw new IllegalArgumentException("Inputs should be known to DAG!");
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(wordnet,
                                                                         vertices.get(nounA));
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(wordnet,
                                                                         vertices.get(nounB));
        int minDistance = Integer.MAX_VALUE;
        String commonAncestor = "";
        for (int v = 0; v < V; v++) {
            if ((pathsA.hasPathTo(v)) && (pathsB.hasPathTo(v))) {
                int ancestralDistance = pathsA.distTo(v) + pathsB.distTo(v);
                if (ancestralDistance < minDistance) {
                    minDistance = ancestralDistance;
                    commonAncestor = getKey(v);
                }
            }
        }
        return commonAncestor;
    }

    // find noun for vertex id
    private String getKey(int v) {
        String key = "";
        for (String noun : vertices.keySet()) {
            if (vertices.get(noun).contains(v)) key = key + " " + noun;
        }
        return key;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        System.out.println(net.isNoun("sun"));
        System.out.println(net.distance("b", "f"));
        System.out.println(net.sap("b", "f"));

    }
}
