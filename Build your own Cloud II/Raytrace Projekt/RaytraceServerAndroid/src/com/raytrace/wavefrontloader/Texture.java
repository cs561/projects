package com.raytrace.wavefrontloader;

import android.graphics.Bitmap;

public class Texture {
	private final String name;
	private final Bitmap image;
	
	public Texture(String name, Bitmap image) {
		this.name = name;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public Bitmap getImage() {
		return image;
	}
}
