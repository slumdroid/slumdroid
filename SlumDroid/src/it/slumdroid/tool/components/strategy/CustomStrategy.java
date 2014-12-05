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

import static it.slumdroid.tool.Resources.TAG;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.model.StateDiscoveryListener;
import it.slumdroid.tool.model.Strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

public class CustomStrategy implements Strategy {

	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState> ();
	private Comparator comparator;
	protected boolean positiveComparation = true;
	private Task theTask;
	private List<StateDiscoveryListener> theListeners = new ArrayList<StateDiscoveryListener>();
	
	public CustomStrategy (Comparator c) {
		super();
		setComparator(c);
	}

	public void addState(ActivityState newActivity) {
		for (StateDiscoveryListener listener: getListeners()) {
			listener.onNewState(newActivity);
		}
		this.guiNodes.add(newActivity);
	}

	public void compareState(ActivityState theActivity) {
		this.positiveComparation = true;
		for (ActivityState stored: this.guiNodes) {
			if (getComparator().compare(theActivity, stored)) {
				theActivity.setId(stored.getId());
				Log.i(TAG, "This activity state is equivalent to " + stored.getId());
				return;
			}
		}	
		this.positiveComparation = false;
		Log.i(TAG, "Registering activity " + theActivity.getName() + " (id: " + theActivity.getId() + ") as a new found state");
		addState (theActivity);
	}
	
	public final boolean checkForExploration() {		
		return !this.positiveComparation;		
	}

	public Comparator getComparator() {
		return this.comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public void setTask(Task theTask) {
		this.theTask = theTask;
	}

	public Task getTask () {
		return this.theTask;
	}

	public List<StateDiscoveryListener> getListeners() {
		return this.theListeners;
	}

	public void registerStateListener(StateDiscoveryListener theListener) {
		this.theListeners.add(theListener);
	}

}
