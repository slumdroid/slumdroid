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

// TODO: Auto-generated Javadoc
/**
 * The Class InteractionStats.
 */
public class InteractionStats extends StatsReport {

	/** The events. */
	private int events;
	
	/** The inputs. */
	private int inputs;
	
	/** The diff events. */
	private Set<String> diffEvents;
	
	/** The diff inputs. */
	private Set<String> diffInputs;
	
	/** The event types. */
	private Map<String,Integer> eventTypes;	
	
	/** The input types. */
	private Map<String,Integer> inputTypes;

	/**
	 * Instantiates a new interaction stats.
	 */
	public InteractionStats () {
		this.events = 0;
		this.inputs = 0;
		this.diffEvents = new HashSet<String>();
		this.diffInputs = new HashSet<String>();
		this.eventTypes = new Hashtable<String, Integer>();
		this.inputTypes = new Hashtable<String, Integer>();
	}

	/**
	 * Analyze interactions.
	 *
	 * @param transition the transition
	 */
	public void analyzeInteractions(Transition transition) {
		addEvent(transition.getEvent());
		for (UserInput input: transition) {
			addInput(input);
		}
	}

	/**
	 * Adds the event.
	 *
	 * @param event the event
	 */
	private void addEvent (UserEvent event) {
		if (!this.diffEvents.contains(event.getId())) {
			inc (this.eventTypes, event.getType());
		}
		this.diffEvents.add(event.getId());
		this.events++;
	}

	/**
	 * Adds the input.
	 *
	 * @param input the input
	 */
	private void addInput (UserInput input) {
		if (!this.diffInputs.contains(input.getId())) {
			inc (this.inputTypes, input.getType());
		}
		this.diffInputs.add(input.getId());
		this.inputs++;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.utilities.module.androidtest.stats.StatsReport#getReport()
	 */
	public String getReport() {
		return 	"Interactions: " + NEW_LINE +
				TAB + "Total Events: " + this.events + NEW_LINE +
				TAB + "Different events: " + this.diffEvents.size() + NEW_LINE +
				expandMap(this.eventTypes) +
				TAB + "Total Inputs: " + this.inputs + NEW_LINE +
				TAB + "Different inputs: " + this.diffInputs.size() + NEW_LINE +
				expandMap(this.inputTypes);
	}

}
