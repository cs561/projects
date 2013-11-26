package graph;

import java.util.Random;

import javax.vecmath.Point2d;

public class Node {

	private int id;
	private String name;
	private Point2d pos;

	public Node() {
		this.pos = new Point2d(0, 0);
		this.id = 0;
		this.name = "defaultname";
	}

	public Node(final int id, final String name) {
		this.pos = new Point2d(0, 0);
		this.id = id;
		this.name = name;
	}

	public final Point2d getPos() {
		return pos;
	}

	public final void setPos(final double x, final double y) {
		this.pos.x = x;
		this.pos.y = y;
	}

	public final void setPos(final Point2d pos) {
		this.pos = pos;

	}

	public final void setRandomPos(final int maxX, final int maxY) {
		Random r = new Random();
		setPos(r.nextInt(maxX), r.nextInt(maxY));
	}

}
