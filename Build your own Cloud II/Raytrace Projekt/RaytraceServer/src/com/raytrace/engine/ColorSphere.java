package com.raytrace.engine;

public class ColorSphere implements Intersectable {
	private final float[] center;
	private final float rSquare;
	private final float[] color;
	
	public ColorSphere (float[] center, float r, float[] color) {
		this.center = center;
		rSquare = r * r;
		this.color = color;
	}
	
	public Intersection intersect(float[] origin, float[] direction) {
		// find intersection
		float[] L = Matrix.substractVector(center, origin);
		float tca = Matrix.dotVector(L, direction);
		
		if (tca < 0) {
			return null;
		}
		
		float dSquare = Matrix.dotVector(L, L) - tca * tca;
		
		if (dSquare > rSquare) {
			return null;
		}
		
		float thc = (float) Math.sqrt(rSquare - dSquare);
		float t = (float) Math.min(tca - thc, tca + thc);
		
		float[] P = Matrix.addVector(origin, Matrix.multVectorScalar(direction, t));
		
		// find normal
		float[] N = Matrix.substractVector(P, center);
		N = Matrix.normalizeVector(N);
		
		
		// merge
		float[] colorCopy = {color[0], color[1], color[2]};
		return new Intersection(this, P, N, t, colorCopy);
	}
}