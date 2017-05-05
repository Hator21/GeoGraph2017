package de.fh_bielefeld.geograph.GUI;
import de.fh_bielefeld.geograph.GUI.AVLTree.AVLNode;
import de.fh_bielefeld.geograph.GUI_INTERFACE.*;

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

	public AVLTreeInterface<MapNode> getNodes() {
		return nodes;
	}

	public void setNodes(AVLTreeInterface<MapNode> nodes) {
		this.nodes = nodes;
	}

	public AVLTreeInterface<MapWay> getWays() {
		return ways;
	}

	public void setWays(AVLTreeInterface<MapWay> ways) {
		this.ways = ways;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public OSMStreetGUIController getController() {
		return controller;
	}

	public void setController(OSMStreetGUIController controller) {
		this.controller = controller;
	}

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
