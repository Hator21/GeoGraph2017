package de.fh_bielefeld.geograph.GUI_INTERFACE;

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
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	
	boolean contains(T x);
	
}