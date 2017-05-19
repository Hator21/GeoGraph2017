package de.fh_bielefeld.geograph.GUI_INTERFACE;

import de.fh_bielefeld.geograph.GUI.AVLTree.AVLNode;
import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI.MapWay;
import de.fh_bielefeld.geograph.GUI.OSMStreetGUIController;
import de.fh_bielefeld.geograph.GUI_INTERFACE.AVLTreeInterface;

public interface ContentHolderInterface {
	
	/**
	 * 
	 * @return
	 */
	
	AVLTreeInterface<MapNode> getNodes();

	/**
	 * @param nodes
	 */
	
	void setNodes(AVLTreeInterface<MapNode> nodes);

	/**
	 * @return
	 */
	
	AVLTreeInterface<MapWay> getWays();

	/**
	 * @param ways
	 */
	
	void setWays(AVLTreeInterface<MapWay> ways);

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
	 * @param t
	 */
	
	void sendData(AVLNode t);

}