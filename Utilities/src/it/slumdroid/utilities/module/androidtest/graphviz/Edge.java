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

package it.slumdroid.utilities.module.androidtest.graphviz;

// TODO: Auto-generated Javadoc
/**
 * The Class Edge.
 */
public class Edge {

	/** The from. */
	private Node from;
	
	/** The to. */
	private Node to;
	
	/** The id. */
	private String id = new String();
	
	/** The label. */
	private String label = new String();

	/**
	 * Instantiates a new edge.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public Edge (Node from, Node to) {
		setNodes(from, to);
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public Node getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(Node from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public Node getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(Node to) {
		this.to = to;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public void setNodes (Node from, Node to) {
		setFrom(from);
		setTo(to);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return from.getId() + " -> " + to.getId(); 
	}

}
