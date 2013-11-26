package com.raytrace.wavefrontloader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.raytrace.engine.Matrix;

public class WavefrontMesh {

	private ArrayList<Triangle> triangles;
	
	public WavefrontMesh(File meshFile) {
		try {
			triangles = parseObjFile(meshFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<Triangle> parseObjFile(File meshFile) throws Exception {
		ArrayList<float[]> vertices = new ArrayList<float[]>();
		ArrayList<float[]> textureCoordinates = new ArrayList<float[]>();
		ArrayList<float[]> normals = new ArrayList<float[]>();
		ArrayList<Texture> textures = null;
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		InputStream meshStream = new FileInputStream(meshFile);
		BufferedReader meshReader = new BufferedReader(new InputStreamReader(meshStream));
			
		String line;
		Texture currentTexture = null;
		while ((line = meshReader.readLine()) != null) {
			line = line.replaceAll("#IND", "");
			line = line.replaceAll("  ", " ");
			String[] parts = line.trim().split(" ");
				
			if (parts[0].equalsIgnoreCase("v")) {
				float[] data = {Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])};
				vertices.add(data);
			} else if (parts[0].equalsIgnoreCase("vt")) {
				float[] data = {Float.parseFloat(parts[1]), 1-Float.parseFloat(parts[2])};
				textureCoordinates.add(data);
			} else if (parts[0].equalsIgnoreCase("vn")) {
				float[] data = {Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])};
				normals.add(data);
			} else if (parts[0].equalsIgnoreCase("f")) {
				String[] subPartA = parts[1].split("/");
				String[] subPartB = parts[2].split("/");
				String[] subPartC = parts[3].split("/");
				
				int[] a = {(int) Math.abs(Integer.parseInt(subPartA[0]))-1, (int) Math.abs(Integer.parseInt(subPartA[1]))-1, (int) Math.abs(Integer.parseInt(subPartA[2]))-1};
				int[] b = {(int) Math.abs(Integer.parseInt(subPartB[0]))-1, (int) Math.abs(Integer.parseInt(subPartB[1]))-1, (int) Math.abs(Integer.parseInt(subPartB[2]))-1};
				int[] c = {(int) Math.abs(Integer.parseInt(subPartC[0]))-1, (int) Math.abs(Integer.parseInt(subPartC[1]))-1, (int) Math.abs(Integer.parseInt(subPartC[2]))-1};
					
				Triangle triangle = new Triangle(vertices.get(a[0]), vertices.get(b[0]), vertices.get(c[0]),
												 textureCoordinates.get(a[1]), textureCoordinates.get(b[1]), textureCoordinates.get(c[1]),
												 normals.get(a[2]), normals.get(b[2]), normals.get(c[2]),
												 currentTexture);
					
				triangles.add(triangle);
			} else if (parts[0].equalsIgnoreCase("mtllib")) {
				textures = parseMtlFile(new File(meshFile.getParentFile(),parts[1]));
			} else if (parts[0].equalsIgnoreCase("usemtl")) {
				for (Texture texture : textures) {
					if (texture.getName().equalsIgnoreCase(parts[1])) {
						currentTexture = texture;
						break;
					}
				}
			}
			
		}
			
		meshReader.close();
		
		return triangles;
	}
	
	private ArrayList<Texture> parseMtlFile(File mtlFile) throws Exception {
		ArrayList<Texture> textures = new ArrayList<Texture>();
		
		InputStream mtlStream = new FileInputStream(mtlFile);
		BufferedReader mtlReader = new BufferedReader(new InputStreamReader(mtlStream));
			
		String line;
		String currentName = null;
		while ((line = mtlReader.readLine()) != null) {
			String[] parts = line.trim().split(" ");
			
			if (parts[0].equalsIgnoreCase("newmtl")) {
				currentName = parts[1];
			} else if (parts[0].equalsIgnoreCase("map_Kd")) {
				if (parts.length > 1) {
					File textureFile = new File(mtlFile.getParentFile(), parts[1]);
					
					BufferedImage textureImage = null;
					if (textureFile.getName().endsWith(".tga")) {
						textureImage = TargaReader.getImage(textureFile);
					} else {
						textureImage = ImageIO.read(textureFile);
					}
				
					textures.add(new Texture(currentName, textureImage));
					
				}
			}
		}
		
		mtlReader.close();
		
		return textures;
	}
	
	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}
	
	public void scale(float[] scale) {
		for (Triangle triangle : triangles) {
			
			float[] a = Matrix.multVectorVectorComponents(triangle.getVertexA(), scale);
			float[] b = Matrix.multVectorVectorComponents(triangle.getVertexB(), scale);
			float[] c = Matrix.multVectorVectorComponents(triangle.getVertexC(), scale);
			
			triangle.setVertexA(a);
			triangle.setVertexB(b);
			triangle.setVertexC(c);
		}
	}
	
	public void translate(float[] translate) {
		for (Triangle triangle : triangles) {
			
			float[] a = Matrix.addVector(triangle.getVertexA(), translate);
			float[] b = Matrix.addVector(triangle.getVertexB(), translate);
			float[] c = Matrix.addVector(triangle.getVertexC(), translate);
			
			triangle.setVertexA(a);
			triangle.setVertexB(b);
			triangle.setVertexC(c);
		}
	}
}
