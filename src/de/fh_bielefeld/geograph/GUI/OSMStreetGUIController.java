package de.fh_bielefeld.geograph.GUI;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import de.fh_bielefeld.geograph.API.Exception.InvalidAPIRequestException;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;
import de.fh_bielefeld.geograph.PARSER.OSMParser;

public class OSMStreetGUIController {

	private OSMParser				parser;

	@FXML private Button			searchButtonArea, searchButtonRadius, fileChooserButton, fileSaveButton;

	@FXML private TextField			latitudeTextFieldL, longitudeTextFieldL, latitudeTextFieldR, longitudeTextFieldR, radiusLatitude, radiusLongitude;

	@FXML private Label				requestTimeLabel;
	
	@FXML private Slider			zoomSlider;

	@FXML private Canvas			paintingCanvas;

	@FXML private AnchorPane		rightAnchor;
	
	@FXML private Tab				TabRadius;

	private GraphicsContext			gc;

	private ContentHolderInterface	content				= new ContentHolder(this);

	private final int				NODERADIUS			= 3;
	private final int				ARR_SIZE			= 5;
	private static final double		MAXRANGELAT			= 0.25;
	private static final double		MAXRANGELON			= 0.25;

	private double					zoomFactor			= 0;
	private double					pressedX			= 0;
	private double					pressedY			= 0;
	private double					resultX				= 0;
	private double					resultY				= 0;
	private double					draggedX			= 0;
	private double					draggedY			= 0;
	private double					offsetY				= 0;
	private double					offsetX				= 0;

	ChangeListener<Number>			stageSizeListener	= (observable, oldValue, newValue) -> this.resize();
	private boolean					firstcall			= true;
	private boolean					centerNextNode		= false;

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
		
		requestTimeLabel.setText("");
		
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
						if (latitudeR - latitudeL <= MAXRANGELAT) {
							if (longitudeR - longitudeL <= MAXRANGELON) {
								content.setMinLatitude(latitudeL);
								content.setMinLongitude(longitudeL);
								content.setMaxLatitude(latitudeR);
								content.setMaxLongitude(longitudeR);
								
								if(!firstcall){ //Clear everything
									offsetX = 0;
									offsetY = 0;
									draggedX = 0;
									draggedY = 0;
									resultX = 0;
									resultY = 0;
									pressedX = 0;
									pressedY = 0;
									zoomSlider.setValue(zoomSlider.getMin());
									zoomFactor = zoomSlider.getMin();
									content.clearNextNode();
								}
								
								
								long time = System.currentTimeMillis();
								callParser();
								time = System.currentTimeMillis() - time;
								
								if(time > 1000.0){
									requestTimeLabel.setText("Abfragezeit: " + time/1000.0 + " s");
								}else{
									requestTimeLabel.setText("Abfragezeit: " + time + " ms");
								}
								TabRadius.setDisable(false);
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
			fileSaveButton.setDisable(false);
		});
		
		searchButtonRadius.setOnAction((event) -> {
			boolean ok = true;
			double latitude = 0;
			double longitude = 0;
			try {
				latitude = Double.parseDouble(radiusLatitude.getText());

			} catch (NumberFormatException nbe) {
				popUp("Breitengrad Rechts");
				latitudeTextFieldR.setText("");
				ok = false;
			}
			try {
				longitude = Double.parseDouble(radiusLongitude.getText());
			} catch (NumberFormatException nbe) {
				popUp("Laengengrad Rechts");
				longitudeTextFieldR.setText("");
				ok = false;
			}
			if (ok) {
				if (latitude >= Double.parseDouble(latitudeTextFieldL.getText()) && latitude <= Double.parseDouble(latitudeTextFieldR.getText())) {
					if (longitude >= Double.parseDouble(longitudeTextFieldL.getText()) && longitude <= Double.parseDouble(longitudeTextFieldR.getText())) {

						content.setNextNode(latitude, longitude);
						centerNextNode = true;
						draw();
						
					} else {
						popUpNotInRange();
					}
				} else {
					popUpNotInRange();
				}
			}
		});

		fileChooserButton.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Datei laden");
            File chooserFile = fileChooser.showOpenDialog(fileChooserButton.getScene().getWindow());

            if (chooserFile != null) {
                loadFile(chooserFile);
            }
        });

        fileSaveButton.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Datei speichern");
            File chooserFile = fileChooser.showSaveDialog(fileSaveButton.getScene().getWindow());

            if (chooserFile != null) {
                saveToFile(chooserFile);
            }
        });

		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			zoomFactor = zoomSlider.getValue();
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
				draw();
			}
		});
	}

	/**
	 * Resizes the Map, when changes Window-Bounds
	 */
	private void resize() {
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
		double latitudeN = (latitude) - (middleY - latitude) * zoomFactor + resultY + offsetY;
		double longitudeN = (longitude) + (longitude - middleX) * zoomFactor + resultX + offsetX;
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
				draw();
			}
		}
	}
	
	/**
	 * Draws the next Node on the MapCanvas
	 * 
	 * @param node
	 */
	public void drawNextNode(MapNodeInterface node) {
		Point c = getNodeCoords(node);
		double latitude = c.getY();
		double longitude = c.getX();
		if (0 <= latitude && latitude <= paintingCanvas.getHeight() && 0 <= longitude && longitude <= paintingCanvas.getWidth()) {
			gc.setStroke(Color.BLACK);
			gc.strokeOval(longitude + 1 -NODERADIUS, latitude + 1 -NODERADIUS, NODERADIUS * 4, NODERADIUS * 4);
			gc.setFill(Color.GREEN);
			gc.fillOval(longitude + 2 -NODERADIUS, latitude + 2 -NODERADIUS, NODERADIUS * 4 - 2, NODERADIUS * 4 - 2);
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
		alert.setContentText("Abstand zwischen Latitude Links und Latitude Rechts ist groesser als 0.25");
		alert.showAndWait();
	}

	/**
	 * Generates a PopUp, that pops up when lower longitude is bigger higher longitude
	 */
	private void popUpLongToBig() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Abstand zwischen Longitude Links und Longitude Rechts ist groesser als 0.25");
		alert.showAndWait();
	}
	
	/**
	 * Generates a PopUp, that pops up when user input is not in range of search values
	 */
	private void popUpNotInRange() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Falsche Eingabe");
		alert.setHeaderText(null);
		alert.setContentText("Ihre Eingabe befindet sich ausserhalb des Suchbereiches.");
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

	/**
	 * Function to draw all Nodes and Ways
	 */
	private void draw() {
		clearCanvas();
		
		MapNodeInterface node = content.getNextNode();
		if(node != null){
			if(centerNextNode == true){
				offsetX = 0;
				offsetY = 0;
				Point p = getNodeCoords(content.getNextNode());
				offsetX = (paintingCanvas.getWidth()/2 - p.getX());
				offsetY = (paintingCanvas.getHeight()/2 - p.getY());
			}
			drawNextNode(node);
		}
		getNodes();
		getWays();
		centerNextNode = false;
	}

	/**
	 * Calls the Parser and gives the Parser all Information
	 */
	private void callParser() {
		try {
			parser = new OSMParser(content);
			try {
				content = parser.parse();
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
	
    private void loadFile(File file) {
        Document doc;
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.parse(file);
            
            double maxLong = Double.parseDouble(doc.getElementsByTagName("bounds").item(0).getAttributes().getNamedItem("maxlon").getNodeValue());
            double maxLat = Double.parseDouble(doc.getElementsByTagName("bounds").item(0).getAttributes().getNamedItem("maxlat").getNodeValue());
            double minLong = Double.parseDouble(doc.getElementsByTagName("bounds").item(0).getAttributes().getNamedItem("minlon").getNodeValue());
            double minLat = Double.parseDouble(doc.getElementsByTagName("bounds").item(0).getAttributes().getNamedItem("minlat").getNodeValue());
            
            content.setMinLatitude(minLat);
            content.setMaxLatitude(maxLat);
            content.setMinLongitude(minLong);
            content.setMaxLongitude(maxLong);
            
            content.setDocument(doc);
            callParser();
            fileSaveButton.setDisable(true);
        } catch (SAXException | IOException | NullPointerException | ParserConfigurationException e) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Kritischer Fehler");
            alert.setHeaderText("Die Datei konnte nicht geladen werden");
            //alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    private void saveToFile(File file) {
        if (parser != null && file != null) {
            Document currentD = parser.getCurrentDocument();

            Transformer transformer;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(file);
                Source input = new DOMSource(currentD);
                transformer.transform(input, output);
            } catch (TransformerException | TransformerFactoryConfigurationError e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Kritischer Fehler");
                alert.setHeaderText(null);
                alert.setContentText("Die Datei konnte nicht gespeichert werden");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Kritischer Fehler");
            alert.setHeaderText(null);
            alert.setContentText("Die Datei konnte nicht gespeichert werden");
            alert.showAndWait();
        }
    }
}
