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

package it.slumdroid.tool.model;

import java.util.Collection;
import java.util.List;

import it.slumdroid.droidmodels.model.Task;

public interface TaskScheduler {

	public Task nextTask();
	public void addTasks (Collection<Task> newTasks);
	public void addPlannedTasks(List<Task> newTasks);
	public void setTaskList (List<Task> theList);
	public boolean hasMore();
	public void remove (Task t);
	public void addTasks(Task t);
	public List<Task> getTaskList();

}
