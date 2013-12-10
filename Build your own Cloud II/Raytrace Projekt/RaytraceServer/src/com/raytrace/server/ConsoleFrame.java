package com.raytrace.server;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ConsoleFrame extends OutputStream implements WindowListener {
	private JTextArea txtArea;
	
	public ConsoleFrame() {
		JFrame frame = new JFrame();
		frame.addWindowListener(this);
		frame.setTitle("Raytrace Server Console");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 672, 396);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		txtArea = new JTextArea();
		scrollPane.setViewportView(txtArea);
		
		frame.setVisible(true);
	}

	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		String s = String.valueOf(c);
		txtArea.append(s);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		RaytraceServer.endServerThreads();
		System.exit(0);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
