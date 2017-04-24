package de.fh_bielefeld.geograph.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class OSMStreetGUIController {

	@FXML private Button	searchButton;

	@FXML private TextField	latitudeTextField, longitudeTextField;

	@FXML private Slider	zoomSlider;

	@FXML private Pane		mapPane;

	@FXML
	public void initialize() {

		searchButton.setOnAction((event) -> {
			String latitude = latitudeTextField.getText();
			String longitude = longitudeTextField.getText();
			System.out.println(latitude + ", " + longitude);
		});

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Slider Value Changed (newValue: " + newValue.doubleValue() + ")\n");
		});

	}
}
