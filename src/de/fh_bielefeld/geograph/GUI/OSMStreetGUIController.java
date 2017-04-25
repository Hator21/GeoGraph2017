package de.fh_bielefeld.geograph.GUI;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class OSMStreetGUIController {

	@FXML private Button	searchButton;

	@FXML private TextField	latitudeTextField, longitudeTextField;

	@FXML private Slider	zoomSlider;

	@FXML private Canvas	paintingCanvas;

	private GraphicsContext	gc;

	private double			minLatitude	= 52, maxLatitude = 54, minLongitude = 8, maxLongitude = 10;

	@FXML
	public void initialize() {
		gc = paintingCanvas.getGraphicsContext2D();

		System.out.println(mapLongitude(9));
		searchButton.setOnAction((event) -> {
			String latitude = latitudeTextField.getText();
			String longitude = longitudeTextField.getText();
			System.out.println(latitude + ", " + longitude);
		});

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Slider Value Changed (newValue: " + newValue.doubleValue() + ")\n");
		});
	}

	private void drawShapes(GraphicsContext gc) {

	}

	public int mapLatitude(double latitude) {
		return (int) ((paintingCanvas.getWidth() * latitude - (minLatitude * paintingCanvas.getWidth())) / (maxLatitude - minLatitude));
	}

	public int mapLongitude(double longitude) {
		return (int) ((paintingCanvas.getHeight() * longitude - (minLongitude * paintingCanvas.getHeight())) / (maxLongitude - minLongitude));
	}

}
