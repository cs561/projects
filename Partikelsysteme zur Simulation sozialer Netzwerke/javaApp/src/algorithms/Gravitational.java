package algorithms;

import java.util.Hashtable;

import javax.vecmath.Point2d;

import edu.uci.ics.jung.graph.Graph;
import graph.Link;
import graph.Node;

/**
 * Computes algorithm similar to fruchterman-reingold with gravity. After
 * http://arxiv.org/pdf/1209.0748.pdf
 * 
 * @author andi
 * 
 */
public class Gravitational extends AbstractIterativeAlgorithm {
	private static final double I_MAX = 10.0;
	private static final double K = 80.0;
	public static final String NAME = "Gravitational";
	private static final double OMEGA = 0.1;
	private double gamma;
	private Graph<Node, Link> graph;
	private int step;

	public Gravitational(final Graph<Node, Link> graph) {
		this.graph = graph;
		this.step = 0;
	}

	private Point2d computeCentroid() {
		int numVertex = graph.getVertexCount();
		Point2d centroid = new Point2d(0, 0);
		for (Node v : graph.getVertices()) {
			Point2d temp = new Point2d(0, 0);
			temp.add(v.getPos());
			temp.scale(1.0 / numVertex);
			centroid.add(temp);
		}
		return centroid;
	}

	public final double getGamma() {
		return gamma;
	}

	@Override
	public final void step() {
		Point2d centroid = computeCentroid();
		Hashtable<Node, Point2d> tempPos = new Hashtable<Node, Point2d>();
		gamma = 0.2 * (step / 200);
		for (Node v : graph.getVertices()) {
			Point2d i = new Point2d(0, 0);
			for (Node u : graph.getVertices()) {

				// gravitational force
				if (v.equals(u)) {
					Point2d f = (Point2d) centroid.clone();
					f.sub(v.getPos());
					f.scale(gamma);
					i.add(f);
				} else {
					double dist = v.getPos().distance(u.getPos());

					// attractive force
					if (graph.isNeighbor(v, u)) {
						Point2d dir = (Point2d) u.getPos().clone();
						dir.sub(v.getPos());
						dir.scale(dist / K);
						i.add(dir);
					}

					// repulsive force
					Point2d dir = (Point2d) v.getPos().clone();
					dir.sub(u.getPos());
					dir.scale((K * K) / (dist * dist));
					i.add(dir);
				}
			}

			// scaling
			double iNorm = i.distance(new Point2d(0, 0));
			if (iNorm > I_MAX) {
				i.scale(I_MAX / iNorm);
			}
			i.scale(OMEGA);
			tempPos.put(v, i);
		}

		// set new positions
		for (Node n : graph.getVertices()) {
			n.getPos().add(tempPos.get(n));
		}
		step++;
	}
}
