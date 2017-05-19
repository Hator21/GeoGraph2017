package de.fh_bielefeld.geograph.GUI;
import de.fh_bielefeld.geograph.GUI.AVLTree.AVLNode;
import de.fh_bielefeld.geograph.GUI_INTERFACE.*;

/**
 * TODO
 * 
 * @param 
 * 
 * @return 
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
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public AVLTreeInterface<MapNode> getNodes() {
		return nodes;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setNodes(AVLTreeInterface<MapNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public AVLTreeInterface<MapWay> getWays() {
		return ways;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setWays(AVLTreeInterface<MapWay> ways) {
		this.ways = ways;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public double getMinLatitude() {
		return minLatitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public double getMinLongitude() {
		return minLongitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public double getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public OSMStreetGUIController getController() {
		return controller;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public void setController(OSMStreetGUIController controller) {
		this.controller = controller;
	}

	/**
	 * TODO
	 * 
	 * @param 
	 * 
	 * @return 
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
