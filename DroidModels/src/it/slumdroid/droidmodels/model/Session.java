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
 * The Interface Session.
 */
public interface Session extends Iterable<Task> {

	/**
	 * Gets the date time.
	 *
	 * @return the date time
	 */
	public String getDateTime();

	/**
	 * Tasks.
	 *
	 * @return the iterator
	 */
	public Iterator<Task> tasks();

	/**
	 * Gets the base activity.
	 *
	 * @return the base activity
	 */
	public ActivityState getBaseActivity();

	/**
	 * Adds the task.
	 *
	 * @param theTask the the task
	 */
	public void addTask(Task theTask);

	/**
	 * Removes the task.
	 *
	 * @param theTask the the task
	 */
	public void removeTask(Task theTask);

	/**
	 * Adds the failed task.
	 *
	 * @param theTask the the task
	 */
	public void addFailedTask(Task theTask);

	/**
	 * Adds the crashed task.
	 *
	 * @param theTask the the task
	 */
	public void addCrashedTask(Task theTask);

	/**
	 * Parses the.
	 *
	 * @param xml the xml
	 */
	public void parse(String xml);

}
