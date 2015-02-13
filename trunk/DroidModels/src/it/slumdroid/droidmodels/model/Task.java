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

package it.slumdroid.droidmodels.model;

import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Interface Task.
 */
public interface Task extends Iterable<Transition>, WrapperInterface {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId ();

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId (String id);

	/**
	 * Transitions.
	 *
	 * @return the iterator
	 */
	public Iterator<Transition> transitions();

	/**
	 * Adds the transition.
	 *
	 * @param theTransition the the transition
	 */
	public void addTransition(Transition theTransition);

	/**
	 * Sets the final activity.
	 *
	 * @param theState the new final activity
	 */
	public void setFinalActivity(ActivityState theState);

	/**
	 * Gets the final transition.
	 *
	 * @return the final transition
	 */
	public Transition getFinalTransition ();

	/**
	 * Checks if is failed.
	 *
	 * @return true, if is failed
	 */
	public boolean isFailed();

	/**
	 * Sets the failed.
	 *
	 * @param failure the new failed
	 */
	public void setFailed (boolean failure);

}
