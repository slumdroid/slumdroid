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
 * Copyright (C) 2014 Gennaro Imparato
 */

package it.slumdroid.androidtest.graphviz;

import it.slumdroid.droidmodels.model.ActivityState;

public class Node {

	private String id = new String();
	private String label = new String();
	private String image = new String();
	private String name = new String();

	public Node (String id) {
		setId(id);
	}

	public Node (ActivityState s) {
		this (s.getUniqueId());
		setImage (s.getScreenshot());
		setName (s.getName());
		setLabel (s.getId());
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean hasImage() {
		return (!getImage().equals(""));
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return  getId();
	}

}
