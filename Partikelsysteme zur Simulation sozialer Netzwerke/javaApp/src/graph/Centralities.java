package graph;

import java.util.HashMap;


import edu.uci.ics.jung.algorithms.scoring.*;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class contains static methods to retrieve centrality values from a
 * graph.
 * 
 * @author andi
 * 
 */
public class Centralities {
	
	public static HashMap<Node, Double> computeCentrality(
			Graph<Node, Link> graph,String name) {
		
		switch ((String) name) {
		case "Betweenness":
			BetweennessCentrality<Node, Link> resbtc = new BetweennessCentrality<>(
					graph);
			return  normalize(graph,resbtc);
			
		case "Closeness":
			ClosenessCentrality<Node, Link> resClc = new ClosenessCentrality<>(
					graph);
			return  normalize(graph,resClc);
		

		case "Eigenvector":
			
			EigenvectorCentrality<Node, Link> resEigen = new EigenvectorCentrality<>(
					graph);
			return  normalize(graph,resEigen);
		
		
		
		default:
			break;
		}
		return null;
		
		
	
			
			
		
		
		
	}

	private static HashMap<Node, Double> normalize(Graph<Node, Link> graph,VertexScorer<Node, Double> scorer) {
		
		
		
		HashMap<Node, Double> values = new HashMap<Node, Double>();
		double maxScore = 0;

		for (Node v : graph.getVertices()) {
			double score = scorer.getVertexScore(v);
			values.put(v, score);

			if (score > maxScore) {
				maxScore = score;
			}
		}

		// normalize by maximum score, not total number of vertices.
		for (Node v : graph.getVertices()) {
			double normScore = values.get(v) / maxScore;
			values.put(v, normScore);
		}
		return values;
	}
	

	
	
	
	
}
