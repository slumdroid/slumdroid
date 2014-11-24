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

import it.slumdroid.tool.model.RandomInteractor;

import java.util.List;
import java.util.Random;

import it.slumdroid.droidmodels.model.*;
import static it.slumdroid.tool.Resources.RANDOM_SEED;

public abstract class RandomInteractorAdapter extends SimpleInteractorAdapter implements RandomInteractor {

	private Random random = new Random(RANDOM_SEED);
	private int min=0;
	private int max=99;

	public RandomInteractorAdapter (String ... simpleTypes) {
		super (simpleTypes);
	}

	@Override
	public void setRandomGenerator (Random r) {
		this.random = r;
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
		if (minValue>maxValue) {
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

	public int getMax(WidgetState w) {
		return getMax();
	}

	public int getMin(WidgetState w) {
		return getMin();
	}

	public int getValue() {
		return getRandomGenerator().nextInt(getMax()-getMin()) + getMin();
	}

	public int getValue (WidgetState w) {
		int delta = getMax(w)-getMin(w)+1;
		return (delta>0)?(getRandomGenerator().nextInt(delta) + getMin(w)):getMin(w);
	}

	@Override
	public List<UserEvent> getEvents (WidgetState w) {
		return getEvents (w, String.valueOf(getValue(w)));
	}

	@Override
	public List<UserInput> getInputs (WidgetState w) {
		return getInputs (w, String.valueOf(getValue(w)));
	}

}