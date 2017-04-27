package de.fh_bielefeld.geograph.GUI;

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

public class OSMStreetGUIController {

	@FXML private Button	searchButton;

	@FXML private TextField	latitudeTextField, longitudeTextField;

	@FXML private Slider	zoomSlider;

	@FXML private Canvas	paintingCanvas;

	private GraphicsContext	gc;

	private double			minLatitude, maxLatitude, minLongitude, maxLongitude;

	private ContentHolder	content		= new ContentHolder(this);

	private final int		NODERADIUS	= 3;
	private final int		ARR_SIZE	= 5;

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
				popUp("Längengrad");
				longitudeTextField.setText("");
			}
		});

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Slider Value Changed (newValue: " + newValue.doubleValue() + ")\n");
		});

		getNodes();

		drawArrow(gc, 50, 50, 200, 200);

	}

	private void getNodes() {
		content.getNodes().sendContent();
	}

	public void drawNode(MapNode node) {
		gc.setStroke(Color.BLACK);
		gc.strokeOval(mapLatitude(node.getLatitude()) - NODERADIUS, mapLongitude(node.getLongitude()) - NODERADIUS, NODERADIUS * 2, NODERADIUS * 2);
		gc.setFill(Color.RED);
		gc.fillOval(mapLatitude(node.getLatitude()) - NODERADIUS + 1, mapLongitude(node.getLongitude()) - NODERADIUS + 1, NODERADIUS * 2 - 2, NODERADIUS * 2 - 2);
	}

	private void getWay() {
		content.getWays().sendContent();
	}

	public boolean drawWay(MapWay way) {
		// gc.strokeOval(mapLatitude(way.getLatitude()), mapLongitude(way.getLongitude()), 3, 3);
		return true;
	}

	public double mapLatitude(double latitude) {
		return (paintingCanvas.getWidth() * latitude - (content.getMinLatitude() * paintingCanvas.getWidth())) / (content.getMaxLatitude() - content.getMinLatitude());
	}

	public double mapLongitude(double longitude) {
		return (paintingCanvas.getHeight() * longitude - (content.getMinLongitude() * paintingCanvas.getHeight())) / (content.getMaxLongitude() - content.getMinLongitude());
	}

	private void popUp(String grad) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Der " + grad + " ist keine Zahl");
		alert.showAndWait();
	}

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

	private void createExampleData() {
		content.setLatitude(8.0);
		content.setMinLatitude(7.0);
		content.setMaxLatitude(9.0);
		content.setLongitude(50.0);
		content.setMinLongitude(49.0);
		content.setMaxLongitude(51.0);

		content.getNodes().insert(new MapNode("1", 8.0, 50.0));
		content.getNodes().insert(new MapNode("2", 7.9, 50.1));
		content.getNodes().insert(new MapNode("3", 8.1, 50.1));
		content.getNodes().insert(new MapNode("4", 7.9, 49.9));
		content.getNodes().insert(new MapNode("5", 8.1, 49.9));

		content.getNodes().serializePrefix();
	}

}
