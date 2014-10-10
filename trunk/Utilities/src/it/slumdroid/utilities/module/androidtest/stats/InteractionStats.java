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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;

public class InteractionStats extends StatsReport {

	private Set<String> events;
	private Set<String> inputs;
	private Map<String,Integer> eventTypes;	
	private Map<String,Integer> inputTypes;

	public InteractionStats () {
		this.events = new HashSet<String>();
		this.inputs = new HashSet<String>();
		this.eventTypes = new Hashtable<String, Integer>();
		this.inputTypes = new Hashtable<String, Integer>();
	}

	public void analyzeInteractions(Transition step) {
		addEvent(step.getEvent());
		for (UserInput i: step) {
			addInput(i);
		}
	}

	public Set<String> getEvents() {
		return this.events;
	}

	public void addEvent (UserEvent e) {
		if (!getEvents().contains(e.getId())) {
			inc (this.eventTypes, e.getType());
		}
		addEvent (e.getId());
	}

	public void addEvent(String event) {
		this.events.add(event);
	}

	public Set<String> getInputs() {
		return this.inputs;
	}

	public void addInput (UserInput i) {
		if (!getInputs().contains(i.getId())) {
			inc (this.inputTypes, i.getType());
		}
		addInput (i.getId());
	}

	public void addInput(String input) {
		this.inputs.add(input);
	}

	public int getDifferentEvents() {
		return getEvents().size();
	}

	public int getDifferentInputs() {
		return getInputs().size();
	}

	public int getDifferentInteractions() {
		return getDifferentEvents() + getDifferentInputs();
	}

	public String getReport() {
		return 	"Interactions: " + NEW_LINE +
				TAB + "Different events: " + getDifferentEvents() + NEW_LINE +
				expandMap(this.eventTypes) +
				TAB + "Different inputs: " + getDifferentInputs() + NEW_LINE +
				expandMap(this.inputTypes);
	}

}
