package de.fh_bielefeld.geograph.GUI_INTERFACE;

import java.util.ArrayList;

import de.fh_bielefeld.geograph.GUI.MapNode;
import de.fh_bielefeld.geograph.GUI.MapTag;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public interface MapWayInterface {

	/**
	 * @return the id
	 */
	
	String getId();

	/**
	 * @param id
	 *            the id to set
	 */
	
	void setId(String id);

	/**
	 * @return the refList
	 */
	
	ArrayList<MapNode> getRefList();

	/**
	 * @param refList@Override
	 *            the refList to set
	 */
	
	void setRefList(ArrayList<MapNode> refList);

	/**
	 * @return the tagList
	 */
	
	ArrayList<MapTag> getTagList();

	/**
	 * @param tagList
	 *            the tagList to set
	 */
	
	void setTagList(ArrayList<MapTag> tagList);

}