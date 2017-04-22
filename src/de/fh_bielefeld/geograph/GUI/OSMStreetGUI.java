package de.fh_bielefeld.geograph.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OSMStreetGUI extends Application {

	public static void main(String[] args) {
		Application.launch(OSMStreetGUI.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("fxml_OSMStreetGUI.fxml"));

		stage.setTitle("OSMStreetGUI");
		stage.setScene(new Scene(root, 1600, 900));
		stage.show();
	}
}
