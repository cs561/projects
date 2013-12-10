package com.raytrace.engine;

public class ColorTriangle implements Intersectable {
	private final float[] a;
	private final float[] b;
	private final float[] c;
	private final float[] normal;
	private final float d;
	private final float[] color;
	
	public ColorTriangle (float[] a, float[] b, float[] c, float[] color) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		float[] ab = Matrix.substractVector(b, a);
		float[] ac = Matrix.substractVector(c, a);
		normal = Matrix.normalizeVector(Matrix.crossVector(ab, ac));

		d = -Matrix.dotVector(normal, a);
		
		this.color = color;
	}
	
	@Override
	public Intersection intersect(float[] origin, float[] direction) {
		// find intersection in plane
		float dotDirectionNormal = Matrix.dotVector(direction, normal);
		
		if (dotDirectionNormal == 0) {
			return null;
		}
		
		float t = -(Matrix.dotVector(origin, normal) + d) / dotDirectionNormal;
		float[] P = Matrix.addVector(origin, Matrix.multVectorScalar(direction, t));
		
		// find intersection in triangle
		if (!pointInTriangle(P)) {
			return null;
		}
		
		// merge
		float[] colorCopy = {color[0], color[1], color[2]};
		return new Intersection(this, P, normal, t, colorCopy);
	}

	private boolean pointInTriangle(float[] p) {
		return (internalSide(p,a,b,c) && internalSide(p,b,a,c) && internalSide(p,c,a,b));
	}
	
	private static boolean internalSide(float[] p1, float[] p2, float[] as, float[] bs) {
		float[] asbs = Matrix.substractVector(bs, as);
		float[] cp1 = Matrix.crossVector(asbs, Matrix.substractVector(p1, as));
		float[] cp2 = Matrix.crossVector(asbs, Matrix.substractVector(p2, as));
		
		return (Matrix.dotVector(cp1, cp2) >= 0);
	}
}
