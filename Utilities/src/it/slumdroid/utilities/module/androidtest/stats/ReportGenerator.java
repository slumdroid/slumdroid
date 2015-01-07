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

import static it.slumdroid.utilities.Resources.BREAK;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.utilities.module.androidtest.graphviz.Edge;
import it.slumdroid.utilities.module.androidtest.graphviz.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportGenerator.
 */
public class ReportGenerator extends StatsReport {

	/** The session. */
	private GuiTree session;
	
	/** The task report. */
	private TaskStats taskReport;
	
	/** The event report. */
	private InteractionStats eventReport;

	/** The depth. */
	private int depth = 0;
	
	/** The branch. */
	private int branch = 0;
	
	/** The crash. */
	private int crash = 0;
	
	/** The activity. */
	private Set<String> activity;
	
	/** The activity states. */
	private Set<String> activityStates;
	
	/** The actual crashes. */
	private List<String> actualCrashes;

	/**
	 * Instantiates a new report generator.
	 *
	 * @param guiTree the gui tree
	 * @param txtFile the txt file name
	 */
	public ReportGenerator(GuiTree guiTree, String txtFile) {
		this.session = guiTree;
		this.activity = new HashSet<String>();
		this.activityStates = new HashSet<String>();
		this.actualCrashes = new ArrayList<String>();
		this.taskReport = new TaskStats(txtFile);
		this.eventReport = new InteractionStats();
	}

	/**
	 * Evaluate.
	 */
	public void evaluate() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Task theTask: this.session) {
			taskReport.analyzeTask(theTask);
			boolean first = true;
			int currentDepth = 0;
			ActivityState start = null;
			ActivityState finish = null;
			for (Transition transition: theTask) {
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
		for (Edge edge:edges) {
			if (from.equals(edge.getFrom().getId())) {
				currentBranch++;
				this.branch = max(this.branch, currentBranch);
			} else {
				currentBranch = 1;
				from = new String(edge.getFrom().getId());
			}
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.utilities.module.androidtest.stats.StatsReport#getReport()
	 */
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
				TAB + "Max Reached Branch: " + this.branch + BREAK 
				+ this.eventReport);
		return builder.toString();
	}

	/**
	 * Count widgets.
	 *
	 * @param state the state
	 */
	private void countWidgets (ActivityState state) {
		if (state.isFailure()) {
			return;
		}
		if (state.isCrash()) {
			crash = 1;
			return;
		}
		this.activity.add(state.getName());
		this.activityStates.add(state.getId());
	}

}
