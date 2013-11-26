package visual;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import algorithms.FruchtermanReingold;
import algorithms.Gravitational;
import algorithms.Spring;

/**
 * A GUI panel for setting different view setting for the graph.
 * 
 * @author andi
 * 
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1482452602990387052L;

	public MenuPanel(final DisplayPanel graphPanel) {
		super();
		String[] centralities = {"Betweenness Centrality", "Closeness Centrality", "Eigenvector Centrality"};
		JComboBox<String> centralityChooser = new JComboBox<String>(centralities);
		centralityChooser.setActionCommand("centralityChooser");
		centralityChooser.addActionListener((ActionListener) graphPanel);

		String[] algorithms = { FruchtermanReingold.NAME, Spring.NAME,
				Gravitational.NAME };
		JComboBox<String> algoChooser = new JComboBox<String>(algorithms);
		algoChooser.setActionCommand("algoChooser");
		algoChooser.addActionListener((ActionListener) graphPanel);

		String[] graphs = { "Graph 1", "Graph 2", "Graph 3", "Graph 4" };
		JComboBox<String> graphChooser = new JComboBox<String>(graphs);
		graphChooser.setActionCommand("graphChooser");
		graphChooser.addActionListener((ActionListener) graphPanel);
		graphChooser.setSelectedItem("Graph 1");
		
		this.add(centralityChooser);
		this.add(algoChooser);
		this.add(graphChooser);
		
	}
}
