package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

import de.fh_bielefeld.geograph.GUI_INTERFACE.MapWayInterface;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public class MapWay implements Comparable<MapWay>, MapWayInterface {
	String				id;
	ArrayList<String>	refList;
	ArrayList<MapTag>	tagList;

	/**
	 * Contructor for creating a MapWay
	 * 
	 * @param id
	 *            Identificationnumber
	 * @param refList
	 *            List with all Node IDs on the way
	 * @param tagList
	 *            Tags, the Way has
	 */
	public MapWay(String id, ArrayList<String> refList, ArrayList<MapTag> tagList) {

		this.id = id;
		this.refList = refList;
		this.tagList = tagList;
	}

	/**
	 * Contructor for creating a MapWay
	 * 
	 * @param id
	 *            Identificationnumber
	 * @param refList
	 *            List with all Node IDs on the way
	 */
	public MapWay(String id, ArrayList<String> refList) {

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

	public ArrayList<String> getRefList() {
		return refList;
	}

	public void setRefList(ArrayList<String> refList) {
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
