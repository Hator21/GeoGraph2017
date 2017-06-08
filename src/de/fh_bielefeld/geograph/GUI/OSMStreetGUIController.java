package de.fh_bielefeld.geograph.GUI;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;
import de.fh_bielefeld.geograph.PARSER.OmlParser;

public class OSMStreetGUIController {

	private OmlParser				parser;

	@FXML private Button			/* searchButton, */searchButtonArea/* , fileChooserButton */;

	@FXML private TextField			latitudeTextField, longitudeTextField, latitudeTextFieldL, longitudeTextFieldL, latitudeTextFieldR, longitudeTextFieldR;

	@FXML private Slider			zoomSlider;

	@FXML private Canvas			paintingCanvas;
	
	@FXML private AnchorPane		anchor;
	
	@FXML private AnchorPane		rightAnchor;
	
	private Stage					stage;

	private GraphicsContext			gc;

	private ContentHolderInterface	content		= new ContentHolder(this);

	private final int				NODERADIUS	= 3;
	private final int				ARR_SIZE	= 5;
	
	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	this.resize();

	@FXML
	public void initialize() {
		gc = paintingCanvas.getGraphicsContext2D();
		rightAnchor.widthProperty().addListener(stageSizeListener);
		rightAnchor.heightProperty().addListener(stageSizeListener);
		
		/*
		 * searchButton.setOnAction((event) -> {
		 * double latitude;
		 * double longitude;
		 * try {
		 * latitude = Double.parseDouble(latitudeTextField.getText());
		 * content.setLatitude(latitude);
		 * } catch (NumberFormatException nbe) {
		 * popUp("Breitengrad");
		 * latitudeTextField.setText("");
		 * }
		 * try {
		 * longitude = Double.parseDouble(longitudeTextField.getText());
		 * content.setLongitude(longitude);
		 * } catch (NumberFormatException nbe) {
		 * popUp("Laengengrad");
		 * longitudeTextField.setText("");
		 * }
		 * callParser();
		 * });
		 */

		searchButtonArea.setOnAction((event) -> {
			boolean ok = true;
			double latitudeL = 0;
			double longitudeL = 0;
			double latitudeR = 0;
			double longitudeR = 0;
			try {
				latitudeL = Double.parseDouble(latitudeTextFieldL.getText());
			} catch (NumberFormatException nbe) {
				popUp("Breitengrad Links");
				latitudeTextFieldL.setText("");
				ok = false;
			}
			try {
				longitudeL = Double.parseDouble(longitudeTextFieldL.getText());

			} catch (NumberFormatException nbe) {
				popUp("Laengengrad Links");
				longitudeTextFieldL.setText("");
				ok = false;
			}
			try {
				latitudeR = Double.parseDouble(latitudeTextFieldR.getText());

			} catch (NumberFormatException nbe) {
				popUp("Breitengrad Rechts");
				latitudeTextFieldR.setText("");
				ok = false;
			}
			try {
				longitudeR = Double.parseDouble(longitudeTextFieldR.getText());
			} catch (NumberFormatException nbe) {
				popUp("Laengengrad Rechts");
				longitudeTextFieldR.setText("");
				ok = false;
			}
			if (ok) {
				if (latitudeL < latitudeR) {
					if (longitudeL < longitudeR) {
						content.setMinLatitude(latitudeL);
						content.setMinLongitude(longitudeL);
						content.setMaxLatitude(latitudeR);
						content.setMaxLongitude(longitudeR);
						callParser();
					} else {
						popUpLong();
					}
				} else {
					popUpLat();
				}
			}
		});

		/*
		 * fileChooserButton.setOnAction((event) -> {
		 * FileChooser fileChooser = new FileChooser();
		 * fileChooser.setTitle("Open OSM File");
		 * fileChooser.showOpenDialog(fileChooserButton.getScene().getWindow());
		 * });
		 */

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Slider Value Changed (newValue: " + newValue.doubleValue() + ")\n");
		});
		
	}
	
	private void resize(){
		clearCanvas();
		paintingCanvas.setWidth(rightAnchor.getWidth()-14.0);
		paintingCanvas.setHeight(rightAnchor.getHeight()-14.0);
		draw();
	}
	
	private void clearCanvas(){
		gc.setFill(Color.ALICEBLUE);
		gc.clearRect(0, 0, paintingCanvas.getWidth(), paintingCanvas.getHeight());
	}

	private void getNodes() {
		for (MapNodeInterface node : content.getNodes()) {
			System.out.println("drawNode: " + node.getId());
			drawNode(node);
		}
	}

	public void drawNode(MapNodeInterface node) {
		if (0 <= (mapLatitude(node.getLatitude()) - NODERADIUS) && (mapLatitude(node.getLatitude()) - NODERADIUS) <= paintingCanvas.getHeight() && 0 <= (mapLongitude(node.getLongitude()) - NODERADIUS) && (mapLongitude(node.getLongitude()) - NODERADIUS) <= paintingCanvas.getWidth()) {
			gc.setStroke(Color.BLACK);
			gc.strokeOval(mapLongitude(node.getLongitude()) - NODERADIUS, mapLatitude(node.getLatitude()) - NODERADIUS, NODERADIUS * 2, NODERADIUS * 2);
			gc.setFill(Color.RED);
			gc.fillOval(mapLongitude(node.getLongitude()) - NODERADIUS + 1, mapLatitude(node.getLatitude()) - NODERADIUS + 1, NODERADIUS * 2 - 2, NODERADIUS * 2 - 2);
		}
	}

	private void getWays() {
		for (MapWayInterface way : content.getWays()) {
			drawWay(way);
		}
	}

	public void drawWay(MapWayInterface way) {
		MapNodeInterface node1;
		MapNodeInterface node2;
		for (int i = 0; i < way.getRefList().size() - 1; i++) {
			String id1 = way.getRefList().get(i);
			String id2 = way.getRefList().get(i + 1);
			node1 = null;
			node2 = null;
			for (MapNodeInterface node : content.getNodes()) {
				if (node.getId().equals(id1)) {
					node1 = node;
				}
				if (node.getId().equals(id2)) {
					node2 = node;
				}
			}
			if (node1 != null && node2 != null) {
				int y1 = (int) (mapLatitude(node1.getLatitude()));
				int x1 = (int) (mapLongitude(node1.getLongitude()));
				int y2 = (int) (mapLatitude(node2.getLatitude()));
				int x2 = (int) (mapLongitude(node2.getLongitude()));
				drawArrow(gc, x1, y1, x2, y2);
			}
		}
	}

	public double mapLatitude(double latitude) {
		double y = paintingCanvas.getHeight() - (paintingCanvas.getHeight() * ((latitude - content.getMinLatitude()) / (content.getMaxLatitude() - content.getMinLatitude())));
		return y;

	}

	public double mapLongitude(double longitude) {
		double x = paintingCanvas.getWidth() * ((longitude - content.getMinLongitude()) / (content.getMaxLongitude() - content.getMinLongitude()));
		return x;
	}

	private void popUp(String grad) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Der " + grad + " ist keine Zahl");
		alert.showAndWait();
	}

	private void popUpLat() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Latitude Links > Latitude Rechts");
		alert.showAndWait();
	}

	private void popUpLong() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Longitude Links > Longitude Rechts");
		alert.showAndWait();
	}

	public void drawArrow(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		gc.setFill(Color.BLACK);

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);

		Transform save = gc.getTransform();
		Transform transform = Transform.translate(x1, y1);
		transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
		gc.setTransform(new Affine(transform));

		gc.strokeLine(0, 0, len, 0);
		gc.fillPolygon(new double[] {
				len, len - ARR_SIZE, len - ARR_SIZE, len
		}, new double[] {
				0, -ARR_SIZE, ARR_SIZE, 0
		}, 4);
		
		gc.setTransform(new Affine(save));
	}

	private void draw() {
		getNodes();
		getWays();
	}

	private void callParser() {
		parser = new OmlParser(content);
		try {
			content = parser.parse();
			draw();
		} catch (NullPointerException | InvalidAPIRequestException e) {
			e.printStackTrace();
		}
	}
}
