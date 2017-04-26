package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

public class MapWay implements Comparable<MapWay> {
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
	 * @return the refList
	 */
	public ArrayList<MapNode> getRefList() {
		return refList;
	}

	/**
	 * @param refList
	 *            the refList to set
	 */
	public void setRefList(ArrayList<MapNode> refList) {
		this.refList = refList;
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
	public int compareTo(MapWay o) {
		return o.id.compareTo(this.id);
	}

}
