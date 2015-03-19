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
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.tool.components.exploration;

import static it.slumdroid.tool.Resources.TAG;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.model.StateDiscoveryListener;
import it.slumdroid.tool.model.Strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class ExplorationStrategy.
 */
public class ExplorationStrategy implements Strategy {

	/** The gui nodes. */
	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState> ();

	/** The comparator. */
	private CompositionalComparator comparator;

	/** The positive comparation. */
	protected boolean positiveComparation = true;

	/** The task. */
	private Task theTask;

	/** The listeners. */
	private List<StateDiscoveryListener> theListeners = new ArrayList<StateDiscoveryListener>();

	/**
	 * Instantiates a new exploration strategy.
	 */
	public ExplorationStrategy() {
		this(new CompositionalComparator());
	}

	/**
	 * Instantiates a new exploration strategy.
	 *
	 * @param comparator the comparator
	 */
	public ExplorationStrategy(CompositionalComparator comparator) {
		super();
		setComparator(comparator);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#addState(it.slumdroid.droidmodels.model.ActivityState)
	 */
	public void addState(ActivityState newActivity) {
		for (StateDiscoveryListener listener: getListeners()) {
			listener.onNewState(newActivity);
		}
		this.guiNodes.add(newActivity);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#compareState(it.slumdroid.droidmodels.model.ActivityState)
	 */
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

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#checkForExploration()
	 */
	public final boolean checkForExploration() {		
		return !this.positiveComparation;		
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#getComparator()
	 */
	public CompositionalComparator getComparator() {
		return this.comparator;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#setComparator(it.slumdroid.tool.components.exploration.CompositionalComparator)
	 */
	public void setComparator(CompositionalComparator comparator) {
		this.comparator = comparator;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#setTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void setTask(Task theTask) {
		this.theTask = theTask;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Strategy#getTask()
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
