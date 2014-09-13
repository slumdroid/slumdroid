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

import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.model.ExplorationCriteria;
import it.slumdroid.tool.model.PauseCriteria;
import it.slumdroid.tool.model.StrategyCriteria;
import it.slumdroid.tool.model.TerminationCriteria;

import java.util.ArrayList;
import java.util.Collection;

public class CustomStrategy extends SimpleStrategy {

	private Collection<ExplorationCriteria> explorers = new ArrayList<ExplorationCriteria>();

	public CustomStrategy(Comparator c, StrategyCriteria ... criterias) {
		super(c);
		for (StrategyCriteria s: criterias) {
			if (s==null) continue;
			addCriteria(s);
		}
	}

	public void addCriteria (StrategyCriteria criteria) {
		if (criteria instanceof ExplorationCriteria) {
			addExplorationCriteria((ExplorationCriteria)criteria);
		} else if (criteria instanceof TerminationCriteria) {
			addTerminationCriteria((TerminationCriteria)criteria);
		} else if (criteria instanceof PauseCriteria) {
			addPauseCriteria((PauseCriteria)criteria);
		}
	}

	public void addExplorationCriteria (ExplorationCriteria e) {
		e.setStrategy(this);
		this.explorers.add(e);
	}

	@Override
	public boolean explorationNeeded() { // Logic AND of the criterias
		for (ExplorationCriteria e: this.explorers) {
			if (!e.exploration()) return false;
		}
		return true;
	}

}
