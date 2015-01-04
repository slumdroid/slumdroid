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

import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.BREAK;
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
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class GuiTreeToFSM.
 */
public class GuiTreeToFSM {

	/** The session. */
	private GuiTree session;
	
	/** The nodes. */
	private TreeSet<String> nodes;

	/**
	 * Instantiates a new gui tree to fsm.
	 *
	 * @param session the session
	 */
	public GuiTreeToFSM (GuiTree session) {
		this.session = session;
		this.nodes = new TreeSet<String>();
	}

	/**
	 * Gets the dot.
	 *
	 * @return the dot
	 */
	public String getDot () {
		ArrayList<String> insertedEvents = new ArrayList<String>();

		StringBuilder dot = new StringBuilder ();
		dot.append("digraph finite_state_machine {" + BREAK);
		dot.append("\trankdir=LR;" + NEW_LINE + "\tnode [shape = circle];" + BREAK);

		for (Task theTask: this.session) {
			Transition theTransition = theTask.getFinalTransition();
			ActivityState start = theTransition.getStartActivity();
			Iterator<UserInput> inputs = theTransition.iterator();
			String userInputs = new String();
			while(inputs.hasNext()){
				UserInput input = inputs.next();
				userInputs = userInputs.concat(" Input" + input.getId().replace("i", "") + ": " + input.getType() + " " + input.getWidget().getSimpleType());
				if (!input.getValue().equals("")) userInputs = userInputs.concat(" Value: "+ input.getValue());
			}
			UserEvent event = theTransition.getEvent();
			ActivityState end = theTransition.getFinalActivity();
			String startnode = start.getId().equals("a0")?"start":start.getId();
			String endnode = end.getId().equals("a0")?"start":end.getId();
			dot.append("\t" + startnode + " -> " + endnode + " [label=\"" + event.getId().replace("e", "Event") + ": " + getCaption(event) + userInputs + "\"];" + NEW_LINE);
			this.nodes.add(endnode);
			insertedEvents.add(event.getId());
		}

		dot.append(NEW_LINE);
		for (String node: this.nodes) {
			dot.append(TAB + node + " [label=\"" + node + "\"];" + NEW_LINE);
		}
		dot.append(NEW_LINE + "}");
		return dot.toString();
	}

}
