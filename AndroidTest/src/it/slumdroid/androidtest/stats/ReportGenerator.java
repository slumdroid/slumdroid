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

import java.util.*;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.*;

public class ReportGenerator extends StatsReport {

	private GuiTree session;

	private TraceStats traceReport = new TraceStats();
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

		for (Trace theTrace: this.session) {
			// Trace count
			traceReport.analyzeTrace(theTrace);

			boolean first = true;
			int currentDepth = 0;
			for (Transition step: theTrace) {				

				currentDepth++;

				// Events and input count
				eventReport.analyzeInteractions(step);

				// Widgets count
				if (first) {
					countWidgets(step.getStartActivity());
				}
				countWidgets(step.getFinalActivity());
				first = false;

			}
			this.depth = max(this.depth,currentDepth);
		}


	}

	public String getReport () {
		evaluate();
		StringBuilder builder = new StringBuilder();
		builder.append(this.traceReport + NEW_LINE);
		int stateSize = this.activityStates.size() + crash;
		if (actualCrashes.size()!=0) builder.append("List of actual crashed traces: " + expandList(this.actualCrashes) + NEW_LINE);
		builder.append("Depth reached: " + this.depth + NEW_LINE + "Transitions: " + NEW_LINE + 
				TAB + "different gui states found: " + stateSize + NEW_LINE + 
				TAB + "different activities found: " + this.activity.size() +
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
