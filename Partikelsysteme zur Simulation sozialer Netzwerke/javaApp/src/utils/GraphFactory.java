package utils;

import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import graph.Link;
import graph.Node;

public class GraphFactory {
	public static UndirectedSparseGraph<Node, Link> createGraph(
			final JSONObject jsonData) {
		UndirectedSparseGraph<Node, Link> graph = new UndirectedSparseGraph<Node, Link>();

		JSONArray jsonNodes = (JSONArray) jsonData.get("nodes");
		JSONArray jsonLinks = (JSONArray) jsonData.get("links");

		Hashtable<Integer, Node> tempNodes = new Hashtable<>();

		for (int i = 0; i < jsonNodes.size(); i++) {
			JSONObject node = (JSONObject) jsonNodes.get(i);
			String name = (String) node.get("name");
			Node n = new Node(i, name);
			graph.addVertex(n);
			tempNodes.put(i, n);
		}

		for (int i = 0; i < jsonLinks.size(); i++) {
			JSONObject link = (JSONObject) jsonLinks.get(i);
			int weight = Integer.parseInt(String.valueOf((long) (link
					.get("value"))));
			int capacity = 1;
			int iNode0 = Integer.parseInt(String.valueOf((long) (link
					.get("source"))));
			int iNode = Integer.parseInt(String.valueOf((long) (link
					.get("target"))));
			graph.addEdge(new Link(i, weight, capacity), new Pair<Node>(
					tempNodes.get(iNode0), tempNodes.get(iNode)));
		}
		return graph;
	}

	public static UndirectedSparseGraph<Node, Link> createTestGraph() {
		UndirectedSparseGraph<Node, Link> graph = new UndirectedSparseGraph<Node, Link>();
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();
		Node n6 = new Node();

		graph.addEdge(new Link(1), new Pair<Node>(n1, n2));
		graph.addEdge(new Link(2), new Pair<Node>(n1, n3));
		graph.addEdge(new Link(3), new Pair<Node>(n1, n4));
		graph.addEdge(new Link(4), new Pair<Node>(n2, n4));
		graph.addEdge(new Link(5), new Pair<Node>(n3, n4));
		graph.addEdge(new Link(6), new Pair<Node>(n4, n5));
		graph.addEdge(new Link(7), new Pair<Node>(n5, n6));
		graph.addEdge(new Link(8), new Pair<Node>(n3, n6));

		// TODO weird things happen with fruchterman when you add these...
		// expected or bug?
		Node n7 = new Node();
		Node n8 = new Node();
		graph.addEdge(new Link(9), new Pair<Node>(n7, n8));
		return graph;
	}

	public static UndirectedSparseGraph<Node, Link> createSmallGraph() {
		UndirectedSparseGraph<Node, Link> graph = new UndirectedSparseGraph<Node, Link>();
		Node n1 = new Node();
		Node n2 = new Node();
		graph.addEdge(new Link(1), new Pair<Node>(n1, n2));
		return graph;
	}
	
	public static UndirectedSparseGraph<Node, Link> createStarGraph() {
		UndirectedSparseGraph<Node, Link> graph = new UndirectedSparseGraph<Node, Link>();
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();
		Node n6 = new Node();

		graph.addEdge(new Link(1), new Pair<Node>(n1, n2));
		graph.addEdge(new Link(2), new Pair<Node>(n1, n3));
		graph.addEdge(new Link(3), new Pair<Node>(n1, n4));
		graph.addEdge(new Link(4), new Pair<Node>(n1, n5));
		graph.addEdge(new Link(5), new Pair<Node>(n1, n6));
		return graph;
	}
}
