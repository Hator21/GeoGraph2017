package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;


import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;

public class MapNode implements Comparable<MapNode>, MapNodeInterface {


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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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


	public ArrayList<MapTag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<MapTag> tagList) {
		this.tagList = tagList;
	}

	@Override
	public int compareTo(MapNode o) {
		return o.id.compareTo(this.id);
	}

}
