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

package it.slumdroid.utilities.module.androidtest.stats;

import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TaskStats.
 */
public class TaskStats extends StatsReport {

	/** The crashes. */
	private List<String> crashes;

	/** The exits. */
	private List<String> exits;

	/** The failures. */
	private List<String> failures;

	/** The tasks. */
	private int tasks = 0;

	/** The tasks crashed. */
	private int tasksCrashed = 0;

	/** The tasks exit. */
	private int tasksExit = 0;

	/** The tasks failed. */
	private int tasksFailed = 0;

	/** The txt file name. */
	private String txtFileName;

	/**
	 * Instantiates a new task stats.
	 * 
	 * @param txtFile the txt file name
	 */
	public TaskStats(String txtFile) {
		this.crashes = new ArrayList<String>();
		this.failures = new ArrayList<String>();
		this.exits = new ArrayList<String>();
		this.txtFileName = new String(txtFile);
	}

	/**
	 * Analyze task.
	 *
	 * @param theTask the the task
	 */
	public void analyzeTask (Task theTask) {
		this.tasks++;
		ActivityState state = theTask.getFinalTransition().getFinalActivity();
		String text;
		if (state.isFailure()) {
			this.tasksFailed++;
			text = theTask.getId();
			this.failures.add(text);
		} else if (state.isCrash()) {
			this.tasksCrashed++;
			text = theTask.getId();
			this.crashes.add(text);
		} else if (state.isExit()) {
			this.tasksExit++;
			text = theTask.getId();
			this.exits.add(text);
		}
	}

	/**
	 * Prints the list.
	 *
	 * @param list the list
	 * @return the string
	 */
	private String printList (List<String> list) {
		return ((list.size() > 0)?(TAB + TAB + "Tasks: " + expandList(list) + NEW_LINE):"");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.utilities.module.androidtest.stats.StatsReport#getReport()
	 */
	public String getReport() {
		return "Performed Tasks: " + getTasks() + NEW_LINE + 
				TAB + "Failures: " + getTasksFailed() + NEW_LINE + 
				printList (this.failures) +
				TAB + "Crashes: " + getTasksCrashed() + NEW_LINE +
				printList (this.crashes) +
				TAB + "Exits: " + getTasksExit() + NEW_LINE + 
				TAB + "Time: " + getRippingTime() + NEW_LINE;
	}

	/**
	 * Gets the ripping time.
	 *
	 * @return the ripping time
	 */
	private String getRippingTime() {
		double seconds = 0;
		BufferedReader inputStream1 = null;
		try {
			inputStream1 = new BufferedReader (new FileReader (getTxtFileName()));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains("Time: ")) {
					seconds += Double.valueOf(line.replace("Time: ", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String round = String.valueOf(seconds).replace("."," ").split(" ")[0];
		return new String("about " + round + " seconds");
	}


	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 */
	private int getTasks() {
		return tasks;
	}

	/**
	 * Gets the tasks failed.
	 *
	 * @return the tasks failed
	 */
	private int getTasksFailed() {
		return tasksFailed;
	}

	/**
	 * Gets the tasks exit.
	 *
	 * @return the tasks exit
	 */
	private int getTasksExit() {
		return tasksExit;
	}

	/**
	 * Gets the tasks crashed.
	 *
	 * @return the tasks crashed
	 */
	private int getTasksCrashed() {
		return tasksCrashed;
	}

	/**
	 * Gets the txt file name.
	 *
	 * @return the txt file name
	 */
	private String getTxtFileName() {
		return txtFileName;
	}

}
