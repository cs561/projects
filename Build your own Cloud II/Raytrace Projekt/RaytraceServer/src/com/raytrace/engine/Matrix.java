package com.raytrace.engine;

public class Matrix {

	public static float[][] getIdentity() {
		float[][] m = new float[4][4];
		m[0][0] = 1;
		m[1][1] = 1;
		m[2][2] = 1;
		m[3][3] = 1;
		return m;
	}
	
	public static float[][] getFrustum(float left, float right, float bottom, float top, float near, float far) {
		float[][] m = new float[4][4];
		m[0][0] = (2*near)/(right-left);
		m[2][0] = (right+left)/(right-left);
		m[1][1] = (2*near)/(top-bottom);
		m[2][1] = (top+bottom)/(top-bottom);
		m[2][2] = -(far+near)/(far-near);
		m[3][2] = -(2*far*near)/(far-near);
		m[2][3] = -1;
		return m;
	}
	
	public static float[][] getTranslation(float[] offset) {
		float[][] m = getIdentity();
		m[3][0] = offset[0];
		m[3][1] = offset[1];
		m[3][2] = offset[2];
		return m;
	}
	
	public static float[][] getScale(float[] scale) {
		float[][] m = new float[4][4];
		m[0][0] = scale[0];
		m[1][1] = scale[1];
		m[2][2] = scale[2];
		m[3][3] = 1;
		return m;
	}
	
	public static float[][] getXRotation(float alpha) {
		float[][] m = new float[4][4];
		m[0][0] = 1;
		m[1][1] = (float) Math.cos(alpha);
		m[2][1] = (float) -Math.sin(alpha);
		m[1][2] = (float) Math.sin(alpha);
		m[2][2] = (float) Math.cos(alpha);
		m[3][3] = 1;
		return m;
	}
	
	public static float[][] getYRotation(float alpha) {
		float[][] m = new float[4][4];
		m[0][0] = (float) Math.cos(alpha);
		m[2][0] = (float) Math.sin(alpha);
		m[1][1] = 1;
		m[0][2] = (float) -Math.sin(alpha);
		m[2][2] = (float) Math.cos(alpha);
		m[3][3] = 1;
		return m;
	}
	
	public static float[][] getZRotation(float alpha) {
		float[][] m = new float[4][4];
		m[0][0] = (float) Math.cos(alpha);
		m[1][0] = (float) -Math.sin(alpha);
		m[0][1] = (float) Math.sin(alpha);
		m[1][1] = (float) Math.cos(alpha);
		m[2][2] = 1;
		m[3][3] = 1;
		return m;
	}
	
	public static float[][] multMatrixMatrix(float[][] m1, float[][] m2) {
		float[][] m = new float[4][4];
		
		for (int x=0; x<4; x++) {
			for (int y=0; y<4; y++) {
				float v = 0;
				for (int n=0; n<4; n++) {
					v += m1[n][y]*m2[x][n];
				}
				m[x][y] = v;
			}
		}
		
		return m;
	}
	
	public static float[] multMatrixVector(float[][] m, float[] v) {
		float[] vector = new float[4];
		
		for (int y=0; y<4; y++) {
			float val = 0;
			for (int n=0; n<4; n++) {
				val += m[n][y]*v[n];
			}
			vector[y] = val;
		}
		
		return vector;
	}
	
	public static float[] multVectorVectorComponents(float[] v1, float[] v2) {
		float[] vector = new float[3];
		vector[0] = v1[0] * v2[0];
		vector[1] = v1[1] * v2[1];
		vector[2] = v1[2] * v2[2];
		return vector;
	}
	
	public static float[] multVectorScalar(float[] v, float s) {
		float[] vector = new float[3];
		vector[0] = v[0] * s;
		vector[1] = v[1] * s;
		vector[2] = v[2] * s;
		return vector;
	}
	
	public static float[] crossVector(float[] v1, float[] v2) {
		float[] vector = new float[3];
		vector[0] = v1[1]*v2[2] - v1[2]*v2[1];
		vector[1] = v1[2]*v2[0] - v1[0]*v2[2];
		vector[2] = v1[0]*v2[1] - v1[1]*v2[0];
		return vector;
	}
	
	public static float[] normalizeVector(float[] v) {
		float length = lengthVector(v);
		float[] vector = new float[3];
		vector[0] = v[0] / length;
		vector[1] = v[1] / length;
		vector[2] = v[2] / length;
		return vector;
	}
	
	public static float[] addVector(float[] v1, float[] v2) {
		float[] vector = new float[3];
		vector[0] = v1[0] + v2[0];
		vector[1] = v1[1] + v2[1];
		vector[2] = v1[2] + v2[2];
		return vector;
	}
	
	public static float[] substractVector(float[] v1, float[] v2) {
		float[] vector = new float[3];
		vector[0] = v1[0] - v2[0];
		vector[1] = v1[1] - v2[1];
		vector[2] = v1[2] - v2[2];
		return vector;
	}
	
	public static float dotVector(float[] v1, float[] v2) {
		return (float) v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
	}
	
	public static float angleBetweenVector(float[] v1, float[] v2) {
		float[] v1n = normalizeVector(v1);
		float[] v2n = normalizeVector(v2);
		return dotVector(v1n, v2n);
	}
	
	public static float lengthVector(float[] v) {
		return (float) Math.sqrt(dotVector(v, v));
	}
	
	public static void printMatrix(float[][] matrix) {
		for (int y=0; y<4; y++) {
			for (int x=0; x<4; x++) {
				System.out.print(matrix[x][y] + " \t");
			}
			
			System.out.println("");
		}
	}
	
	public static void printVector(float[] v) {
		System.out.println("(" + v[0] + ", " + v[1] + ", " + v[2] + ")");
	}
}
