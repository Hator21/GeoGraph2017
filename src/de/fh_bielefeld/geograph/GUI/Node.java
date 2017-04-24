package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

public class Node {

	String			id;
	double			latitude;
	double			longitude;
	ArrayList<Tag>	tagList;

	public Node(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tagList = null;
	}

	public Node(String id, double latitude, double longitude, ArrayList<Tag> tagList) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tagList = tagList;
	}
}
