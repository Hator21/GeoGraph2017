package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

public class MapNode implements Comparable<MapNode> {

	String			id;
	double			latitude;
	double			longitude;
	ArrayList<MapTag>	tagList;

	public MapNode(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tagList = null;
	}

	public MapNode(String id, double latitude, double longitude, ArrayList<MapTag> tagList) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tagList = tagList;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the tagList
	 */
	public ArrayList<MapTag> getTagList() {
		return tagList;
	}

	/**
	 * @param tagList
	 *            the tagList to set
	 */
	public void setTagList(ArrayList<MapTag> tagList) {
		this.tagList = tagList;
	}

	@Override
	public int compareTo(MapNode o) {
		return o.id.compareTo(this.id);
	}

}
