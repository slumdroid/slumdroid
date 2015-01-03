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

public class GuiTreeToFSM {

	private GuiTree session;
	private TreeSet<String> nodes;

	public GuiTreeToFSM (GuiTree session) {
		this.session = session;
		this.nodes = new TreeSet<String>();
	}

	public String getDot () {
		ArrayList<String> insertedEvents = new ArrayList<String>();

		StringBuilder dot = new StringBuilder ();
		dot.append("digraph finite_state_machine {" + BREAK);
		dot.append("\trankdir=LR;" + NEW_LINE + "\tnode [shape = circle];" + BREAK);

		for (Task t: this.session) {
			Transition action = t.getFinalTransition();
			ActivityState start = action.getStartActivity();
			Iterator<UserInput> inputs = action.iterator();
			String userInputs = new String();
			while(inputs.hasNext()){
				UserInput input = inputs.next();
				userInputs = userInputs.concat(" Input" + input.getId().replace("i", "") + ": " + input.getType() + " " + input.getWidget().getSimpleType());
				if (!input.getValue().equals("")) userInputs = userInputs.concat(" Value: "+ input.getValue());
			}
			UserEvent event = action.getEvent();
			ActivityState end = action.getFinalActivity();
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
