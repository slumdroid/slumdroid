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

package it.slumdroid.tool.components.strategy;

import it.slumdroid.tool.components.strategy.criteria.MaxStepsPause;
import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.model.Strategy;

public class StrategyFactory {

	private Comparator comparator;
	private int pauseAfterTasks = 1; // After performing this amount of traces, the tool pauses (0 = no pause)

	public StrategyFactory (Comparator comparator) {
		this.comparator = comparator;
	}

	public Strategy getStrategy () {
		CustomStrategy strategy = new CustomStrategy(this.comparator);
		strategy.addCriteria(new MaxStepsPause(this.pauseAfterTasks));
		return strategy;
	}
	
}