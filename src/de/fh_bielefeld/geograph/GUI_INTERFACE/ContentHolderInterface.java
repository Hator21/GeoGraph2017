package de.fh_bielefeld.geograph.GUI_INTERFACE;

import java.util.ArrayList;

import org.w3c.dom.Document;

import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI.MapWay;
import de.fh_bielefeld.geograph.GUI.OSMStreetGUIController;

/**
 * Interface for the ContentHolder class.
 * This class holds the complete data for the GUI.
 * 
 * TODO: Siehe Info in der ContentHolder class
 */
public interface ContentHolderInterface {

	/**
	 * @return
	 */
	ArrayList<MapNode> getNodes();

	/**
	 * @param nodes
	 */
	void setNodes(ArrayList<MapNode> nodes);

	/**
	 * @return
	 */
	ArrayList<MapWay> getWays();

	/**
	 * @param ways
	 */
	void setWays(ArrayList<MapWay> ways);

	/**
	 * @return
	 */
	double getLatitude();

	/**
	 * @param latitude
	 */
	void setLatitude(double latitude);

	/**
	 * @return
	 */
	double getLongitude();

	/**
	 * @param longitude
	 */
	void setLongitude(double longitude);

	/**
	 * @return
	 */
	double getMinLatitude();

	/**
	 * @param minLatitude
	 */
	void setMinLatitude(double minLatitude);

	/**
	 * @return
	 */
	double getMaxLatitude();

	/**
	 * @param maxLatitude
	 */
	void setMaxLatitude(double maxLatitude);

	/**
	 * @return
	 */
	double getMinLongitude();

	/**
	 * @param minLongitude
	 */
	void setMinLongitude(double minLongitude);

	/**
	 * @return
	 */
	double getMaxLongitude();

	/**
	 * @param maxLongitude
	 */
	void setMaxLongitude(double maxLongitude);

	/**
	 * @return
	 */
	OSMStreetGUIController getController();

	/**
	 * @param controller
	 */
	void setController(OSMStreetGUIController controller);
	
	/**
	 * Sets the next node to the give node.
	 * 
	 * @param searchLat Latitude value from point to search from.
	 * @param searchLon Longitude value from point to search from.
	 */
	void setNextNode(double searchLat, double searchLon);
	
	/**
	 * Returns the next node to the give node.
	 * @return node Next node to the given node.
	 */
	MapNode getNextNode();
	
	/**
	 * Clears the next node.
	 * 
	 */
	void clearNextNode();
  
  /**
   * @param doc
   */
  void setDocument(Document doc);
    
  /**
   * @return Document
   */
  Document getDocument();
}
