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

import it.slumdroid.tool.model.DispatchListener;
import it.slumdroid.tool.model.TaskScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.Resources.SchedulerAlgorithm;

import static it.slumdroid.tool.Resources.SCHEDULER_ALGORITHM;

public class TraceDispatcher implements Iterable<Task> {

	private TaskScheduler scheduler;
	List<DispatchListener> theListeners = new ArrayList<DispatchListener>();

	public TraceDispatcher () {
		this (SchedulerAlgorithm.valueOf(SCHEDULER_ALGORITHM));
	}

	public TraceDispatcher(SchedulerAlgorithm algorithm) {
		setScheduler(getTrivialScheduler(algorithm));
	}

	public void setScheduler (TaskScheduler ts) {
		this.scheduler = ts;
	}

	public void addPlannedTasks (List<Task> t) {
		getScheduler().addPlannedTasks(t);
	}

	public void addTasks (Collection<Task> t) {
		getScheduler().addTasks(t);
	}

	public void addTasks (Task t) {
		getScheduler().addTasks(t);
	}

	public TaskScheduler getTrivialScheduler(SchedulerAlgorithm a) {
		TaskScheduler s = new TrivialScheduler(this, a);
		s.setTaskList(new ArrayList<Task>());
		return s;
	}

	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	public void registerListener(DispatchListener theListener) {
		this.theListeners.add(theListener);
	}

	public Iterator<Task> iterator() {
		return new Iterator<Task> () {

			Task lastTask;

			public boolean hasNext() {
				return scheduler.hasMore();
			}

			public Task next() {
				this.lastTask = scheduler.nextTask();
				for (DispatchListener theListener: theListeners) {
					theListener.onTaskDispatched(this.lastTask);
				}
				remove();
				return this.lastTask;
			}

			public void remove() {
				scheduler.remove(this.lastTask);
			}

		};
	}

}
