package de.fh_bielefeld.geograph.GUI;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class OSMStreetGUIController {

	@FXML private Button	searchButton;

	@FXML private TextField	latitudeTextField, longitudeTextField;

	@FXML private Slider	zoomSlider;

	@FXML private Canvas	paintingCanvas;

	private GraphicsContext	gc;

	private double			minLatitude, maxLatitude, minLongitude, maxLongitude;

	private ContentHolder	content	= new ContentHolder();

	@FXML
	public void initialize() {
		gc = paintingCanvas.getGraphicsContext2D();

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
	}

	private void drawShapes(GraphicsContext gc, AVLTree<MapNode> t) {

	}

	public int mapLatitude(double latitude) {
		return (int) ((paintingCanvas.getWidth() * latitude - (minLatitude * paintingCanvas.getWidth())) / (maxLatitude - minLatitude));
	}

	public int mapLongitude(double longitude) {
		return (int) ((paintingCanvas.getHeight() * longitude - (minLongitude * paintingCanvas.getHeight())) / (maxLongitude - minLongitude));
	}

	private void popUp(String grad) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Der " + grad + " ist keine Zahl");
		alert.showAndWait();
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
	}

}
