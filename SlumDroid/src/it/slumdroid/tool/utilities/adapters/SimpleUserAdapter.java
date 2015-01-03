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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.Interactor;
import it.slumdroid.tool.model.RandomInteractor;
import it.slumdroid.tool.model.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleUserAdapter implements UserAdapter {

	private Abstractor abs;
	private Random randomGenerator;
	private List<Interactor> eventTypes;
	private List<Interactor> inputTypes;

	public SimpleUserAdapter (Abstractor abstractor, Random random) {
		this.eventTypes = new ArrayList<Interactor>();
		this.inputTypes = new ArrayList<Interactor>();		
		setRandomGenerator(random);	
		setAbstractor(abstractor);
	}

	public List<UserEvent> handleEvent(WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		for (Interactor eventAdapter: getEventTypes()) {
			events.addAll(eventAdapter.getEvents(widget));
		}
		return events;
	}

	public List<UserInput> handleInput(WidgetState widget) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		for (Interactor inputAdapter: getInputTypes()) {
			inputs.addAll(inputAdapter.getInputs(widget));
		}
		return inputs;
	}

	public Abstractor getAbstractor() {
		return this.abs;
	}

	public void setAbstractor(Abstractor abs) {
		this.abs = abs;
	}

	public Random getRandomGenerator() {
		return this.randomGenerator;
	}

	public void setRandomGenerator(Random randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	public void addEvent (Interactor ... events) {
		for (Interactor e: events) {
			e.setAbstractor(getAbstractor());
			this.eventTypes.add(e);
		}
	}

	public Iterable<Interactor> getEventTypes () {
		return this.eventTypes;
	}

	public void addInput (Interactor ... inputs) {
		for (Interactor i: inputs) {
			i.setAbstractor(getAbstractor());
			if (i instanceof RandomInteractor) {
				((RandomInteractor) i).setRandomGenerator(getRandomGenerator());
			}
			this.inputTypes.add(i);
		}
	}

	public Iterable<Interactor> getInputTypes () {
		return this.inputTypes;
	}

}
