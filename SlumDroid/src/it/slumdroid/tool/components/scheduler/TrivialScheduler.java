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

package it.slumdroid.tool.components.scheduler;

import it.slumdroid.tool.Resources.SchedulerAlgorithm;
import it.slumdroid.tool.model.DispatchListener;
import it.slumdroid.tool.model.TaskScheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.slumdroid.droidmodels.model.Task;

class TrivialScheduler implements TaskScheduler {

	private final TraceDispatcher traceDispatcher;
	private List<Task> tasks;
	private SchedulerAlgorithm algorithm;

	public TrivialScheduler (TraceDispatcher traceDispatcher, SchedulerAlgorithm algorithm) {
		this.traceDispatcher = traceDispatcher;
		setSchedulerAlgorithm (algorithm);
	}

	public void setSchedulerAlgorithm (SchedulerAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Task nextTask() {
		if (!hasMore()) return null;
		switch (algorithm) {
		case DEPTH_FIRST: return lastTask();
		case BREADTH_FIRST:
		case RANDOM:
		default: return firstTask();
		}
	}

	public void addTasks(Collection<Task> newTasks) {
		for (Task t: newTasks) {
			tasks.add(t);
			for (DispatchListener theListener: this.traceDispatcher.theListeners) {
				theListener.onNewTaskAdded(t);
			}
		}				
	}

	public void addPlannedTasks(List<Task> newTasks) {
		switch (algorithm) {
		case DEPTH_FIRST:
			Collections.reverse(newTasks);
		case BREADTH_FIRST:
		case RANDOM:
		default: addTasks(newTasks);
		}
	}

	public void setTaskList(List<Task> theList) {
		this.tasks = theList;
	}

	public List<Task> getTaskList() {
		return this.tasks;
	}

	public boolean hasMore() {
		return (!tasks.isEmpty());
	}

	public void remove(Task t) {
		tasks.remove(t);
	}

	public void addTasks(Task t) {
		this.tasks.add(t);
	}

	public Task firstTask() {
		return this.tasks.get(0);
	}

	public Task lastTask() {
		return this.tasks.get(this.tasks.size()-1);
	}

}