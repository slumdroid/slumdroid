/* This file is part of SlumDroid <https://code.google.com/p/slumdroid/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 * 
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.droidmodels.xml;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeListIterator.
 */
public class NodeListIterator implements Iterator<Element>, Iterable<Element> {

	/** The items. */
	private NodeList items = null;

	/** The current item. */
	private int currentItem;

	/**
	 * Instantiates a new node list iterator.
	 *
	 * @param element the element
	 */
	public NodeListIterator (Element element) {
		this (element.getChildNodes());
	}

	/**
	 * Instantiates a new node list iterator.
	 *
	 * @param list the list
	 */
	public NodeListIterator(NodeList list) {
		setItems(list);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Element> iterator() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		int item = this.getItems().getLength();
		return this.currentItem < item;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Element next() {
		Element trace = (Element) this.getItems().item(this.currentItem);
		this.currentItem++;
		return trace;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// Doesn't actually remove anything
		this.currentItem++;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	private NodeList getItems() {
		if (this.items instanceof NodeList)
			return this.items;
		return null;
	}

	/**
	 * Sets the items.
	 *
	 * @param list the new items
	 */
	private void setItems (NodeList list) {
		this.items = list;
		this.currentItem = 0;
	}

}
