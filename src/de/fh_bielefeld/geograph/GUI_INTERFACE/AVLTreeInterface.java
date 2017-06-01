package de.fh_bielefeld.geograph.GUI_INTERFACE;

import de.fh_bielefeld.geograph.GUI.AVLTree.AVLNode;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public interface AVLTreeInterface<T extends Comparable<? super T>> {

	/**
	 * 
	 * @param x
	 * @return
	 */

	boolean insert(T x);

	/**
	 * 
	 */

	void sendContent();

	/*
	 * 
	 */

	public AVLNode getNodeByElement(String id);

	/**
	 * 
	 * @param x
	 * @return
	 */

	boolean contains(T x);

}
