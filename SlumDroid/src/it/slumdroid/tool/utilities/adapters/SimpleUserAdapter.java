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

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleUserAdapter.
 */
public class SimpleUserAdapter implements UserAdapter {

	/** The abs. */
	private Abstractor abs;
	
	/** The random generator. */
	private Random randomGenerator;
	
	/** The event types. */
	private List<Interactor> eventTypes;
	
	/** The input types. */
	private List<Interactor> inputTypes;

	/**
	 * Instantiates a new simple user adapter.
	 *
	 * @param abstractor the abstractor
	 * @param random the random
	 */
	public SimpleUserAdapter (Abstractor abstractor, Random random) {
		this.eventTypes = new ArrayList<Interactor>();
		this.inputTypes = new ArrayList<Interactor>();		
		setRandomGenerator(random);	
		setAbstractor(abstractor);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.EventHandler#handleEvent(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserEvent> handleEvent(WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		for (Interactor eventAdapter: getEventTypes()) {
			events.addAll(eventAdapter.getEvents(widget));
		}
		return events;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.InputHandler#handleInput(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserInput> handleInput(WidgetState widget) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		for (Interactor inputAdapter: getInputTypes()) {
			inputs.addAll(inputAdapter.getInputs(widget));
		}
		return inputs;
	}

	/**
	 * Gets the abstractor.
	 *
	 * @return the abstractor
	 */
	public Abstractor getAbstractor() {
		return this.abs;
	}

	/**
	 * Sets the abstractor.
	 *
	 * @param abs the new abstractor
	 */
	public void setAbstractor(Abstractor abs) {
		this.abs = abs;
	}

	/**
	 * Gets the random generator.
	 *
	 * @return the random generator
	 */
	public Random getRandomGenerator() {
		return this.randomGenerator;
	}

	/**
	 * Sets the random generator.
	 *
	 * @param randomGenerator the new random generator
	 */
	public void setRandomGenerator(Random randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.UserAdapter#addEvent(it.slumdroid.tool.model.Interactor[])
	 */
	public void addEvent (Interactor ... events) {
		for (Interactor e: events) {
			e.setAbstractor(getAbstractor());
			this.eventTypes.add(e);
		}
	}

	/**
	 * Gets the event types.
	 *
	 * @return the event types
	 */
	public Iterable<Interactor> getEventTypes () {
		return this.eventTypes;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.UserAdapter#addInput(it.slumdroid.tool.model.Interactor[])
	 */
	public void addInput (Interactor ... inputs) {
		for (Interactor i: inputs) {
			i.setAbstractor(getAbstractor());
			if (i instanceof RandomInteractor) {
				((RandomInteractor) i).setRandomGenerator(getRandomGenerator());
			}
			this.inputTypes.add(i);
		}
	}

	/**
	 * Gets the input types.
	 *
	 * @return the input types
	 */
	public Iterable<Interactor> getInputTypes () {
		return this.inputTypes;
	}

}
