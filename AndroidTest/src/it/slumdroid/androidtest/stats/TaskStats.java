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

package it.slumdroid.androidtest.stats;

import java.util.ArrayList;
import java.util.List;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;

public class TaskStats extends StatsReport {

	private int tasks = 0;
	private int tasksFailed = 0;
	private int tasksCrashed = 0;
	private int tasksExit = 0;
	private List<String> crashes;
	private List<String> failures;
	private List<String> exits;

	public TaskStats() {
		crashes = new ArrayList<String>();
		failures = new ArrayList<String>();
		exits = new ArrayList<String>();
	}

	public void analyzeTrace (Task theTask) {
		this.tasks++;
		ActivityState a = theTask.getFinalTransition().getFinalActivity();
		String txt;
		if (a.isFailure()) {
			this.tasksFailed++;
			txt = theTask.getId();
			this.failures.add(txt);
		} else if (a.isCrash()) {
			this.tasksCrashed++;
			txt = theTask.getId();
			this.crashes.add(txt);
		} else if (a.isExit()) {
			this.tasksExit++;
			txt = theTask.getId();
			this.exits.add(txt);
		}
	}

	public int getTasks() {
		return tasks;
	}

	public int getTasksFailed() {
		return tasksFailed;
	}

	public int getTasksExit() {
		return tasksExit;
	}

	public int getTasksCrashed() {
		return tasksCrashed;
	}

	public String printList (List<String> list) {
		return ((list.size()>0)?(TAB + TAB + "Tasks: " + expandList(list) + NEW_LINE):"");
	}

	public String getReport() {
		return "Performed Tasks: " + getTasks() + NEW_LINE + 
				TAB + "Failures: " + getTasksFailed() + NEW_LINE + 
				printList (this.failures) +
				TAB + "Crashes: " + getTasksCrashed() + NEW_LINE +
				printList (this.crashes) +
				TAB + "Exits: " + getTasksExit() + NEW_LINE;
	}

}
