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

import static it.slumdroid.tool.Resources.RANDOM_SEED;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.RandomInteractor;

import java.util.List;
import java.util.Random;

public abstract class RandomInteractorAdapter extends SimpleInteractorAdapter implements RandomInteractor {

	private Random random = new Random(RANDOM_SEED);
	private int min=0;
	private int max=99;

	public RandomInteractorAdapter (String ... simpleTypes) {
		super (simpleTypes);
	}

	@Override
	public void setRandomGenerator (Random random) {
		this.random = random;
	}

	public Random getRandomGenerator () {
		return this.random;
	}

	public void setMin (int min) {
		this.min = min;
	}

	public void setMax (int max) {
		this.max = max;
	}

	public void setMinMax (int minValue, int maxValue) {
		if (minValue > maxValue) {
			setMinMax(maxValue, minValue);
		}
		setMin(minValue);
		setMax(maxValue);
	}

	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax(WidgetState widget) {
		return getMax();
	}

	public int getMin(WidgetState widget) {
		return getMin();
	}

	public int getValue() {
		return getRandomGenerator().nextInt(getMax() - getMin()) + getMin();
	}

	public int getValue (WidgetState widget) {
		int delta = getMax(widget) - getMin(widget) + 1;
		return (delta > 0)?(getRandomGenerator().nextInt(delta) + getMin(widget)):getMin(widget);
	}

	@Override
	public List<UserEvent> getEvents (WidgetState widget) {
		return getEvents (widget, String.valueOf(getValue(widget)));
	}

	@Override
	public List<UserInput> getInputs (WidgetState widget) {
		return getInputs (widget, String.valueOf(getValue(widget)));
	}

}