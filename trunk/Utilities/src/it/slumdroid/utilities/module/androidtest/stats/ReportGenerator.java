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

import static it.slumdroid.utilities.Resources.BREAK;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportGenerator extends StatsReport {

	private GuiTree session;

	private TaskStats taskReport = new TaskStats();
	private InteractionStats eventReport = new InteractionStats();

	private int depth = 0;
	private int crash = 0;
	private Set<String> activity;
	private Set<String> activityStates;

	private Map<String,Integer> widgetTypes;
	private Map<String,Integer> widgets;
	private Map<String,Integer> widgetStates;
	private List<String> actualCrashes;

	public ReportGenerator(GuiTree guiTree) {
		this.session = guiTree;
		this.activity = new HashSet<String>();
		this.activityStates = new HashSet<String>();
		this.widgetTypes = new Hashtable<String, Integer>();
		this.widgets = new Hashtable<String,Integer>();
		this.widgetStates = new Hashtable<String,Integer>();
		this.actualCrashes = new ArrayList<String>();
	}

	public void evaluate() {
		for (Task theTask: this.session) {
			taskReport.analyzeTask(theTask);
			boolean first = true;
			int currentDepth = 0;
			for (Transition step: theTask) {				
				currentDepth++;
				eventReport.analyzeInteractions(step);
				if (first) countWidgets(step.getStartActivity());
				countWidgets(step.getFinalActivity());
				first = false;
			}
			this.depth = max(this.depth,currentDepth);
		}
	}

	public String getReport () {
		evaluate();
		StringBuilder builder = new StringBuilder();
		builder.append(this.taskReport + NEW_LINE);
		int stateSize = this.activityStates.size() + crash;
		
		if (actualCrashes.size()!=0) builder.append("List of Crashed Tasks: " + expandList(this.actualCrashes) + NEW_LINE);
		builder.append("Model Information: " + NEW_LINE + 
				TAB + "Different Gui States: " + stateSize + NEW_LINE + 
				TAB + "Different Activities: " + this.activity.size() + NEW_LINE +
				TAB + "Maximum Depth: " + this.depth +
				BREAK + this.eventReport);
		
		return builder.toString();
	}

	public void countWidgets (ActivityState activity) {
		if (activity.isFailure()) return;
		if (activity.isCrash()){
			crash = 1;
			return;
		}
		int localCount = 0;
		String key = activity.getName();
		String key2 = activity.getId();
		for (WidgetState w: activity) {
			localCount++;
			if (! (w.getSimpleType().equals("") || w.getSimpleType().equals("null")) ) {
				inc (this.widgetTypes, w.getSimpleType());
			}
		}	
		this.activity.add(key);
		this.widgets.put(key, max(localCount,this.widgets.get(key)));
		this.activityStates.add(key2);
		this.widgetStates.put(key2, max(localCount,this.widgetStates.get(key2)));
	}

}
