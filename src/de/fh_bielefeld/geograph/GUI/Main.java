package de.fh_bielefeld.geograph.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Class which starts the Program and starts the gui.
 */
public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("OSMStreetGUI2.fxml"));
		stage.setTitle("OSMStreetGUI");
		stage.setScene(new Scene(root, 1300, 700));
		stage.show();
	}
}
