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

import it.slumdroid.droidmodels.model.Task;

import java.util.Collection;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface TaskScheduler.
 */
public interface TaskScheduler {

	/**
	 * Next task.
	 *
	 * @return the task
	 */
	public Task nextTask();

	/**
	 * Adds the tasks.
	 *
	 * @param newTasks the new tasks
	 */
	public void addTasks(Collection<Task> newTasks);

	/**
	 * Adds the planned tasks.
	 *
	 * @param newTasks the new tasks
	 */
	public void addPlannedTasks(List<Task> newTasks);

	/**
	 * Sets the task list.
	 *
	 * @param theList the new task list
	 */
	public void setTaskList(List<Task> theList);

	/**
	 * Checks for more.
	 *
	 * @return true, if successful
	 */
	public boolean hasMore();

	/**
	 * Removes the.
	 *
	 * @param theTask the task
	 */
	public void remove(Task theTask);

	/**
	 * Adds the tasks.
	 *
	 * @param theTask the task
	 */
	public void addTasks(Task theTask);

	/**
	 * Gets the task list.
	 *
	 * @return the task list
	 */
	public List<Task> getTaskList();

}
