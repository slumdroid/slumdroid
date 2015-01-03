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
import it.slumdroid.utilities.module.androidtest.graphviz.Edge;
import it.slumdroid.utilities.module.androidtest.graphviz.Node;

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
	private int branch = 0;
	private int crash = 0;
	private int transitions = 0;
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
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Task theTask: this.session) {
			taskReport.analyzeTask(theTask);
			boolean first = true;
			int currentDepth = 0;
			ActivityState start = null;
			ActivityState finish = null;
			for (Transition transition: theTask) {
				this.transitions++;
				currentDepth++;
				eventReport.analyzeInteractions(transition);
				if (first) {
					countWidgets(transition.getStartActivity());
				} 
				countWidgets(transition.getFinalActivity());
				first = false;
				start = transition.getStartActivity();
				finish = transition.getFinalActivity();
			}
			this.depth = max(this.depth,currentDepth);
			Edge edge = new Edge(new Node (start), new Node (finish));
			edges.add(edge);
		}
		
		String from = new String();
		int currentBranch = 0;
		for (Edge edge:edges){
			if (from.equals(edge.getFrom().getId())){
				currentBranch++;
				this.branch = max(this.branch, currentBranch);
			} else {
				currentBranch = 1;
				from = new String(edge.getFrom().getId());
			}
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
				TAB + "Different GUI States: " + stateSize + NEW_LINE + 
				TAB + "Different Activities: " + this.activity.size() + NEW_LINE +
				TAB + "Max Reached Depth: " + this.depth + NEW_LINE +
				TAB + "Max Reached Branch: " + this.branch + NEW_LINE +
				TAB + "Total Transitions: " + this.transitions +
				BREAK + this.eventReport + NEW_LINE +
				TAB + "Available Widgets: " + NEW_LINE +
				expandMap(countWidgetTypes()) );
		return builder.toString();
	}

	public void countWidgets (ActivityState state) {
		if (state.isFailure()) return;
		if (state.isCrash()){
			crash = 1;
			return;
		}
		this.activity.add(state.getName());
		this.activityStates.add(state.getId());
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
