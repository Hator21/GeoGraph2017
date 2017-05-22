package de.fh_bielefeld.geograph.GUI;

import de.fh_bielefeld.geograph.GUI_INTERFACE.MapTagInterface;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public class MapTag implements MapTagInterface {
	
	String	key;
	String	value;

	public MapTag(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
