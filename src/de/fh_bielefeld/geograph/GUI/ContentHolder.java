package de.fh_bielefeld.geograph.GUI;

import de.fh_bielefeld.geograph.GUI.AVLTree.AVLNode;
import de.fh_bielefeld.geograph.GUI_INTERFACE.*;

/**
 * This class holds the complete data for the GUI.
 */
public class ContentHolder implements ContentHolderInterface {
	private AVLTreeInterface<MapNode>		nodes;
	private AVLTreeInterface<MapWay>			ways;
	private double					latitude, minLatitude, maxLatitude;
	private double					longitude, minLongitude, maxLongitude;
	private OSMStreetGUIController	controller;

	public ContentHolder(OSMStreetGUIController controller) {
		nodes = new AVLTree<MapNode>(this);
		ways = new AVLTree<MapWay>(this);
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
	public AVLTreeInterface<MapNode> getNodes() {
		return nodes;
	}

	/**
	 * Setter for the Nodes on the map.
	 * 
	 * @param Nodes on the map.
	 */
	public void setNodes(AVLTreeInterface<MapNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Getter for the ways on the map.
	 * 
	 * @return Ways on the map.
	 */
	public AVLTreeInterface<MapWay> getWays() {
		return ways;
	}

	/**
	 * Setter for the ways on the map.
	 * 
	 * @param Ways on the map.
	 */
	public void setWays(AVLTreeInterface<MapWay> ways) {
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
	 * @param Longitude value.
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
	 * @param Latitude value.
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
	 * @param Latitude value.
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
	 * @param Longitude value.
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
	 * @param Longitude value.
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
	 * @param Controller from the OSMStreetGUI.
	 */
	public void setController(OSMStreetGUIController controller) {
		this.controller = controller;
	}

	/**
	 * Creates the Nodes on the gui. For that it calls the function drawNode().
	 * 
	 * @param Node to draw on the gui.
	 */
	public void sendData(AVLNode t) {
		try {

			MapNodeInterface node = ((MapNodeInterface) (t.element));
			controller.drawNode(node);
		} catch (ClassCastException cceN) {
			try {
				MapWayInterface way = ((MapWayInterface) (t.element));

				controller.drawWay(way);
			} catch (ClassCastException cceW) {
				System.out.println("Something wrong with casting");
			}
		}
	}

}
