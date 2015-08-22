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

package it.slumdroid.tool.components.scheduler;

import static it.slumdroid.tool.Resources.SCHEDULER_ALGORITHM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.Resources.SchedulerAlgorithm;
import it.slumdroid.tool.model.DispatchListener;

// TODO: Auto-generated Javadoc
/**
 * The Class TraceDispatcher.
 */
public class TraceDispatcher implements Iterable<Task> {

	/** The scheduler. */
	private TaskScheduler scheduler;

	/** The listeners. */
	List<DispatchListener> theListeners = new ArrayList<DispatchListener>();

	/**
	 * Instantiates a new trace dispatcher.
	 */
	public TraceDispatcher() {
		this (SchedulerAlgorithm.valueOf(SCHEDULER_ALGORITHM));
	}

	/**
	 * Instantiates a new trace dispatcher.
	 *
	 * @param algorithm the algorithm
	 */
	public TraceDispatcher(SchedulerAlgorithm algorithm) {
		setScheduler(getTrivialScheduler(algorithm));
	}

	/**
	 * Sets the scheduler.
	 *
	 * @param taskScheduler the new scheduler
	 */
	public void setScheduler(TaskScheduler taskScheduler) {
		this.scheduler = taskScheduler;
	}

	/**
	 * Adds the planned tasks.
	 *
	 * @param tasks the tasks
	 */
	public void addPlannedTasks(List<Task> tasks) {
		getScheduler().addPlannedTasks(tasks);
	}

	/**
	 * Adds the tasks.
	 *
	 * @param tasks the tasks
	 */
	public void addTasks(Collection<Task> tasks) {
		getScheduler().addTasks(tasks);
	}

	/**
	 * Adds the tasks.
	 *
	 * @param task the task
	 */
	public void addTasks(Task task) {
		getScheduler().addTasks(task);
	}

	/**
	 * Gets the trivial scheduler.
	 *
	 * @param algorithm the algorithm
	 * @return the trivial scheduler
	 */
	public TaskScheduler getTrivialScheduler(SchedulerAlgorithm algorithm) {
		TaskScheduler scheduler = new TaskScheduler(algorithm);
		scheduler.setTaskList(new ArrayList<Task>());
		return scheduler;
	}

	/**
	 * Gets the scheduler.
	 *
	 * @return the scheduler
	 */
	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	/**
	 * Register listener.
	 *
	 * @param theListener the the listener
	 */
	public void registerListener(DispatchListener theListener) {
		this.theListeners.add(theListener);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Task> iterator() {
		return new Iterator<Task>() {

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
