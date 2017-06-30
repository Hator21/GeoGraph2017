package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

import org.w3c.dom.Document;

import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;

/**
 * This class holds the complete data for the GUI.
 */
public class ContentHolder implements ContentHolderInterface {
	private ArrayList<MapNode>		nodes;
	private ArrayList<MapWay>		ways;
	private double					latitude, minLatitude, maxLatitude;
	private double					longitude, minLongitude, maxLongitude;
	private OSMStreetGUIController	controller;
	private Document                document;

	public ContentHolder(OSMStreetGUIController controller) {
		nodes = new ArrayList<MapNode>();
		ways = new ArrayList<MapWay>();
		latitude = 0;
		longitude = 0;
		minLatitude = 0;
		maxLatitude = 0;
		minLongitude = 0;
		maxLongitude = 0;
		this.controller = controller;
	}

	/**
	 * Getter for the Nodes on the map.
	 * 
	 * @return Nodes on the map.
	 */
	public ArrayList<MapNode> getNodes() {
		return nodes;
	}

	/**
	 * Setter for the Nodes on the map.
	 * 
	 * @param Nodes
	 *            on the map.
	 */
	public void setNodes(ArrayList<MapNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Getter for the ways on the map.
	 * 
	 * @return Ways on the map.
	 */
	public ArrayList<MapWay> getWays() {
		return ways;
	}

	/**
	 * Setter for the ways on the map.
	 * 
	 * @param Ways
	 *            on the map.
	 */
	public void setWays(ArrayList<MapWay> ways) {
		this.ways = ways;
	}

	/**
	 * Getter for the Latitude value. TODO welcher Latitude Wert.
	 * Hintergrund: Die Felder in der GUI werden noch angepasst, bzw. ein paar hinzugefügt.
	 * 
	 * @return Latitude value.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Setter for the Latitude value. TODO welcher Latitude Wert.
	 * 
	 * @return Latitude value.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Getter for the Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @return Longitude value.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Setter for the Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @param Longitude
	 *            value.
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Getter for the minimal Latitude value. TODO welcher Latitude Wert.
	 * 
	 * @return Latitude value.
	 */
	public double getMinLatitude() {
		return minLatitude;
	}

	/**
	 * Setter for the minimal Latitude value. TODO welcher Latitude Wert.
	 * 
	 * @param Latitude
	 *            value.
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * Getter for the maximum Latitude value. TODO welcher Latitude Wert.
	 * 
	 * @return Latitude value.
	 */
	public double getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * Getter for the maximum Latitude value. TODO welcher Latitude Wert.
	 * 
	 * @param Latitude
	 *            value.
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * Getter for the minimal Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @return Longitude value.
	 */
	public double getMinLongitude() {
		return minLongitude;
	}

	/**
	 * Setter for the minimal Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @param Longitude
	 *            value.
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * Getter for the maximum Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @return Longitude value.
	 */
	public double getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * Setter for the maximum Longitude value. TODO welcher Longitude Wert.
	 * 
	 * @param Longitude
	 *            value.
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	/**
	 * Getter for the OSMStreetGUI-Controller.
	 * 
	 * @return Controller from the OSMStreetGUI.
	 */
	public OSMStreetGUIController getController() {
		return controller;
	}

	/**
	 * Setter for the OSMStreetGUI-Controller.
	 * 
	 * @param Controller
	 *            from the OSMStreetGUI.
	 */
	public void setController(OSMStreetGUIController controller) {
		this.controller = controller;
	}

    @Override
    public void setDocument(Document doc) {
        this.document = doc;
    }

    @Override
    public Document getDocument() {
        return this.document;
    }
}
