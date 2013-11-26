package com.raytrace.wavefrontloader;

import java.awt.image.BufferedImage;

public class Texture {
	private final String name;
	private final BufferedImage image;
	
	public Texture(String name, BufferedImage image) {
		this.name = name;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
