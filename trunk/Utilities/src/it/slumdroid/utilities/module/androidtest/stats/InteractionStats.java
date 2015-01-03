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

import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class InteractionStats extends StatsReport {

	private int events;
	private int inputs;
	private Set<String> diffEvents;
	private Set<String> diffInputs;
	private Map<String,Integer> eventTypes;	
	private Map<String,Integer> inputTypes;

	public InteractionStats () {
		this.events = 0;
		this.inputs = 0;
		this.diffEvents = new HashSet<String>();
		this.diffInputs = new HashSet<String>();
		this.eventTypes = new Hashtable<String, Integer>();
		this.inputTypes = new Hashtable<String, Integer>();
	}

	public void analyzeInteractions(Transition step) {
		addEvent(step.getEvent());
		for (UserInput i: step) {
			addInput(i);
			this.inputs++;
		}
	}

	public Set<String> getEvents() {
		return this.diffEvents;
	}

	public void addEvent (UserEvent event) {
		if (!getEvents().contains(event.getId())) {
			inc (this.eventTypes, event.getType());
		}
		addEvent (event.getId());
		this.events++;
	}

	public void addEvent(String event) {
		this.diffEvents.add(event);
	}

	public Set<String> getInputs() {
		return this.diffInputs;
	}

	public void addInput (UserInput input) {
		if (!getInputs().contains(input.getId())) {
			inc (this.inputTypes, input.getType());
		}
		addInput (input.getId());
	}

	public void addInput(String input) {
		this.diffInputs.add(input);
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
				TAB + "Total Events: " + this.events + NEW_LINE +
				TAB + "Different events: " + getDifferentEvents() + NEW_LINE +
				expandMap(this.eventTypes) +
				TAB + "Total Inputs: " + this.inputs + NEW_LINE +
				TAB + "Different inputs: " + getDifferentInputs() + NEW_LINE +
				expandMap(this.inputTypes);
	}

}
