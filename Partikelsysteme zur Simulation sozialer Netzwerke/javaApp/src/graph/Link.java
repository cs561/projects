package graph;

public class Link {
	private int id;
	private double capacity;
	private double weight;

	public Link(final int id, final double weight, final double capacity) {
		this.id = id;
		this.weight = weight;
		this.capacity = capacity;
	}

	public Link(final int id) {
		this.id = id;
		this.weight = 0;
		this.capacity = 0;
	}

	public final String toString() {
		return "id: " + id;
	}
}
