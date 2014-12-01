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

package it.slumdroid.utilities.module.androidtest.graphviz;

import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.EOL;
import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.TAB;
import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.getCaption;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiTreeToDot {

	private GuiTree session;
	private List<Node> nodes;
	private List<Edge> edges;
	private int crashCount = 0;
	private int failCount = 0;

	public GuiTreeToDot (GuiTree session) {
		this.session = session;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}

	public String getDot () {

		boolean first = true;
		for (Task t: this.session) {
			Transition action = t.getFinalTransition();
			addTransition(action, first);
			first = false;
		}

		StringBuilder dot = new StringBuilder ();
		dot.append("digraph GuiTree {" + EOL + EOL);

		for (Edge edge: this.edges) {
			dot.append(TAB + edge + " [label=\"" + edge.getId().replace("e", "Event") + ": " + edge.getLabel() + "\"];" + EOL);
		}
		dot.append(EOL);
		for (Node node: this.nodes) {
			if (node.hasImage()) {
				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + node.getLabel() + "\"; " + node + "};" + EOL);
				dot.append(TAB + node + " [label=\"" + node + "\", shapefile=\"" + node.getImage() + "\"];" + EOL + EOL);				
			} else {
				dot.append(TAB + node + " [label=\"" + node.getLabel() + "\"];" + EOL + EOL);
			}
		}

		dot.append('}');		
		return dot.toString();
	}

	private void addTransition(Transition action, boolean first) {
		Node start = getNode(action.getStartActivity()); 
		Node finish = getNode(action.getFinalActivity()); 
		UserEvent event = action.getEvent();
		
		Iterator<UserInput> inputs = action.iterator();
		String userInputs = new String();
		while(inputs.hasNext()){
			UserInput input = inputs.next();
			userInputs = userInputs.concat(" Input" + input.getId().replace("i", "") + ": " + input.getType() + " Value: "+ input.getValue());
		}

		// Add main activity to nodes
		if (first) {
			this.nodes.add(start);
		}

		// Add new activity to nodes
		finish.setLabel(createLabel (finish));
		this.nodes.add(finish);

		// Add event to edges
		Edge e = new Edge(start,finish);
		e.setLabel(getCaption(event) + userInputs);
		e.setId(event.getId());
		this.edges.add(e);
	}

	private String createLabel (Node state) {
		String label = state.getLabel();
		String id = state.getId();
		if (label.equals(state.getId())) return label;
		if (abnormalState(label)) return label;
		if (id.equals("")) return label;
		if (label.equals("")) return id;
		return id + " = " + label;
	}

	protected boolean abnormalState (String id) {
		return ((id.equals("exit")) || (id.equals("crash")) || (id.equals("fail")));
	}

	private Node getNode (ActivityState activity) {
		Node ret = new Node (activity);
		if (activity.isCrash()) {
			ret.setId(getCrashId());
		} else if (activity.isFailure()) {
			ret.setId(getFailId());
		}
		return ret;
	}

	private String getCrashId() {
		String ret = "crash" + this.crashCount;
		this.crashCount++;
		return ret;
	}

	private String getFailId() {
		String ret = "failure" + this.failCount;
		this.failCount++;
		return ret;
	}

}
