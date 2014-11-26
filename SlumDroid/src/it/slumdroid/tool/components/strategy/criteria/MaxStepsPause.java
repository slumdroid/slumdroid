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

package it.slumdroid.tool.components.strategy.criteria;

import it.slumdroid.tool.model.PauseCriteria;
import it.slumdroid.tool.model.Strategy;

public class MaxStepsPause implements PauseCriteria {

	private int max;
	private int current;

	public MaxStepsPause (int maxSteps) {
		this.max = maxSteps;
		reset();
	}

	public boolean pause () {
		this.current--;
		return this.current == 0;
	}

	public void reset() {
		this.current = this.max;
	}

	public void setStrategy(Strategy theStrategy) {
		
	}

}