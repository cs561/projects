package com.raytrace.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.raytrace.engine.Renderer;

public class ServerThread extends Thread {
	public static final int SO_TIMEOUT = 500;
	
	private Renderer renderer;
	private int port;
	private ServerSocket serverSocket;
	
	boolean shouldEnd;
	
	public ServerThread(int port, Renderer renderer) {
		this.renderer = renderer;
		this.port = port;
		shouldEnd = false;
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(SO_TIMEOUT);
		} catch (IOException e) {
			System.out.println("Server Thread Port " + port + ": " + e.getMessage());
			return;
		}
		
		while (!shouldEnd) {
			Socket clientSocket = null;
			while (!shouldEnd) {
				try {
					clientSocket = serverSocket.accept();
					
					break;
				} catch (Exception e) {
				}
			}
		
			if (shouldEnd) {
				break;
			}
			
			try {
				PrintStream out = new PrintStream(clientSocket.getOutputStream());
				DataOutputStream outData = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
				while (!in.ready() && !shouldEnd) {
					Thread.sleep(1);
				}
				
				String[] request = null;
				while (in.ready()) {
					String line = in.readLine();
					if (line.startsWith("GET")) {
						request = line.split(" ")[1].split("-");
					}
				}
				
				if (request != null && request[0].equalsIgnoreCase("/render")) {
					int width = Integer.parseInt(request[1]);
					int height = Integer.parseInt(request[2]);
					int renderLine = Integer.parseInt(request[3]);
					int maxRecursion = Integer.parseInt(request[4]);
					
					out.print("HTTP/1.1 200 OK\r\n");
					out.print("Server:RaytracerServer\r\n");
					out.print("Content-Type:text/plain\r\n\r\n");
					out.flush();
					
					int[] rgbs = renderer.renderLine(renderLine, width, height, maxRecursion);
					for (int rgb : rgbs) {
						outData.writeInt(rgb);
					}
					
					outData.flush();
				}
				
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Server Thread Port " + port + ": " + e.getMessage());
			}
			
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Server Thread Port " + port + ": " + e.getMessage());
		}
	}
	
	public void shouldEnd() {
		this.shouldEnd = true;
	}
}