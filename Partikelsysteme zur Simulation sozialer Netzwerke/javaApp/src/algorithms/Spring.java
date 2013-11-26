package algorithms;

import java.util.Hashtable;

import javax.vecmath.Point2d;

import edu.uci.ics.jung.graph.Graph;
import graph.Link;
import graph.Node;

/**
 * Implementation of the Spring algorithm after
 * http://cs.brown.edu/~rt/gdhandbook/chapters/force-directed.pdf page 385.
 * 
 * @author andi
 * 
 */
public class Spring extends AbstractIterativeAlgorithm {
	private static final double C_1 = 2;
	private static final double C_2 = 100; // TODO make this dynamic like in fruchterman-Reingold var k
	private static final double C_3 = 1;
	private static final double C_4 = 0.5;
	public static final String NAME = "Spring";
	private Graph<Node, Link> graph;

	public Spring(final Graph<Node, Link> graph) {
		this.graph = graph;
	}

	public final void step() {
		Hashtable<Node, Point2d> tempForces = new Hashtable<Node, Point2d>();
		for (Node v : graph.getVertices()) {
			Point2d f = new Point2d(0, 0);
			for (Node u : graph.getVertices()) {
				double dist = v.getPos().distance(u.getPos());
				if (!v.equals(u)) {
					if (graph.isNeighbor(u, v)) {
						Point2d direction = (Point2d) u.getPos().clone();
						direction.sub(v.getPos());
						direction.scale(1.0 / dist);
						direction.scale(C_1 * Math.log(dist / C_2));
						f.add(direction);
					} else {
						Point2d direction = (Point2d) v.getPos().clone();
						direction.sub(u.getPos());
						direction.scale(1.0 / dist);
						direction.scale(C_3 / Math.sqrt(dist));
						f.add(direction);
					}
				}
			}
			tempForces.put(v, f);
		}

		for (Node v : graph.getVertices()) {
			Point2d f = tempForces.get(v);
			f.scale(C_4);
			v.getPos().add(f);
		}
	}
}
