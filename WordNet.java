import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// An immutable WordNet data type.
public class WordNet {
    private RedBlackBST<String, SET<Integer>> synsetNouns;
    private RedBlackBST<Integer, String> synsetIDs; 
    private ShortestCommonAncestor sca;

    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
	    throw new NullPointerException();
	}
	
	In synsetsFile = new In(synsets);
	In hypernymsFile = new In(hypernyms);

	this.synsetNouns = new RedBlackBST<>();
	this.synsetIDs = new RedBlackBST<>();
	
	while (synsetsFile.hasNextLine()) {
	    String[] splitSynsets = synsetsFile.readLine().split(",");
	    for (String noun : splitSynsets[1].split(" ")) {
		SET<Integer> ID = new SET<Integer>();
		if (synsetNouns.contains(noun)) {
		    ID = synsetNouns.get(noun);
		}
		ID.add(Integer.parseInt(splitSynsets[0]));
		synsetNouns.put(noun, ID);
	    }
	    synsetIDs.put(Integer.parseInt(splitSynsets[0]), splitSynsets[1]);
	}

	Digraph G = new Digraph(synsetIDs.size());
	while (hypernymsFile.hasNextLine()) {
	    String[] splitHypernyms = hypernymsFile.readLine().split(",");
	    int origin = Integer.parseInt(splitHypernyms[0]);
	    for (int i = 1; i < splitHypernyms.length; i++) {
		G.addEdge(origin, Integer.parseInt(splitHypernyms[i]));
	    }
	}

	sca = new ShortestCommonAncestor(G);
    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        return synsetNouns.keys();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
	if (word == null) {
	    throw new NullPointerException();
	}
        return synsetNouns.contains(word);
    }

    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
	if (noun1 == null || noun2 == null) {
	    throw new NullPointerException();
	}
	if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new java.lang.IllegalArgumentException();
        }
	SET<Integer> noun_ID1 = synsetNouns.get(noun1);
	SET<Integer> noun_ID2 = synsetNouns.get(noun2);
	if (sca.ancestor(noun_ID1, noun_ID2) == -1) {
	    return "No Shortest Common Ancestor";
	}
	else {
	    return synsetIDs.get(sca.ancestor(noun_ID1, noun_ID2));
	}
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
	if (noun1 == null || noun2 == null) {
            throw new NullPointerException();
        }
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new java.lang.IllegalArgumentException();
        }
        SET<Integer> noun_ID1 = synsetNouns.get(noun1);
        SET<Integer> noun_ID2 = synsetNouns.get(noun2);
	return sca.length(noun_ID1, noun_ID2);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];        
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                       + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                       + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                       + wordnet.distance(word1, word2));
    }
}
