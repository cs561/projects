import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import visual.DisplayPanel;
import visual.MenuPanel;

public class Program {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		DisplayPanel graphDisp = new DisplayPanel(new Dimension(600, 600));
		MenuPanel menuPan = new MenuPanel(graphDisp);
		JScrollPane scroll = new JScrollPane(graphDisp,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		frame.getContentPane().add(scroll, BorderLayout.CENTER);
		frame.getContentPane().add(menuPan, BorderLayout.NORTH);

		frame.pack();
		frame.setVisible(true);

		while (true) {
			frame.repaint();
		}
	}
}
