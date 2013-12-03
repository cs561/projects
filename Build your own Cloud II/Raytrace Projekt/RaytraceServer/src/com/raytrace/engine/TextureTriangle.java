package com.raytrace.engine;

import java.awt.image.BufferedImage;

public class TextureTriangle implements Intersectable {
	private final float[] a;
	private final float[] b;
	private final float[] c;
	private final float[] ta;
	private final float[] tb;
	private final float[] tc;
	private final float[] na;
	private final float[] nb;
	private final float[] nc;
	private final float[] normal;

	private final float d;
	private BufferedImage texture;
	
	public TextureTriangle (float[] a, float[] b, float[] c, float[] ta, float[] tb, float[] tc, BufferedImage texture) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		float[] ab = Matrix.substractVector(b, a);
		float[] ac = Matrix.substractVector(c, a);
		normal = Matrix.normalizeVector(Matrix.crossVector(ab, ac));
		d = -Matrix.dotVector(normal, a);
		
		na = nb = nc = normal;
		
		
		this.ta = ta;
		this.tb = tb;
		this.tc = tc;
		
		this.texture = texture;
	}
	
	public TextureTriangle (float[] a, float[] b, float[] c, float[] ta, float[] tb, float[] tc, float[] na, float[] nb, float[] nc, BufferedImage texture) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		float[] ab = Matrix.substractVector(b, a);
		float[] ac = Matrix.substractVector(c, a);
		normal = Matrix.normalizeVector(Matrix.crossVector(ab, ac));
		d = -Matrix.dotVector(normal, a);
		
		this.na = na;
		this.nb = nb;
		this.nc = nc;
		
		this.ta = ta;
		this.tb = tb;
		this.tc = tc;
		
		this.texture = texture;
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
		
		float[] barycentric = calcBarycentric(P);
		
		if (barycentric[0] > 1 || barycentric[0] < 0 || barycentric[1] > 1 || barycentric[1] < 0 || barycentric[2] > 1 || barycentric[2] < 0) {
			return null;
		}
		
		// find texture color
		int textureX = (int) ((ta[0]*barycentric[0] + tb[0]*barycentric[1] + tc[0]*barycentric[2]) * (texture.getWidth()-1));
		int textureY = (int) ((ta[1]*barycentric[0] + tb[1]*barycentric[1] + tc[1]*barycentric[2]) * (texture.getHeight()-1));
		
		int textureColor = texture.getRGB(textureX, textureY);
		float[] color = { ((textureColor >> 16) & 0xFF) / 255f, ((textureColor >> 8) & 0xFF) / 255f, (textureColor & 0xFF) / 255f};
		
		// find normal
		float[] customNormal = new float[3];
		customNormal[0] = na[0]*barycentric[0] + nb[0]*barycentric[1] + nc[0]*barycentric[2];
		customNormal[1] = na[1]*barycentric[0] + nb[1]*barycentric[1] + nc[1]*barycentric[2];
		customNormal[2] = na[2]*barycentric[0] + nb[2]*barycentric[1] + nc[2]*barycentric[2];
		
		// merge
		return new Intersection(this, P, customNormal, t, color);
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
	
	private float[] calcBarycentric(float[] p) {
		float[] v0 = Matrix.substractVector(b, a);
		float[] v1 = Matrix.substractVector(c, a);
		float[] v2 = Matrix.substractVector(p, a);
		
		float d00 = Matrix.dotVector(v0, v0);
		float d01 = Matrix.dotVector(v0, v1);
		float d11 = Matrix.dotVector(v1, v1);
		float d20 = Matrix.dotVector(v2, v0);
		float d21 = Matrix.dotVector(v2, v1);
		float denom = d00 * d11 - d01 * d01;
		
	    float v = (d11 * d20 - d01 * d21) / denom;
	    float w = (d00 * d21 - d01 * d20) / denom;
	    float u = 1.0f - v - w;
		
		float[] bary = {u,v,w};
		return bary;
	}
}
