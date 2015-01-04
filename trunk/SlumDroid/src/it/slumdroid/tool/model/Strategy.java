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

package it.slumdroid.tool.model;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.components.exploration.CompositionalComparator;

// TODO: Auto-generated Javadoc
/**
 * The Interface Strategy.
 */
public interface Strategy {

	/**
	 * Adds the state.
	 *
	 * @param newActivity the new activity
	 */
	public void addState (ActivityState newActivity);
	
	/**
	 * Gets the comparator.
	 *
	 * @return the comparator
	 */
	public CompositionalComparator getComparator ();
	
	/**
	 * Sets the comparator.
	 *
	 * @param comparator the new comparator
	 */
	public void setComparator (CompositionalComparator comparator);
	
	/**
	 * Compare state.
	 *
	 * @param theActivity the the activity
	 */
	public void compareState (ActivityState theActivity);
	
	/**
	 * Check for exploration.
	 *
	 * @return true, if successful
	 */
	public boolean checkForExploration ();
	
	/**
	 * Sets the task.
	 *
	 * @param theTask the new task
	 */
	public void setTask (Task theTask);
	
	/**
	 * Gets the task.
	 *
	 * @return the task
	 */
	public Task getTask ();

}
