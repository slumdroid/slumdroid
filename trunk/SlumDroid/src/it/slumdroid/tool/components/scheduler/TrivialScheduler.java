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

import static it.slumdroid.tool.Resources.RANDOM_SEED;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.Resources.SchedulerAlgorithm;
import it.slumdroid.tool.model.TaskScheduler;

import java.util.Collection;
import java.util.List;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class TrivialScheduler.
 */
public class TrivialScheduler implements TaskScheduler {

	/** The tasks. */
	private List<Task> tasks;

	/** The algorithm. */
	private SchedulerAlgorithm algorithm;

	/**
	 * Instantiates a new trivial scheduler.
	 *
	 * @param algorithm the algorithm
	 */
	public TrivialScheduler(SchedulerAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#nextTask()
	 */
	public Task nextTask() {
		if (!hasMore()) {
			return null;
		}
		switch (this.algorithm) {
		case DEPTH_FIRST:
			return lastTask();
		case RANDOM_FIRST:
		 // return randomTask(); 
		case BREADTH_FIRST:
		default:
			return firstTask();
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#addTasks(java.util.Collection)
	 */
	public void addTasks(Collection<Task> newTasks) {
		for (Task task: newTasks) {
			this.tasks.add(task);
		}				
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#addPlannedTasks(java.util.List)
	 */
	public void addPlannedTasks(List<Task> newTasks) {
		addTasks(newTasks);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#setTaskList(java.util.List)
	 */
	public void setTaskList(List<Task> theList) {
		this.tasks = theList;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#getTaskList()
	 */
	public List<Task> getTaskList() {
		return this.tasks;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#hasMore()
	 */
	public boolean hasMore() {
		return !this.tasks.isEmpty();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#remove(it.slumdroid.droidmodels.model.Task)
	 */
	public void remove(Task task) {
		this.tasks.remove(task);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.TaskScheduler#addTasks(it.slumdroid.droidmodels.model.Task)
	 */
	public void addTasks(Task task) {
		this.tasks.add(task);
	}

	/**
	 * First task.
	 *
	 * @return the first task
	 */
	public Task firstTask() {
		return this.tasks.get(0);
	}

	/**
	 * Last task.
	 *
	 * @return the last task
	 */
	public Task lastTask() {
		return this.tasks.get(this.tasks.size() - 1);
	}
	
	/**
	 * Random task.
	 *
	 * @return the random task
	 */
	public Task randomTask() {
		return this.tasks.get(new Random(RANDOM_SEED).nextInt(this.tasks.size()));
	}

}