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

package it.slumdroid.utilities.module.androidtest.stats;

import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import static it.slumdroid.utilities.module.AndroidTest.getTxtFileName;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	public void analyzeTask (Task theTask) {
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
		return ((list.size() > 0)?(TAB + TAB + "Tasks: " + expandList(list) + NEW_LINE):"");
	}

	public String getReport() {
		return "Performed Tasks: " + getTasks() + NEW_LINE + 
				TAB + "Failures: " + getTasksFailed() + NEW_LINE + 
				printList (this.failures) +
				TAB + "Crashes: " + getTasksCrashed() + NEW_LINE +
				printList (this.crashes) +
				TAB + "Exits: " + getTasksExit() + NEW_LINE + 
				TAB + "Ripping Time: " + getRippingTime() + NEW_LINE;
	}

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
		return new String("in "+ round +" seconds ( " + (int) (seconds/60) + " minutes)");
	}

}
