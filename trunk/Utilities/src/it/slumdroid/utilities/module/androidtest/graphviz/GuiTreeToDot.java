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

package it.slumdroid.utilities.module.androidtest.graphviz;

import static it.slumdroid.utilities.Resources.BREAK;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
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

// TODO: Auto-generated Javadoc
/**
 * The Class GuiTreeToDot.
 */
public class GuiTreeToDot {

	/** The session. */
	private GuiTree session;
	
	/** The nodes. */
	private List<Node> nodes;
	
	/** The edges. */
	private List<Edge> edges;
	
	/** The crash count. */
	private int crashCount = 0;
	
	/** The fail count. */
	private int failCount = 0;

	/**
	 * Instantiates a new gui tree to dot.
	 *
	 * @param session the session
	 */
	public GuiTreeToDot (GuiTree session) {
		this.session = session;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}

	/**
	 * Gets the dot.
	 *
	 * @return the dot
	 */
	public String getDot () {

		boolean first = true;
		for (Task theTask: this.session) {
			Transition theTransition = theTask.getFinalTransition();
			addTransition(theTransition, first);
			first = false;
		}
		StringBuilder dot = new StringBuilder ();
		dot.append("digraph GuiTree {" + BREAK);

		for (Edge edge: this.edges) {
			dot.append(TAB + edge + " [label=\"" + edge.getId().replace("e", "Event") + ": " + edge.getLabel() + "\"];" + NEW_LINE);
		}
		dot.append(NEW_LINE);
		for (Node node: this.nodes) {
			if (node.hasImage()) {
				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + node.getLabel() + "\"; " + node + "};" + NEW_LINE);
				dot.append(TAB + node + " [label=\"" + node + "\", shapefile=\"" + node.getImage() + "\"];" + BREAK);				
			} else {
				dot.append(TAB + node + " [label=\"" + node.getLabel() + "\"];" + BREAK);
			}
		}

		dot.append('}');		
		return dot.toString();
	}

	/**
	 * Adds the transition.
	 *
	 * @param action the action
	 * @param first the first
	 */
	private void addTransition(Transition action, boolean first) {
		Node start = getNode(action.getStartActivity()); 
		Node finish = getNode(action.getFinalActivity()); 
		UserEvent event = action.getEvent();
		
		Iterator<UserInput> inputs = action.iterator();
		String userInputs = new String();
		while(inputs.hasNext()){
			UserInput input = inputs.next();
			userInputs = userInputs.concat(" Input" + input.getId().replace("i", "") + ": " + input.getType() + " " + input.getWidget().getSimpleType());
			if (!input.getValue().equals("")) userInputs = userInputs.concat(" Value: "+ input.getValue());
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

	/**
	 * Creates the label.
	 *
	 * @param state the state
	 * @return the string
	 */
	private String createLabel (Node state) {
		String label = state.getLabel();
		String id = state.getId();
		if (label.equals(state.getId())) return label;
		if (abnormalState(label)) return label;
		if (id.equals("")) return label;
		if (label.equals("")) return id;
		return id + " = " + label;
	}

	/**
	 * Abnormal state.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	protected boolean abnormalState (String id) {
		return ((id.equals("exit")) || (id.equals("crash")) || (id.equals("fail")));
	}

	/**
	 * Gets the node.
	 *
	 * @param state the state
	 * @return the node
	 */
	private Node getNode (ActivityState state) {
		Node ret = new Node (state);
		if (state.isCrash()) {
			ret.setId(getCrashId());
		} else if (state.isFailure()) {
			ret.setId(getFailId());
		}
		return ret;
	}

	/**
	 * Gets the crash id.
	 *
	 * @return the crash id
	 */
	private String getCrashId() {
		String ret = "crash" + this.crashCount;
		this.crashCount++;
		return ret;
	}

	/**
	 * Gets the fail id.
	 *
	 * @return the fail id
	 */
	private String getFailId() {
		String ret = "failure" + this.failCount;
		this.failCount++;
		return ret;
	}
	
}
