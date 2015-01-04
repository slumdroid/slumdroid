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

import it.slumdroid.droidmodels.model.ActivityState;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node {

	/** The id. */
	private String id = new String();
	
	/** The label. */
	private String label = new String();
	
	/** The image. */
	private String image = new String();
	
	/** The name. */
	private String name = new String();

	/**
	 * Instantiates a new node.
	 *
	 * @param id the id
	 */
	public Node (String id) {
		setId(id);
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param s the s
	 */
	public Node (ActivityState s) {
		this (s.getUniqueId());
		setImage (s.getScreenshot());
		setName (s.getName());
		setLabel (s.getId());
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
	 * Checks for image.
	 *
	 * @return true, if successful
	 */
	public boolean hasImage() {
		return (!getImage().equals(""));
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return  getId();
	}

}
