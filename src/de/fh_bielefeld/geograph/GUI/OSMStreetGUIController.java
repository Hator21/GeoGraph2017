package de.fh_bielefeld.geograph.GUI;

import java.awt.Point;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;
import de.fh_bielefeld.geograph.PARSER.OSMParser;

public class OSMStreetGUIController {

	private OSMParser				parser;

	@FXML private Button			searchButtonArea/* , fileChooserButton, fileSaveButton */;

	@FXML private TextField			latitudeTextFieldL, longitudeTextFieldL, latitudeTextFieldR, longitudeTextFieldR;

	@FXML private Slider			zoomSlider;

	@FXML private Canvas			paintingCanvas;

	@FXML private AnchorPane		rightAnchor;

	private GraphicsContext			gc;

	private ContentHolderInterface	content				= new ContentHolder(this);

	private final int				NODERADIUS			= 3;
	private final int				ARR_SIZE			= 5;

	private double					zoomFactor			= 0;
	private double					pressedX			= 0;
	private double					pressedY			= 0;
	private double					draggedX			= 0;
	private double					draggedY			= 0;
	private double					resultX				= 0;
	private double					resultY				= 0;

	ChangeListener<Number>			stageSizeListener	= (observable, oldValue, newValue) -> this.resize();
	private boolean					firstcall			= true;

	/**
	 * Initializes functions of all Components, which interact with the user
	 * Buttons
	 * TextFields
	 * Slider
	 * Canvas
	 * 
	 */
	@FXML
	public void initialize() {
		gc = paintingCanvas.getGraphicsContext2D();
		rightAnchor.widthProperty().addListener(stageSizeListener);
		rightAnchor.heightProperty().addListener(stageSizeListener);

		searchButtonArea.setOnAction((event) -> {
			boolean ok = true;
			firstcall = true;
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
						if (latitudeR - latitudeL <= 0.25) {
							if (longitudeR - longitudeL <= 0.25) {
								content.setMinLatitude(latitudeL);
								content.setMinLongitude(longitudeL);
								content.setMaxLatitude(latitudeR);
								content.setMaxLongitude(longitudeR);
								callParser();
							} else {
								popUpLongToBig();
							}
						} else {
							popUpLatToBig();
						}
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

		/*
		 * fileSaveButton.setOnAction((event) -> {
		 * FileChooser fileChooser = new FileChooser();
		 * fileChooser.setTitle("Save OSM File");
		 * fileChooser.showOpenDialog(fileSaveButton.getScene().getWindow());
		 * });
		 */

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			zoomFactor = zoomSlider.getValue();
			clearCanvas();
			draw();
		});

		paintingCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				pressedX = e.getX();
				pressedY = e.getY();
			}
		});

		paintingCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				draggedX = e.getX();
				draggedY = e.getY();
				resultX = resultX + draggedX - pressedX;
				resultY = resultY + draggedY - pressedY;
				pressedX = draggedX;
				pressedY = draggedY;
				gc.clearRect(0, 0, paintingCanvas.getWidth(), paintingCanvas.getHeight());
				draw();
			}
		});
	}

	/**
	 * Resizes the Map, when changes Window-Bounds
	 */
	private void resize() {
		clearCanvas();
		paintingCanvas.setWidth(rightAnchor.getWidth() - 14.0);
		paintingCanvas.setHeight(rightAnchor.getHeight() - 14.0);
		draw();
	}

	/**
	 * Clears the MapCanvas
	 */
	private void clearCanvas() {
		gc.clearRect(0, 0, paintingCanvas.getWidth(), paintingCanvas.getHeight());
	}

	/**
	 * Converts the coordinates of a node int coordinates of the Canvas
	 * 
	 * @param node
	 * @return Point (longitude, latitude)
	 */
	private Point getNodeCoords(MapNodeInterface node) {
		double latitude = (mapLatitude(node.getLatitude()) - NODERADIUS);
		double longitude = (mapLongitude(node.getLongitude()) - NODERADIUS);
		double middleX = paintingCanvas.getWidth() / 2;
		double middleY = paintingCanvas.getHeight() / 2;
		double latitudeN = (latitude) - (middleY - latitude) * zoomFactor + resultY;
		double longitudeN = (longitude) + (longitude - middleX) * zoomFactor + resultX;
		return new Point((int) longitudeN, (int) latitudeN);
	}

	/**
	 * Runs over all nodes and call them to draw
	 */
	private void getNodes() {
		for (MapNodeInterface node : content.getNodes()) {
			drawNode(node);
		}
	}

	/**
	 * Draws a single Node on the MapCanvas
	 * 
	 * @param node
	 */
	public void drawNode(MapNodeInterface node) {
		Point c = getNodeCoords(node);
		double latitude = c.getY();
		double longitude = c.getX();
		if (0 <= latitude && latitude <= paintingCanvas.getHeight() && 0 <= longitude && longitude <= paintingCanvas.getWidth()) {
			gc.setStroke(Color.BLACK);
			gc.strokeOval(longitude + 1, latitude + 1, NODERADIUS * 2, NODERADIUS * 2);
			gc.setFill(Color.RED);
			gc.fillOval(longitude + 2, latitude + 2, NODERADIUS * 2 - 2, NODERADIUS * 2 - 2);
		} else {
			if (firstcall) {
				zoomSlider.setMin(zoomSlider.getValue() - 0.025);
				zoomSlider.setValue(zoomSlider.getValue() - 0.025);
				clearCanvas();
				draw();
			}
		}
	}

	/**
	 * Runs over all ways and call them to draw
	 */
	private void getWays() {
		for (MapWayInterface way : content.getWays()) {
			drawWay(way);
		}
	}

	/**
	 * Draws a single Way on the MapCanvas
	 * Length of the Arrow is the speed u can drive on the road
	 * If speed cant be read, the arrow has 100% length
	 * 
	 * @param node
	 */
	public void drawWay(MapWayInterface way) {
		firstcall = false;
		MapNodeInterface node1;
		MapNodeInterface node2;
		int speed = 0;
		ArrayList<MapTag> taglist = way.getTagList();
		for (MapTag mt : taglist) {
			if (mt.getKey().equals("maxspeed")) {
				try {
					speed = Integer.parseInt(mt.getValue());
				} catch (NumberFormatException e) {
					speed = 0;
				}
			}
		}
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
				Point c1 = getNodeCoords(node1);
				double latitude1 = c1.getY();
				double longitude1 = c1.getX();
				Point c2 = getNodeCoords(node2);
				double latitude2 = c2.getY();
				double longitude2 = c2.getX();
				int y1 = (int) (latitude1 + 4);
				int x1 = (int) (longitude1 + 4);
				int y2 = (int) (latitude2 + 4);
				int x2 = (int) (longitude2 + 4);

				float xn = 0, yn = 0;
				if (speed != 0) {
					xn = (float) (x1) + ((float) (x2) - (float) (x1)) * ((float) (speed) / 130.0f);
					yn = (float) (y1) + ((float) (y2) - (float) (y1)) * ((float) (speed) / 130.0f);
					drawArrow(gc, x1, y1, Math.round(xn), Math.round(yn));
				} else {
					drawArrow(gc, x1, y1, x2, y2);
				}
			}
		}
	}

	/**
	 * Converts latitude into height-coordinate
	 * 
	 * @param latitude
	 * @return height-coordinate
	 */
	public double mapLatitude(double latitude) {
		double y = paintingCanvas.getHeight() - (paintingCanvas.getHeight() * ((latitude - content.getMinLatitude()) / (content.getMaxLatitude() - content.getMinLatitude())));
		return y;

	}

	/**
	 * Converts longitude into width-coordinate
	 * 
	 * @param longitude
	 * @return width-coordinate
	 */
	public double mapLongitude(double longitude) {
		double x = paintingCanvas.getWidth() * ((longitude - content.getMinLongitude()) / (content.getMaxLongitude() - content.getMinLongitude()));
		return x;
	}

	/**
	 * Generates a PopUp, that pops up when an input is wrong
	 * 
	 * @param grad
	 */
	private void popUp(String grad) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Der " + grad + " ist keine Zahl");
		alert.showAndWait();
	}

	/**
	 * Generates a PopUp, that pops up when lower latitude is bigger higher latitude
	 */
	private void popUpLat() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Latitude Links > Latitude Rechts");
		alert.showAndWait();
	}

	/**
	 * Generates a PopUp, that pops up when lower longitude is bigger higher longitude
	 */
	private void popUpLong() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Longitude Links > Longitude Rechts");
		alert.showAndWait();
	}

	/**
	 * Generates a PopUp, that pops up when lower latitude is bigger higher latitude
	 */
	private void popUpLatToBig() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Abstand zwischen Latitude Links und Latitude Rechts ist größer als 0.25");
		alert.showAndWait();
	}

	/**
	 * Generates a PopUp, that pops up when lower longitude is bigger higher longitude
	 */
	private void popUpLongToBig() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Abstand zwischen Longitude Links und Longitude Rechts ist größer als 0.25");
		alert.showAndWait();
	}

	/**
	 * Function to draw and Array from (x1,y1) to (x2,y2) on GraphicsContent gc
	 * 
	 * @param gc
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawArrow(GraphicsContext gc, int x1, int y1, int x2, int y2, int speed) {
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

	/**
	 * Function to draw all Nodes and Ways
	 */
	private void draw() {
		getNodes();
		getWays();
	}

	/**
	 * Calls the Parser and gives the Parser all Information
	 */
	private void callParser() {
		try {
			parser = new OSMParser(content);
			try {
				content = parser.parse();
				clearCanvas();
				draw();
			} catch (NullPointerException | InvalidAPIRequestException e) {
				e.printStackTrace();
			}
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Kritischer Fehler");
			alert.setHeaderText(null);
			alert.setContentText("Bitte Programm neustarten");
			alert.showAndWait();
		}
	}
}
