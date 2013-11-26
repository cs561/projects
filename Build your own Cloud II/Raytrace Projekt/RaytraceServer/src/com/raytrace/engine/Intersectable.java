package com.raytrace.engine;

public interface Intersectable {
	public Intersection intersect(float[] origin, float[] direction);
}
