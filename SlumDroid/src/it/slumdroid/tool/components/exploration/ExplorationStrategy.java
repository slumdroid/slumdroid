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

package it.slumdroid.tool.components.exploration;

import static it.slumdroid.tool.Resources.TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.model.StateDiscoveryListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ExplorationStrategy.
 */
public class ExplorationStrategy {

	/** The gui nodes. */
	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState>();

	/** The comparator. */
	private CompositionalComparator comparator = new CompositionalComparator();

	/** The positive comparation. */
	protected boolean positiveComparation = true;

	/** The task. */
	private Task theTask;

	/** The listeners. */
	private List<StateDiscoveryListener> theListeners = new ArrayList<StateDiscoveryListener>();


	/**
	 * Adds the state.
	 *
	 * @param newActivity the new activity
	 */
	public void addState(ActivityState newActivity) {
		for (StateDiscoveryListener listener: getListeners()) {
			listener.onNewState(newActivity);
		}
		this.guiNodes.add(newActivity);
	}

	/**
	 * Compare state.
	 *
	 * @param theActivity the the activity
	 */
	public void compareState(ActivityState theActivity) {
		this.positiveComparation = true;
		for (ActivityState stored: this.guiNodes) {
			if (this.comparator.compare(theActivity, stored)) {
				theActivity.setId(stored.getId());
				Log.i(TAG, "This activity state is equivalent to " + stored.getId());
				return;
			}
		}	
		this.positiveComparation = false;
		Log.i(TAG, "Registering activity " + theActivity.getName() + " (id: " + theActivity.getId() + ") as a new found state");
		addState (theActivity);
	}

	/**
	 * Check for exploration.
	 *
	 * @return true, if successful
	 */
	public final boolean checkForExploration() {		
		return !this.positiveComparation;		
	}

	/**
	 * Sets the task.
	 *
	 * @param theTask the new task
	 */
	public void setTask(Task theTask) {
		this.theTask = theTask;
	}

	/**
	 * Gets the task.
	 *
	 * @return the task
	 */
	public Task getTask() {
		return this.theTask;
	}

	/**
	 * Gets the listeners.
	 *
	 * @return the listeners
	 */
	public List<StateDiscoveryListener> getListeners() {
		return this.theListeners;
	}

	/**
	 * Register state listener.
	 *
	 * @param theListener the the listener
	 */
	public void registerStateListener(StateDiscoveryListener theListener) {
		this.theListeners.add(theListener);
	}

}
