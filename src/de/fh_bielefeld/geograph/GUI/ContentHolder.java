package de.fh_bielefeld.geograph.GUI;

public class ContentHolder {
	private AVLTree<MapNode>	nodes;
	private AVLTree<MapWay>		ways;
	private double				latitude, minLatitude, maxLatitude;
	private double				longitude, minLongitude, maxLongitude;

	public ContentHolder() {
		nodes = new AVLTree<MapNode>();
		ways = new AVLTree<MapWay>();
		latitude = 0;
		longitude = 0;
		minLatitude = 0;
		maxLatitude = 0;
		minLongitude = 0;
		maxLongitude = 0;
	}

	/**
	 * @return the nodes
	 */
	public AVLTree<MapNode> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(AVLTree<MapNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the ways
	 */
	public AVLTree<MapWay> getWays() {
		return ways;
	}

	/**
	 * @param ways
	 *            the ways to set
	 */
	public void setWays(AVLTree<MapWay> ways) {
		this.ways = ways;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the minLatitude
	 */
	public double getMinLatitude() {
		return minLatitude;
	}

	/**
	 * @param minLatitude
	 *            the minLatitude to set
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * @return the maxLatitude
	 */
	public double getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * @param maxLatitude
	 *            the maxLatitude to set
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * @return the minLongitude
	 */
	public double getMinLongitude() {
		return minLongitude;
	}

	/**
	 * @param minLongitude
	 *            the minLongitude to set
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * @return the maxLongitude
	 */
	public double getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * @param maxLongitude
	 *            the maxLongitude to set
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

}
