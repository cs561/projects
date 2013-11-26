package algorithms;

import java.util.Hashtable;
import javax.vecmath.Point2d;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import graph.Link;
import graph.Node;

//TODO NEEDS TO BE FIXED!!!
/**
 * Computes the Fruchterman-Reingold algorithm for a graph. Algorithm
 * implemented after
 * http://cs.brown.edu/~rt/gdhandbook/chapters/force-directed.pdf page 387
 * 
 * @author andi
 * 
 */
public class FruchtermanReingold extends AbstractIterativeAlgorithm {
	public static final String NAME = "Fruchterman-Reingold";
	private double area;
	private boolean done;
	Graph<Node, Link> graph;
	private double k;
	private static final double MAX_ITER = 700;
	private double step;
	private double t;

	public FruchtermanReingold(final Graph<Node, Link> graph) {
		this.graph = graph;
	}

	private void cool() {
		t *= (1.0 - step / MAX_ITER);
	}

	public final void initialize(final int xMax, final int yMax,
			final int nodeRadius) {

		// compute constants for algorithm
		area = xMax * yMax;
		k = Math.sqrt(area / graph.getVertexCount());
		t = (1.0 / 10) * xMax;

		this.step = 0;
	}

	public final boolean isDone() {
		return done;
	}

	public final void step() {

		if (done) {
			return;
		}

		Hashtable<Node, Point2d> tempPositions = new Hashtable<Node, Point2d>();

		// repulsive forces
		for (Node v : graph.getVertices()) {
			tempPositions.put(v, new Point2d(0, 0));
			for (Node u : graph.getVertices()) {
				if (!v.equals(u)) {
					double dist = v.getPos().distance(u.getPos());
					if (Double.isNaN(1.0 / dist)) { // TODO fix this
						System.out.println("oops2");
						dist = 1.0;
					}
					Point2d delta = (Point2d) v.getPos().clone();
					delta.sub(u.getPos());
					delta.scale(1.0 / dist);
					delta.scale((k * k) / dist);

					Point2d vDisp = tempPositions.get(v);
					vDisp.add(delta);
				}
			}
		}

		// attractive forces
		for (Link e : graph.getEdges()) {
			Pair<Node> nodes = graph.getEndpoints(e);
			Node v = nodes.getFirst();
			Node u = nodes.getSecond();
			double dist = v.getPos().distance(u.getPos());
			if (Double.isNaN(1.0 / dist)) { // TODO fix this
				System.out.println("oops3");
				dist = 1.0;
			}
			Point2d delta = (Point2d) v.getPos().clone();
			delta.sub(u.getPos());

			delta.scale(1.0 / dist);
			delta.scale((dist * dist) / k);
			Point2d vDisp = tempPositions.get(v);
			vDisp.sub(delta);
			Point2d uDisp = tempPositions.get(u);
			uDisp.add(delta);
		}

		// limitation
		for (Node v : graph.getVertices()) {
			Point2d deltaPos = (Point2d) tempPositions.get(v).clone();
			double norm = deltaPos.distance(new Point2d(0, 0));
			deltaPos.scale(1.0 / norm);
			deltaPos.scale(Math.min(norm, t));
			Point2d vPos = v.getPos();
			vPos.add(deltaPos);
		}

		cool();
		if (step > MAX_ITER || t < 1.0 / MAX_ITER) {
			done = true;
			return;
		}
		step++;

	}
}
