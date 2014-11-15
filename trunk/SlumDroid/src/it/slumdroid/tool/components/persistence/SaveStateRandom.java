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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.SessionParams;

import java.util.Random;

@SuppressWarnings("serial")
public class SaveStateRandom extends Random implements SaveStateListener {
	public final static String ACTOR_NAME = "RandomEngine";
	private final static String PARAM_NAME = "randomState";
	int count;

	public SaveStateRandom (long seed) {
		super (seed);
		count = 0;
		PersistenceFactory.registerForSavingState(this);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}

	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.count);
	}

	public void onLoadingState(SessionParams sessionParams) {
		this.count = sessionParams.getInt(PARAM_NAME);
		for (int i=0; i<this.count; i++) {
			nextInt();
		}
	}

	public int nextInt (int max) {
		count++;
		return super.nextInt(max);
	}

}
