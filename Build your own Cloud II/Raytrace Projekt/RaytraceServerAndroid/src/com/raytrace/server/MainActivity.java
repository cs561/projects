package com.raytrace.server;

import java.io.PrintStream;

import com.raytrace.engine.AmbientLight;
import com.raytrace.engine.ColorSphere;
import com.raytrace.engine.ColorTriangle;
import com.raytrace.engine.DiffuseLight;
import com.raytrace.engine.Renderer;
import com.raytrace.engine.SpecularLight;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static AssetManager assetManager;
	private ServerThread[] threads;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		assetManager = getAssets();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView textView = (TextView) findViewById(R.id.textView);
		TextOutput textOutput = new TextOutput(textView);
		System.setOut(new PrintStream(textOutput));
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Renderer renderer = initEngine();
		
		WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		int intIp = myWifiInfo.getIpAddress();
		String ip = (intIp & 0xFF) + "." + ((intIp >> 8) & 0xFF) + "." + ((intIp >> 16) & 0xFF) + "." + ((intIp >> 24) & 0xFF);
		
		int numCores = Runtime.getRuntime().availableProcessors();

		System.out.println("Raytracing Server V10E-Inf");
		System.out.println("(c) Jan Ebbe, Michael Schneider");
		System.out.println("    __ 		");
		System.out.println("   <- ) 	");
		System.out.println("   /( \\ 		" + numCores + " cores found!");
		System.out.println("   \\_\\_> 	");
		System.out.println("   \" \"	");
		System.out.println("");

		threads = new ServerThread[numCores];
		for (int i=0; i<numCores; i++) {
			int port = 1337 + i;
			System.out.println("Thread " + (i+1) + ": " + ip + ":" + port);
			threads[i] = new ServerThread(port, renderer);
			threads[i].start();;
		}
	}
	
	@Override
	protected void onPause () {
		for (ServerThread thread : threads) {
			thread.shouldEnd();
		}
		
		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Renderer initEngine() {
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
		
		/*
		float[] ta = {1, 0};
		float[] tb = {0, 0};
		float[] tc = {0, 1};
		float[] td = {1, 1};
		
		Bitmap t = null;
		try {
			t = BitmapFactory.decodeStream(getAssets().open("chessboard.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		float[] color = {0.8f, 0.8f, 0.8f};
		renderer.addObject(new ColorTriangle(a,b,c,color));
		renderer.addObject(new ColorTriangle(c,d,a,color));
		
		// mesh
		/*
		WavefrontMesh mesh = new WavefrontMesh("hheli.obj");
		float[] scale = {0.025f, 0.025f, 0.025f};
		mesh.scale(scale);
		float[] translation = {0, -1, 4};
		mesh.translate(translation);
		renderer.addMesh(mesh);
		*/

		// spheres
		for (int y=0; y<=2; y++) {
			for (int x = 0; x<=4; x++) {
				float[] s = {x * 3f - 6f,y * 3f - 3f,7};
				
				float[] sc = {y/2f,1-y/2f,x/4f};
				renderer.addObject(new ColorSphere(s,1,sc));
			}
		}
		
		return renderer;
	}
	
	public static AssetManager getAssetManager() {
		return assetManager;
	}

}
