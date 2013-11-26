package com.raytrace.engine;

public class AmbientLight {
	private float[] ambientColor;
	
	public AmbientLight(float[] ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public float[] getBrightness() {
		return ambientColor;
	}
}
