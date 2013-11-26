package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import utils.GraphFactory;
import utils.JSONManager;
import algorithms.FruchtermanReingold;
import algorithms.Gravitational;
import algorithms.Spring;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import graph.Centralities;
import graph.Link;
import graph.Node;

/**
 * This class is used to draw our graphs.
 * 
 * @author andi
 * 
 */
public class DisplayPanel extends JPanel implements ActionListener,
		MouseWheelListener, MouseMotionListener, MouseListener {

	private class FruchtermanTask extends SwingWorker<Void, DisplayPanel> {

		@Override
		protected Void doInBackground() throws Exception {
			FruchtermanReingold fr = new FruchtermanReingold(graph);
			fr.initialize(600, 600, NODE_RADIUS);
			while (!fr.isDone()) {
				fr.step();
				Thread.sleep(1L); // needed for interruption when algorithm
									// changes
			}
			return null;
		}
	}

	private class GravTask extends SwingWorker<Void, DisplayPanel> {
		@Override
		protected Void doInBackground() throws Exception {
			Gravitational grav = new Gravitational(graph);
			while (grav.getGamma() <= 2.5) {
				grav.step();
				Thread.sleep(1L);
			}
			return null;
		}
	}

	private class SpringTask extends SwingWorker<Void, DisplayPanel> {

		@Override
		protected Void doInBackground() throws Exception {
			Spring spr = new Spring(graph);
			for (int M = 1; M <= Integer.MAX_VALUE; M++) {
				spr.step();
				Thread.sleep(1L);
			}
			return null;
		}

	}

	private static HashMap<Node, Double> centralities;
	private static final Color DEFAULT_NODE_COLOR = new Color(0, 0, 0);
	private static final double DEFAULT_ZOOM_FACTOR = 0.1;
	public static final int NODE_RADIUS = 10;
	private static final long serialVersionUID = 8565993015479185343L;

	private static void drawLine(Graphics g, Node n, Node n2) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.draw(new Line2D.Double(n.getPos().x, n.getPos().y, n2.getPos().x,
				n2.getPos().y));

	}

	private static void drawNode(Graphics g, Node n, boolean showCentrality) {
		Graphics2D g2D = (Graphics2D) g;
		if (!showCentrality) {
			g2D.setColor(DEFAULT_NODE_COLOR);

		} else {
			int color = new Double(centralities.get(n) * 255).intValue();
			g2D.setColor(new Color(color, color, color));
		}
		g2D.fill(new Ellipse2D.Double(n.getPos().x - (NODE_RADIUS / 2), n
				.getPos().y - (NODE_RADIUS / 2), NODE_RADIUS, NODE_RADIUS));
	}

	private SwingWorker<Void, DisplayPanel> currentTask;

	private Graph<Node, Link> graph;
	private int initX;

	private int initY;
	private double scaleFactor;
	private boolean showCentrality;
	private boolean transform;
	private int transX;
	private int transY;
	private int dragInitX;
	private int dragInitY;
	private int dragX;
	private int dragY;

	public DisplayPanel(final Dimension size) {
		super();
		this.setPreferredSize(size);
		showCentrality = false;
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		scaleFactor = 1.0;
		initX = size.width;
		initY = size.height;
	}

	/**
	 * This reacts to events triggered in MenuPanel.
	 */
	public final void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
		case "centralityChooser":
			JComboBox<String> comboBoxCentr = (JComboBox<String>) e.getSource();
			showCentrality = true;
			switch((String) comboBoxCentr.getSelectedItem()){
			case "Betweenness Centrality":
				centralities = Centralities.computeCentrality(graph, "Betweenness");
				break;
			case "Closeness Centrality":
				centralities = Centralities.computeCentrality(graph, "Closeness");
				break;
			case "Eigenvector Centrality":
				centralities = Centralities.computeCentrality(graph, "Eigenvector");
				break;
			}
			this.repaint();
			break;
		case "algoChooser":
			if (currentTask != null) {
				currentTask.cancel(true);
			}
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
			switch ((String) comboBox.getSelectedItem()) {
			case "Fruchterman-Reingold":
				currentTask = new FruchtermanTask();
				break;
			case "Gravitational":
				currentTask = new GravTask();
				break;
			case "Spring":
				currentTask = new SpringTask();
				break;
			default:
				break;
			}
			currentTask.execute();
			break;
		case "graphChooser":
			if (currentTask != null) {
				currentTask.cancel(true);
			}
			JComboBox<String> comboBox2 = (JComboBox<String>) e.getSource();
			new GraphFactory();
			switch ((String) comboBox2.getSelectedItem()) {
			case "Graph 1":
				reset(GraphFactory.createSmallGraph());
				break;
			case "Graph 2":
				reset(GraphFactory.createTestGraph());
				break;
			case "Graph 3":
				// TODO make file not hardcoded here or something... (props file
				// or cli parameter)
				reset(GraphFactory.createGraph(JSONManager
						.loadJSONObject("data.json")));
				break;
			case "Graph 4":
				reset(GraphFactory.createStarGraph());
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void displayGraph(final Graphics g) {

		// Draw lines
		for (Link e : graph.getEdges()) {
			Pair<Node> nodes = graph.getEndpoints(e);
			drawLine(g, nodes.getFirst(), nodes.getSecond());
		}

		// Draw nodes
		for (Node n : graph.getVertices()) {
			drawNode(g, n, showCentrality);
		}

	}

	@Override
	public final void mouseWheelMoved(final MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() < 0) {
			scaleFactor += DEFAULT_ZOOM_FACTOR;
		} else {
			scaleFactor -= DEFAULT_ZOOM_FACTOR;
		}
		transX = arg0.getX();
		transY = arg0.getY();
		transform = true;
	}

	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int midX = new Double((initX * scaleFactor) / 2).intValue();
		int midY = new Double((initY * scaleFactor) / 2).intValue();

		doZoomTransformation(g2d);
		
		AffineTransform drag = new AffineTransform();
		drag.setToTranslation(dragX, dragY);
		g2d.transform(drag);
		
		displayGraph(g2d);

		// update preferred window size
		int newX = new Double(scaleFactor * initX).intValue();
		int newY = new Double(scaleFactor * initY).intValue();

		this.setPreferredSize(new Dimension(newX, newY));
		this.revalidate();
	}

	private void doZoomTransformation(Graphics2D g2d) {
		AffineTransform zoomTrans = new AffineTransform();
		if (transform) { // only on new zoom
			AffineTransform transInv = new AffineTransform();
			transInv.setToTranslation(-transX, -transY);
			zoomTrans.concatenate(transInv);
		}

		// scale by current zoom
		AffineTransform scale = new AffineTransform();
		scale.setToScale(scaleFactor, scaleFactor);
		zoomTrans.concatenate(scale);

		if (transform) { // transform back if we have new zoom
			AffineTransform translation = new AffineTransform();
			translation.setToTranslation(transX, transY);
			zoomTrans.concatenate(translation);
			transform = false;
		}

		g2d.transform(zoomTrans);
	}

	private void reset(final Graph<Node, Link> graph) {

		this.graph = graph;
		setRandomGraphPos(new Dimension(
				new Double(initX * (1.0 / scaleFactor)).intValue(), 
				new Double(initY * (1.0 / scaleFactor)).intValue()));

		showCentrality = false;
		currentTask = null;
	}

	private void setRandomGraphPos(final Dimension size) {
		for (Node n : graph.getVertices()) {
			n.setRandomPos(size.width, size.height);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		dragX = arg0.getX() - dragInitX;
		dragY = arg0.getY() - dragInitY;
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		dragInitX = arg0.getX();
		dragInitY = arg0.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	//TODO Verschieben des Graphen mit der Maus (drag!!!)
}
