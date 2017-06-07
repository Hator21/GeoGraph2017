package de.fh_bielefeld.geograph.GUI_INTERFACE;


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
	 * @param x
	 * @return
	 */

	boolean contains(T x);

}
