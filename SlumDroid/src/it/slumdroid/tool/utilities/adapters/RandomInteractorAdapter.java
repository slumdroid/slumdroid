/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.utilities.adapters;

import static it.slumdroid.tool.Resources.RANDOM_SEED;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.RandomInteractor;

import java.util.List;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class RandomInteractorAdapter.
 */
public abstract class RandomInteractorAdapter extends SimpleInteractorAdapter implements RandomInteractor {

	/** The random. */
	private Random random = new Random(RANDOM_SEED);

	/** The min. */
	private int min = 0;

	/** The max. */
	private int max = 99;

	/**
	 * Instantiates a new random interactor adapter.
	 *
	 * @param simpleTypes the simple types
	 */
	public RandomInteractorAdapter(String ... simpleTypes) {
		super(simpleTypes);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.RandomInteractor#setRandomGenerator(java.util.Random)
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

	/**
	 * Gets the random generator.
	 *
	 * @return the random generator
	 */
	public Random getRandomGenerator() {
		return this.random;
	}

	/**
	 * Sets the min.
	 *
	 * @param min the new min
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the new max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Sets the min max.
	 *
	 * @param minValue the min value
	 * @param maxValue the max value
	 */
	public void setMinMax(int minValue, int maxValue) {
		if (minValue > maxValue) {
			setMinMax(maxValue, minValue);
		}
		setMin(minValue);
		setMax(maxValue);
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public int getMax() {
		return this.max;
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public int getMin() {
		return this.min;
	}

	/**
	 * Gets the max.
	 *
	 * @param widget the widget
	 * @return the max
	 */
	public int getMax(WidgetState widget) {
		return getMax();
	}

	/**
	 * Gets the min.
	 *
	 * @param widget the widget
	 * @return the min
	 */
	public int getMin(WidgetState widget) {
		return getMin();
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return getRandomGenerator().nextInt(getMax() - getMin()) + getMin();
	}

	/**
	 * Gets the value.
	 *
	 * @param widget the widget
	 * @return the value
	 */
	public int getValue(WidgetState widget) {
		int delta = getMax(widget) - getMin(widget) + 1;
		return (delta > 0)?(getRandomGenerator().nextInt(delta) + getMin(widget)):getMin(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getEvents(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public List<UserEvent> getEvents(WidgetState widget) {
		return getEvents(widget, String.valueOf(getValue(widget)));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInputs(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public List<UserInput> getInputs(WidgetState widget) {
		return getInputs(widget, String.valueOf(getValue(widget)));
	}

}