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

package it.slumdroid.tool.components.planner;

import it.slumdroid.droidmodels.model.Transition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Plan.
 */
public class Plan implements Iterable<Transition> {

	/** The transitions. */
	private List<Transition> transitions = new ArrayList<Transition> ();

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Transition> iterator() {
		return this.transitions.iterator();
	}

	/**
	 * Adds the transition.
	 *
	 * @param transition the transition
	 * @return true, if successful
	 */
	public boolean addTransition (Transition transition) {
		return this.transitions.add(transition);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size () {
		return this.transitions.size();
	}

	/**
	 * Gets the transition.
	 *
	 * @param id the id
	 * @return the transition
	 */
	public Transition getTransition (int id) {
		return this.transitions.get(id);
	}

	/**
	 * Removes the transition.
	 *
	 * @param id the id
	 */
	public void removeTransition (int id) {
		this.transitions.remove(id);
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty () {
		return size() == 0;
	}

}
