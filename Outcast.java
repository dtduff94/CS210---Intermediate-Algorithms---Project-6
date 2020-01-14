import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// An immutable data type for outcast detection.
public class Outcast {
    private WordNet wordnet;

    // Construct an Outcast object given a WordNet object.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // The outcast noun from nouns.
    public String outcast(String[] nouns) {
        int furthestDistance = 0;
	String furthestDistanceNoun = nouns[0];
	for (String noun1 : nouns) {
	    int distance = 0;
	    for (String noun2 : nouns) {
		distance += wordnet.distance(noun1, noun2);
	    }
	    if (distance > furthestDistance) {
		furthestDistance = distance;
		furthestDistanceNoun = noun1;
	    }
	}
	return furthestDistanceNoun;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println("outcast(" + args[t] + ") = "
                           + outcast.outcast(nouns));
        }
    }
}
