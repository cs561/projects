package com.raytrace.server;

import java.io.IOException;
import java.io.OutputStream;

import android.widget.TextView;

public class TextOutput extends OutputStream {

	private TextView textView;
	
	public TextOutput(TextView textView) {
		this.textView = textView;
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		String s = String.valueOf(c);
		textView.append(s);
	}

}
