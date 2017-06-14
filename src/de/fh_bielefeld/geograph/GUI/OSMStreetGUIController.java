package de.fh_bielefeld.geograph.GUI;

import java.awt.Point;

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
import de.fh_bielefeld.geograph.PARSER.OmlParser;

public class OSMStreetGUIController {

	private OmlParser				parser;

	@FXML private Button			/* searchButton, */searchButtonArea/* , fileChooserButton */;

	@FXML private TextField			latitudeTextField, longitudeTextField, latitudeTextFieldL, longitudeTextFieldL, latitudeTextFieldR, longitudeTextFieldR;

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

	ChangeListener<Number>			stageSizeListener	= (observable, oldValue, newValue) -> this.resize();
	private boolean firstcall = true;

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
			zoomFactor = zoomSlider.getValue();
			clearCanvas();
			draw();
		});
		
		paintingCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				pressedX = e.getX();
                pressedY = e.getY();
                }	
			});
		
		paintingCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
			       new EventHandler<MouseEvent>() {
			           @Override
			           public void handle(MouseEvent e) {
			        	   pressedX = e.getX()- 300;
			               pressedY = e.getY()- 300;			               
			        	   gc.clearRect(0, 0, paintingCanvas.getWidth(), paintingCanvas.getHeight());
			        	   draw();
			           }
			       });

	}

	private void resize() {
		clearCanvas();
		paintingCanvas.setWidth(rightAnchor.getWidth() - 14.0);
		paintingCanvas.setHeight(rightAnchor.getHeight() - 14.0);
		draw();
	}

	private void clearCanvas() {
		gc.clearRect(0, 0, paintingCanvas.getWidth(), paintingCanvas.getHeight());
	}

	private Point getNodeCoords(MapNodeInterface node) {
		double latitude = (mapLatitude(node.getLatitude()) - NODERADIUS);
		double longitude = (mapLongitude(node.getLongitude()) - NODERADIUS);
		double middleX = paintingCanvas.getWidth() / 2;
		double middleY = paintingCanvas.getHeight() / 2;
		double latitudeN = (latitude) - (middleY - latitude) * zoomFactor + pressedY;
		double longitudeN = (longitude) + (longitude - middleX) * zoomFactor + pressedX;
		return new Point((int) longitudeN, (int) latitudeN);
	}

	private void getNodes() {
		for (MapNodeInterface node : content.getNodes()) {
			drawNode(node);
		}
	}

	public void drawNode(MapNodeInterface node) {
		Point c = getNodeCoords(node);
		double latitude = c.getY();
		double longitude = c.getX();
		if (0 <= latitude && latitude <= paintingCanvas.getHeight() && 0 <= longitude && longitude <= paintingCanvas.getWidth()) {
			gc.setStroke(Color.BLACK);
			gc.strokeOval(longitude + 1, latitude + 1, NODERADIUS * 2, NODERADIUS * 2);
			gc.setFill(Color.RED);
			gc.fillOval(longitude + 2, latitude + 2, NODERADIUS * 2 - 2, NODERADIUS * 2 - 2);
		}
		else{
			if(firstcall){
				zoomSlider.setValue(zoomSlider.getValue()-0.025);
				clearCanvas();
				draw();
			}
		}
	}

	private void getWays() {
		for (MapWayInterface way : content.getWays()) {
			drawWay(way);
		}
	}

	public void drawWay(MapWayInterface way) {
		firstcall = false;
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
			clearCanvas();
			draw();
		} catch (NullPointerException | InvalidAPIRequestException e) {
			e.printStackTrace();
		}
	}
}
