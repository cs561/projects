package com.raytrace.engine;

public class DiffuseLight {
	private final float[] BLACK = {0, 0, 0};
	
	private float[] direction;
	private float[] diffuseColor;
	private float[] position;
	
	public DiffuseLight(float[] direction, float[] diffuseColor, float[] position) {
		this.direction = direction;
		this.diffuseColor = diffuseColor;
		this.position = position;
	}
	
	public float[] getBrightness(float[] normal) {
		float angle = Matrix.angleBetweenVector(normal, direction);
		if (angle > 0) {
			return BLACK;
		} else {
			return Matrix.multVectorScalar(diffuseColor, -angle);
		}
	}
	
	public float[] getPosition() {
		return position;
	}
}
