package com.raytrace.engine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.raytrace.wavefrontloader.Triangle;
import com.raytrace.wavefrontloader.WavefrontMesh;

public class Renderer {
	private final float[] BACKGROUND = {0, 0, 0};
	
	private final float top;
	private final float bottom;
	private final float left;
	private final float right;
	
	private AmbientLight ambientLight;
	private DiffuseLight diffuseLight;
	private SpecularLight specularLight;
	
	private ArrayList<Intersectable> objects;
	
	public Renderer(float left, float right, float top, float bottom) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		
		objects = new ArrayList<Intersectable>();
	}
	
	public BufferedImage renderScene(int width, int height, int maxRecursion) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		float[] origin = {0,0,0};
		
		float deltaX = ((right - left) / width);
		float deltaY = ((bottom - top) / height);
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				
				float[] direction = {x * deltaX + left, y * deltaY + top, 1.0f};
				direction = Matrix.normalizeVector(direction);
				
				float[] color = rayTrace(origin, direction, maxRecursion);
				bufferedImage.setRGB(x, y, (((int)(color[0]*255))<<16) + (((int)(color[1]*255))<<8) + ((int)(color[2]*255)));
			}
			
			System.out.println(x + " / " + width);
		}
		
		return bufferedImage;
	}
	
	public int[] renderLine(int line, int width, int height, int maxRecursion) {
		int[] rgb = new int[width];
		
		float[] origin = {0,0,0};
		
		float deltaX = ((right - left) / width);
		float deltaY = ((bottom - top) / height);
		
		for (int x=0; x<width; x++) {
			float[] direction = {x * deltaX + left, line * deltaY + top, 1.0f};
			direction = Matrix.normalizeVector(direction);
				
			float[] color = rayTrace(origin, direction, maxRecursion);
			rgb[x] = (((int)(color[0]*255))<<16) + (((int)(color[1]*255))<<8) + ((int)(color[2]*255));
		}
		
		return rgb;
	}
	
	public float[] rayTrace(float[] origin, float[] direction, int maxRecursion) {
		Intersection nearestIntersection = null;
		
		for (Intersectable object : objects) {
			Intersection result = object.intersect(origin, direction);
			
			if ((result != null) && (result.t > 0.1f)) {
				if ((nearestIntersection == null) || (result.getT() < nearestIntersection.getT())) {
					nearestIntersection = result;
				}
			}
		}
		
		if (nearestIntersection != null) {
			float[] color = nearestIntersection.getColor();
			
			// ambient light
			float[] lightColor;
			if (ambientLight != null) {
				lightColor = Matrix.multVectorVectorComponents(color, ambientLight.getBrightness());
			} else {
				lightColor = color;
			}
			
			// diffuse light
			if (diffuseLight != null) {
				lightColor = Matrix.addVector(Matrix.multVectorVectorComponents(color, diffuseLight.getBrightness(nearestIntersection.getNormal())), lightColor);
			}
			
			// specular light
			if (specularLight != null) {
				lightColor = Matrix.addVector(Matrix.multVectorVectorComponents(color, specularLight.getBrightness(nearestIntersection.getNormal(), direction)), lightColor);
			}

			// reflection
			if (maxRecursion > 0) {
				float[] reflectedDirection = Matrix.substractVector(Matrix.multVectorScalar(Matrix.multVectorScalar(nearestIntersection.getNormal(), -2),Matrix.dotVector(nearestIntersection.getNormal(), direction)), direction);
				
				float[] reflectedColor = rayTrace(nearestIntersection.getIntersection(), reflectedDirection, maxRecursion-1);
				if (reflectedColor != BACKGROUND) {					
					lightColor = Matrix.addVector(Matrix.multVectorScalar(lightColor, 0.7f), Matrix.multVectorScalar(reflectedColor, 0.3f));
				}
			}
			
			return lightColor;
		} else {
			return BACKGROUND;
		}
	}
	
	public void addMesh(WavefrontMesh mesh) {
		ArrayList<Triangle> triangles = mesh.getTriangles();
		for (Triangle triangle : triangles) {
			addObject(new TextureTriangle(triangle.getVertexA(), triangle.getVertexB(), triangle.getVertexC(),
					  triangle.getTextureCoordinateA(), triangle.getTextureCoordinateB(), triangle.getTextureCoordinateC(),
					  triangle.getNormalA(), triangle.getNormalB(), triangle.getNormalC(),
					  triangle.getTexture().getImage()));
		}
	}
	
	public void addObject(Intersectable object) {
		objects.add(object);
	}
	
	public void setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
	}
	
	public void setDiffuseLight(DiffuseLight diffuseLight) {
		this.diffuseLight = diffuseLight;
	}
	
	public void setSpecularLight(SpecularLight specularLight) {
		this.specularLight = specularLight;
	}
}
