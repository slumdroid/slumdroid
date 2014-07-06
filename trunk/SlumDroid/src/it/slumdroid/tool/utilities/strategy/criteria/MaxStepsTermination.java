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

package it.slumdroid.tool.utilities.strategy.criteria;

import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.SessionParams;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.model.TerminationCriteria;

public class MaxStepsTermination implements TerminationCriteria, SaveStateListener {

	private static final String ACTOR_NAME = "MaxStepsTermination";
	private static final String PARAM_NAME = "current";
	private int max;
	private int current;

	public MaxStepsTermination (int maxSteps) {
		this.max = maxSteps;
		reset();
		PersistenceFactory.registerForSavingState(this);
	}

	public boolean termination () {
		this.current--;
		return (this.current == 0);
	}

	public void reset() {
		this.current = max;
	}

	public void setStrategy(Strategy theStrategy) {}

	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.current);
	}

	public void onLoadingState(SessionParams sessionParams) {
		this.current = sessionParams.getInt(PARAM_NAME);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}

}