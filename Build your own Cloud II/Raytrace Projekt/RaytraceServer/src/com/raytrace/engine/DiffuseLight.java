package com.raytrace.engine;

public class DiffuseLight {
	private final float[] BLACK = {0, 0, 0};
	
	private float[] direction;
	private float[] diffuseColor;
	
	public DiffuseLight(float[] direction, float[] diffuseColor) {
		this.direction = direction;
		this.diffuseColor = diffuseColor;
	}
	
	public float[] getBrightness(float[] normal) {
		float angle = Matrix.angleBetweenVector(normal, direction);
		if (angle > 0) {
			return BLACK;
		} else {
			return Matrix.multVectorScalar(diffuseColor, -angle);
		}
	}
}
