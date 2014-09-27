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

package it.slumdroid.tool.utilities.strategy;

import java.util.ArrayList;

import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.model.StrategyCriteria;
import it.slumdroid.tool.utilities.strategy.criteria.MaxStepsPause;
import it.slumdroid.tool.utilities.strategy.criteria.MaxStepsTermination;
import static it.slumdroid.tool.Resources.MAX_NUM_EVENTS;
import static it.slumdroid.tool.Resources.PAUSE_AFTER_TRACES;

public class StrategyFactory {

	private Comparator comparator;
	private ArrayList<StrategyCriteria> otherCriterias = new ArrayList<StrategyCriteria>();

	public StrategyFactory (Comparator c) {
		this.comparator = c;
	}

	public StrategyFactory (Comparator c, StrategyCriteria ... criterias) {
		this (c);
		setMoreCriterias(criterias);
	}

	public void setMoreCriterias (StrategyCriteria ... s) {
		for (StrategyCriteria sc: s) {
			this.otherCriterias.add(sc);
		}
	}

	public Strategy getStrategy () {
		StrategyCriteria[] c = new StrategyCriteria[this.otherCriterias.size()];
		CustomStrategy s = new CustomStrategy(this.comparator, this.otherCriterias.toArray(c));
		if (MAX_NUM_EVENTS > 0) s.addCriteria(new MaxStepsTermination(MAX_NUM_EVENTS));	
		if (PAUSE_AFTER_TRACES > 0) s.addCriteria(new MaxStepsPause(PAUSE_AFTER_TRACES));
		return s;
	}

}