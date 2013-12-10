package com.raytrace.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

import javax.imageio.ImageIO;

import com.raytrace.engine.AmbientLight;
import com.raytrace.engine.ColorSphere;
import com.raytrace.engine.DiffuseLight;
import com.raytrace.engine.Renderer;
import com.raytrace.engine.SpecularLight;
import com.raytrace.engine.TextureTriangle;
import com.raytrace.wavefrontloader.WavefrontMesh;

public class RaytraceServer {
	private static ServerThread[] threads;
	
	public static void main(String args[]) {
		ConsoleFrame console = new ConsoleFrame();
		System.setOut(new PrintStream(console));
		
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
		System.out.println("   \" \"	" + numCores + " cores found!");
		System.out.println("");
		
		threads = new ServerThread[numCores];
		for (int i=0; i<numCores; i++) {
			int port = 1337 + i;
			System.out.println("Thread " + (i+1) + ": " + ip + ":" + port);
			threads[i] = new ServerThread(port, renderer);
			threads[i].start();;
		}
	}
	
	public static void endServerThreads() {
		for (ServerThread thread : threads) {
			thread.shouldEnd();
		}
	}
	
	private static Renderer initEngine() {
		Renderer renderer = new Renderer(-1f,1f,0.5625f,-0.5625f);

		// setup light
		float[] ambientColor = {0.2f, 0.2f, 0.2f};
		AmbientLight ambientLight = new AmbientLight(ambientColor);
		
		float[] diffuseColor = {0.5f, 0.5f, 0.5f};
		float[] diffuseDirection = {0, 1, 1};
		float[] diffusePosition = {0, -1, 1};
		DiffuseLight diffuseLight = new DiffuseLight(diffuseDirection, diffuseColor, diffusePosition);
		
		float[] specularColor = {0.3f, 0.3f, 0.3f};
		float[] specularDirection = {0, 1, 1};
		float[] specularPosition = {0, -1, 1};
		SpecularLight specularLight = new SpecularLight(specularDirection, specularColor, 15, specularPosition);
		
		renderer.setAmbientLight(ambientLight);
		renderer.setDiffuseLight(diffuseLight);
		renderer.setSpecularLight(specularLight);
		
		// plane background
		float[] a = {20,-20,13};
		float[] b = {-20,-20,13};
		float[] c = {-20,20,13};
		float[] d = {20,20,13};
		float[] ta = {1, 0};
		float[] tb = {0, 0};
		float[] tc = {0, 1};
		float[] td = {1, 1};
		BufferedImage t = null;
		try {
			t = ImageIO.read(new File("mesh/chessboard.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		renderer.addObject(new TextureTriangle(a,b,c,ta,tb,tc,t));
		renderer.addObject(new TextureTriangle(c,d,a,tc,td,ta,t));
		
		// mesh
		WavefrontMesh mesh = new WavefrontMesh(new File("mesh/hheli.obj"));
		float[] scale = {0.025f, 0.025f, 0.025f};
		mesh.scale(scale);
		float[] translation = {0, -1, 7};
		mesh.translate(translation);
		renderer.addMesh(mesh);

		// spheres
		float[] s1 = {4f,2f,7};
		float[] s1c = {1,0,0};
		renderer.addObject(new ColorSphere(s1,1,s1c));
		
		float[] s2 = {-5f,0f,7};
		float[] s2c = {0,0,1};
		renderer.addObject(new ColorSphere(s2,1,s2c));
		
		float[] s3 = {3f,-2.5f,7};
		float[] s3c = {0,1,0};
		renderer.addObject(new ColorSphere(s3,1,s3c));
		
		float[] s4 = {-2.5f,-2.5f,7};
		float[] s4c = {1,1,0};
		renderer.addObject(new ColorSphere(s4,1,s4c));
		
		//debug
//		try {
//			ImageIO.write(renderer.renderScene(320, 180, 5), "png", new File("/Users/jan_ebbe/desktop/ray.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return renderer;
	}
}
