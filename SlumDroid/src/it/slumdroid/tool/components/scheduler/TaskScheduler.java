/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.components.scheduler;

import static it.slumdroid.tool.Resources.RANDOM_SEED;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.Resources.SchedulerAlgorithm;

// TODO: Auto-generated Javadoc
/**
 * The Class TrivialScheduler.
 */
public class TaskScheduler {

	/** The tasks. */
	private List<Task> tasks;

	/** The algorithm. */
	private SchedulerAlgorithm algorithm;

	/**
	 * Instantiates a new trivial scheduler.
	 *
	 * @param algorithm the algorithm
	 */
	public TaskScheduler(SchedulerAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Next task.
	 *
	 * @return the task
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

	/**
	 * Adds the tasks.
	 *
	 * @param newTasks the new tasks
	 */
	public void addTasks(Collection<Task> newTasks) {
		for (Task task: newTasks) {
			this.tasks.add(task);
		}				
	}

	/**
	 * Adds the planned tasks.
	 *
	 * @param newTasks the new tasks
	 */
	public void addPlannedTasks(List<Task> newTasks) {
		addTasks(newTasks);
	}

	/**
	 * Sets the task list.
	 *
	 * @param theList the new task list
	 */
	public void setTaskList(List<Task> theList) {
		this.tasks = theList;
	}

	/**
	 * Gets the task list.
	 *
	 * @return the task list
	 */
	public List<Task> getTaskList() {
		return this.tasks;
	}

	/**
	 * Checks for more.
	 *
	 * @return true, if successful
	 */
	public boolean hasMore() {
		return !this.tasks.isEmpty();
	}

	/**
	 * Removes the.
	 *
	 * @param task the task
	 */
	public void remove(Task task) {
		this.tasks.remove(task);
	}

	/**
	 * Adds the tasks.
	 *
	 * @param task the task
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