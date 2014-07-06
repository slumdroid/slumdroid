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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.tool.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.slumdroid.droidmodels.model.*;

public class SimpleUserAdapter implements UserAdapter {

	private Abstractor abs;
	private Random randomGenerator;
	private List<Interactor> eventTypes;
	private List<Interactor> inputTypes;

	public SimpleUserAdapter (Abstractor a, Random r) {
		this.eventTypes = new ArrayList<Interactor>();
		this.inputTypes = new ArrayList<Interactor>();		
		setRandomGenerator(r);	
		setAbstractor(a);
	}

	public List<UserEvent> handleEvent(WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		for (Interactor eventAdapter: getEventTypes()) {
			events.addAll(eventAdapter.getEvents(w));
		}
		return events;
	}

	public List<UserInput> handleInput(WidgetState w) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		for (Interactor inputAdapter: getInputTypes()) {
			inputs.addAll(inputAdapter.getInputs(w));
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
			eventTypes.add(e);
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
			inputTypes.add(i);
		}
	}

	public Iterable<Interactor> getInputTypes () {
		return this.inputTypes;
	}

}
