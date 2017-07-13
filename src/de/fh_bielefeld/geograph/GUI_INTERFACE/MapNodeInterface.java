package de.fh_bielefeld.geograph.GUI_INTERFACE;

import java.util.ArrayList;

import de.fh_bielefeld.geograph.GUI.MapTag;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public interface MapNodeInterface {

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
	 * @return the latitude
	 */
	
	double getLatitude();

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	
	void setLatitude(double latitude);

	/**
	 * @return the longitude
	 */
	
	double getLongitude();

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	
	void setLongitude(double longitude);

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