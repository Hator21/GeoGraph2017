package de.fh_bielefeld.geograph.GUI;

import de.fh_bielefeld.geograph.GUI_INTERFACE.AVLTreeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * Controller class for the GUI.
 * Controls the elements in the GUI.
 */
public class OSMStreetGUIController {

	@FXML private Button	searchButton;

	@FXML private TextField	latitudeTextField, longitudeTextField;

	@FXML private Slider	zoomSlider;

	@FXML private Canvas	paintingCanvas;

	private GraphicsContext	gc;

	private ContentHolderInterface	content		= new ContentHolder(this);

	private final int		NODERADIUS	= 3;
	private final int		ARR_SIZE	= 5;

	/**
	 * Initializes the elemts with standard values.
	 */
	@FXML
	public void initialize() {
		gc = paintingCanvas.getGraphicsContext2D();
		createExampleData();

		searchButton.setOnAction((event) -> {
			double latitude;
			double longitude;
			try {
				latitude = Double.parseDouble(latitudeTextField.getText());
				content.setLatitude(latitude);
			} catch (NumberFormatException nbe) {
				popUp("Breitengrad");
				latitudeTextField.setText("");
			}
			try {
				longitude = Double.parseDouble(longitudeTextField.getText());
				content.setLongitude(longitude);
			} catch (NumberFormatException nbe) {
				popUp("Laengengrad");
				longitudeTextField.setText("");
			}
		});

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Slider Value Changed (newValue: " + newValue.doubleValue() + ")\n");
		});

		getNodes();

		drawArrow(gc, 83, 903, 103, 923);

	}

	/**
	 * Getter for the Nodes in the Map.
	 * 
	 * @return Nodes on the map.
	 */
	private void getNodes() {
		content.getNodes().sendContent();
	}

	/**
	 * Draws the Node in the map.
	 * 
	 * @param  Node to view on the map.
	 */
	public void drawNode(MapNodeInterface node) {
		gc.setStroke(Color.BLACK);
		gc.strokeOval(mapLongitude(node.getLongitude()) - NODERADIUS, mapLatitude(node.getLatitude()) - NODERADIUS, NODERADIUS * 2, NODERADIUS * 2);
		gc.setFill(Color.RED);
		gc.fillOval(mapLongitude(node.getLongitude()) - NODERADIUS + 1, mapLatitude(node.getLatitude()) - NODERADIUS + 1, NODERADIUS * 2 - 2, NODERADIUS * 2 - 2);
	}

	/**
	 * TODO
	 */
	private void getWay() {
		content.getWays().sendContent();
	}

	/**
	 * Draws the way on the map.
	 * 
	 * @param Way to draw.
	 * 
	 * @return True if the way has been drawn. False if the way has not been drawn.
	 */
	public boolean drawWay(MapWayInterface way) {
		// gc.strokeOval(mapLatitude(way.getLatitude()), mapLongitude(way.getLongitude()), 3, 3);
		return true;
	}

	/**
	 * TODO
	 */
	public double mapLatitude(double latitude) {
		double y = paintingCanvas.getHeight() - ((paintingCanvas.getHeight() - 0) / (content.getMaxLatitude() - content.getMinLatitude()) * (latitude - content.getMinLatitude()));
		return y;
	}

	/**
	 * TODO
	 */
	public double mapLongitude(double longitude) {
		double y = (paintingCanvas.getWidth() - 0) / (content.getMaxLongitude() - content.getMinLongitude()) * (longitude - content.getMinLongitude());
		return y;
	}

	/**
	 * Gives an alert if the input string expects not a value.
	 * 
	 * @param String from input field.
	 * 
	 */
	private void popUp(String grad) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Der " + grad + " ist keine Zahl");
		alert.showAndWait();
	}

	/**
	 * Draws the arrow on the map.
	 * 
	 * @param TODO
	 */
	public void drawArrow(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		gc.setFill(Color.BLACK);

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);

		Transform transform = Transform.translate(x1, y1);
		transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
		gc.setTransform(new Affine(transform));

		gc.strokeLine(0, 0, len, 0);
		gc.fillPolygon(new double[] {
				len, len - ARR_SIZE, len - ARR_SIZE, len
		}, new double[] {
				0, -ARR_SIZE, ARR_SIZE, 0
		}, 4);
	}

	/**
	 * Creates example data.
	 * TODO remove
	 */
	private void createExampleData() {
		content.setLatitude(52.1174047);
		content.setMinLatitude(52.1164047);
		content.setMaxLatitude(52.1184047);
		content.setLongitude(8.6764046);
		content.setMinLongitude(8.6740046);
		content.setMaxLongitude(8.6788046);

		content.getNodes().insert(new MapNode("1", 52.1172509, 8.6764067));
		content.getNodes().insert(new MapNode("2", 52.1172090, 8.6764746));
		content.getNodes().insert(new MapNode("3", 52.1170113, 8.6768153));
		content.getNodes().insert(new MapNode("4", 52.1166197, 8.6773873));
		content.getNodes().insert(new MapNode("5", 52.1162315, 8.6778956));
		content.getNodes().insert(new MapNode("6", 52.1160254, 8.6781414));
		content.getNodes().insert(new MapNode("7", 52.1158071, 8.6784022));

//		content.getNodes().serializePrefix();
	}

}
