package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;

public class MapWay implements Comparable<MapWay>, MapWayInterface {
	String				id;
	ArrayList<MapNode>	refList;
	ArrayList<MapTag>	tagList;

	public MapWay(String id, ArrayList<MapNode> refList, ArrayList<MapTag> tagList) {
		this.id = id;
		this.refList = refList;
		this.tagList = tagList;
	}

	public MapWay(String id, ArrayList<MapNode> refList) {
		this.id = id;
		this.refList = refList;
		this.tagList = null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<MapNode> getRefList() {
		return refList;
	}

	public void setRefList(ArrayList<MapNode> refList) {
		this.refList = refList;
	}

	public ArrayList<MapTag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<MapTag> tagList) {
		this.tagList = tagList;
	}

	@Override
	public int compareTo(MapWay o) {
		return o.id.compareTo(this.id);
	}

}
