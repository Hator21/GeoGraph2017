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
}
