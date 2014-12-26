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

import static it.slumdroid.droidmodels.model.SimpleType.DIALOG_TITLE;
import static it.slumdroid.utilities.Resources.BREAK;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import static it.slumdroid.utilities.module.AndroidTest.getStateFileName;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.xml.XmlGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

public class ReportGenerator extends StatsReport {

	private GuiTree session;

	private TaskStats taskReport = new TaskStats();
	private InteractionStats eventReport = new InteractionStats();

	private int depth = 0;
	private int crash = 0;
	private Set<String> activity;
	private Set<String> activityStates;
	private List<String> actualCrashes;

	public ReportGenerator(GuiTree guiTree) {
		this.session = guiTree;
		this.activity = new HashSet<String>();
		this.activityStates = new HashSet<String>();
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
		builder.append(this.taskReport + NEW_LINE);
		int stateSize = this.activityStates.size() + crash;

		if (actualCrashes.size() != 0) {
			builder.append("List of Crashed Tasks: " + expandList(this.actualCrashes) + NEW_LINE);
		}
		builder.append( "Model Information: " + NEW_LINE + 
				TAB + "Different Gui States: " + stateSize + NEW_LINE + 
				TAB + "Different Activities: " + this.activity.size() + NEW_LINE +
				TAB + "Maximum Depth: " + this.depth +
				BREAK + this.eventReport + NEW_LINE +
				"Available Widgets: " + NEW_LINE +
				expandMap(countWidgetTypes()) );
		return builder.toString();
	}

	public void countWidgets (ActivityState activity) {
		if (activity.isFailure()) return;
		if (activity.isCrash()){
			crash = 1;
			return;
		}
		this.activity.add(activity.getName());
		this.activityStates.add(activity.getId());
	}

	public Map<String, Integer> countWidgetTypes() {
		HashSet<ActivityState> stateList = new HashSet<ActivityState>();
		List<String> entries = readFile (getStateFileName());
		for (String row: entries) {
			this.session.parse(row);
			Element element = ((XmlGraph)this.session).getDom().getDocumentElement();
			ActivityState state = this.session.importState (element);
			stateList.add(state);
		}
		Map<String,Integer> widgetTypes = new Hashtable<String, Integer>();
		for (ActivityState state: stateList) {
			for (WidgetState widget: state) {
				if (widget.getSimpleType().equals(DIALOG_TITLE)){
					inc (widgetTypes, widget.getSimpleType());	
				} else {
					if (widget.isAvailable()){
						if (widget.isClickable() 
								|| widget.isLongClickable()){
							inc (widgetTypes, widget.getSimpleType());	
						}
					}
				}
			}
		}
		return widgetTypes;
	}

	private List<String> readFile (String input) {
		BufferedReader theStream = null;
		String line = new String();
		List<String> output = new ArrayList<String>();
		try{
			theStream = new BufferedReader (new FileReader (input));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output;
	}

}
