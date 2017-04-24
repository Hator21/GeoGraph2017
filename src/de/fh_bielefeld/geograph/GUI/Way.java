package de.fh_bielefeld.geograph.GUI;

import java.util.ArrayList;

public class Way {
	String			id;
	ArrayList<Node>	refList;
	ArrayList<Tag>	tagList;

	public Way(String id, ArrayList<Node> refList, ArrayList<Tag> tagList) {
		this.id = id;
		this.refList = refList;
		this.tagList = tagList;
	}

	public Way(String id, ArrayList<Node> refList) {
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
	public ArrayList<Node> getRefList() {
		return refList;
	}

	/**
	 * @param refList
	 *            the refList to set
	 */
	public void setRefList(ArrayList<Node> refList) {
		this.refList = refList;
	}

	/**
	 * @return the tagList
	 */
	public ArrayList<Tag> getTagList() {
		return tagList;
	}

	/**
	 * @param tagList
	 *            the tagList to set
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}

}
