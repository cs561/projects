package com.raytrace.wavefrontloader;

public class Triangle {
	private float[] vertexA;
	private float[] vertexB;
	private float[] vertexC;
	
	private final float[] textureCoordinateA;
	private final float[] textureCoordinateB;
	private final float[] textureCoordinateC;
	
	private final float[] normalA;
	private final float[] normalB;
	private final float[] normalC;
	
	private final Texture texture;
	
	public Triangle(float[] vertexA, float[] vertexB, float[] vertexC, float[] textureCoordinateA, float[] textureCoordinateB, float[] textureCoordinateC, float[] normalA, float[] normalB, float[] normalC, Texture texture) {
		this.vertexA = vertexA;
		this.vertexB = vertexB;
		this.vertexC = vertexC;
		
		this.textureCoordinateA = textureCoordinateA;
		this.textureCoordinateB = textureCoordinateB;
		this.textureCoordinateC = textureCoordinateC;
		
		this.normalA = normalA;
		this.normalB = normalB;
		this.normalC = normalC;
		
		this.texture = texture;
	}
	
	public float[] getVertexA() {
		return vertexA;
	}

	public float[] getVertexB() {
		return vertexB;
	}

	public float[] getVertexC() {
		return vertexC;
	}

	public void setVertexA(float[] vertexA) {
		this.vertexA = vertexA;
	}

	public void setVertexB(float[] vertexB) {
		this.vertexB = vertexB;
	}

	public void setVertexC(float[] vertexC) {
		this.vertexC = vertexC;
	}

	public float[] getTextureCoordinateA() {
		return textureCoordinateA;
	}

	public float[] getTextureCoordinateB() {
		return textureCoordinateB;
	}

	public float[] getTextureCoordinateC() {
		return textureCoordinateC;
	}

	public float[] getNormalA() {
		return normalA;
	}

	public float[] getNormalB() {
		return normalB;
	}

	public float[] getNormalC() {
		return normalC;
	}

	public Texture getTexture() {
		return texture;
	}
}
