package com.raytrace.engine;

public class Intersection {
	Intersectable object;
	float[] intersection;
	float[] normal;
	float t;
	float[] color;

	public Intersection(Intersectable object, float[] intersection, float[] normal, float t, float[] color) {
		this.object = object;
		this.intersection = intersection;
		this.normal = normal;
		this.t= t;
		this.color = color;
	}
	
	public Intersectable getObject() {
		return object;
	}
	
	public float[] getIntersection() {
		return intersection;
	}

	public float[] getNormal() {
		return normal;
	}

	public float getT() {
		return t;
	}

	public float[] getColor() {
		return color;
	}
}
