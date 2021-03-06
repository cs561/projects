package com.raytrace.engine;

public class SpecularLight {
	private final float[] BLACK = {0, 0, 0};
	
	private float focusing;
	private float[] direction;
	private float[] specularColor;
	private float[] position;
	
	public SpecularLight(float[] direction, float[] specularColor, float focusing, float[] position) {
		this.direction = direction;
		this.specularColor = specularColor;
		this.focusing = focusing;
		this.position = position;
	}
	
	public float[] getBrightness(float[] normal, float[] rayDirection) {
		float[] reflectedLight = Matrix.substractVector(Matrix.multVectorScalar(Matrix.multVectorScalar(normal, 2),Matrix.dotVector(normal, direction)), direction);
		float angle = Matrix.angleBetweenVector(reflectedLight, rayDirection);
		if (angle > 0) {
			angle = (float) Math.pow(angle, focusing);
			return Matrix.multVectorScalar(specularColor, angle);
		} else {
			return BLACK;
		}
	}
	
	public float[] getPosition() {
		return position;
	}
}
