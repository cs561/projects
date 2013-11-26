package com.raytrace.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.raytrace.engine.AmbientLight;
import com.raytrace.engine.ColorSphere;
import com.raytrace.engine.ColorTriangle;
import com.raytrace.engine.DiffuseLight;
import com.raytrace.engine.Renderer;
import com.raytrace.engine.SpecularLight;
import com.raytrace.engine.TextureTriangle;
import com.raytrace.wavefrontloader.Triangle;
import com.raytrace.wavefrontloader.WavefrontMesh;

public class RaytraceServer {
	public static final int VERSION = 100;
	
	public static void main(String args[]) {
		Renderer renderer = initEngine();
		
		int numCores = Runtime.getRuntime().availableProcessors();
		
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			System.out.println("Error: Could not find host ip!");
			System.exit(0);
		}
		
		System.out.println("    __ 		");
		System.out.println("   <- ) 	Raytracing Server V10E-Inf");
		System.out.println("   /( \\ 	(c) Jan Ebbe, Michael Schneider");
		System.out.println("   \\_\\_> 	");
		System.out.println("   \" \"		" + numCores + " cores found!");
		System.out.println("");
		
		ServerThread[] threads = new ServerThread[numCores];
		for (int i=0; i<numCores; i++) {
			int port = 1337 + i;
			System.out.println("Thread " + (i+1) + ": " + ip + ":" + port);
			threads[i] = new ServerThread(port, renderer);
			threads[i].start();;
		}
		System.out.println("");
		
		System.out.println("Press return to quit...");
		try {
			System.in.read();
		} catch (Exception e) {
		} finally {
			for (ServerThread thread : threads) {
				thread.shouldEnd();
			}
		}
	}
	
	public static Renderer initEngine() {
		Renderer renderer = new Renderer(-1.77f,1.77f,1,-1);
		
		// setup light
		float[] ambientColor = {0.2f, 0.2f, 0.2f};
		AmbientLight ambientLight = new AmbientLight(ambientColor);
		
		float[] diffuseColor = {0.3f, 0.3f, 0.3f};
		float[] diffuseDirection = {0, 0, 1};
		DiffuseLight diffuseLight = new DiffuseLight(diffuseDirection, diffuseColor);
		
		float[] specularColor = {0.5f, 0.5f, 0.5f};
		float[] specularDirection = {0, 0, 1};
		SpecularLight specularLight = new SpecularLight(specularDirection, specularColor, 15);
		
		renderer.setAmbientLight(ambientLight);
		renderer.setDiffuseLight(diffuseLight);
		renderer.setSpecularLight(specularLight);
		
		// plane background
		float[] a = {150,-150,75};
		float[] b = {-150,-150,75};
		float[] c = {-150,150,75};
		float[] d = {150,150,75};
		float[] ta = {1, 0};
		float[] tb = {0, 0};
		float[] tc = {0, 1};
		float[] td = {1, 1};
		BufferedImage t = null;
		try {
			t = ImageIO.read(new File("/Users/jan_ebbe/Downloads/chessboard.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		renderer.addObject(new TextureTriangle(a,b,c,ta,tb,tc,t));
		renderer.addObject(new TextureTriangle(c,d,a,tc,td,ta,t));
		
		// spheres
		float[] s1 = {25,12,18};
		float[] s1c = {1,0,0};
		renderer.addObject(new ColorSphere(s1,5,s1c));
		
		float[] s2 = {-25,12,30};
		float[] s2c = {0,1,0};
		renderer.addObject(new ColorSphere(s2,5,s2c));
		
		float[] s3 = {-25,-12,25};
		float[] s3c = {0,0,1};
		renderer.addObject(new ColorSphere(s3,5,s3c));
		
		float[] s4 = {25,-12,21};
		float[] s4c = {1,1,0};
		renderer.addObject(new ColorSphere(s4,5,s4c));
		
		// load mesh
		WavefrontMesh mesh = new WavefrontMesh(new File("mesh/jeep.obj"));
		float[] scale = {0.10f, 0.10f, 0.10f};
		mesh.scale(scale);
		float[] translate = {0,-30,50};
		mesh.translate(translate);
		renderer.addMesh(mesh);
		
		//debug
		try {
			ImageIO.write(renderer.renderScene(160, 90, 3), "png", new File("/Users/jan_ebbe/desktop/ray.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return renderer;
	}
}
