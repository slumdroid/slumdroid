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

import static it.slumdroid.tool.Resources.TAG;
import static it.slumdroid.tool.Resources.ENABLE_MODEL;
import static it.slumdroid.tool.Resources.COMPARATOR_TYPE;
import static it.slumdroid.tool.Resources.NULL_COMPARATOR;

import it.slumdroid.tool.model.Comparator;
import it.slumdroid.tool.model.PauseCriteria;
import it.slumdroid.tool.model.StateDiscoveryListener;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.model.TerminationCriteria;
import it.slumdroid.tool.model.TerminationListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Trace;

public class SimpleStrategy implements Strategy {

	private HashSet<ActivityState> guiNodes = new HashSet<ActivityState> ();
	private Comparator c;
	protected Collection<TerminationCriteria> terminators = new ArrayList<TerminationCriteria>();
	protected Collection<PauseCriteria> pausers = new ArrayList<PauseCriteria>();
	protected boolean positiveComparation = true;
	private Trace theTask;
	private ActivityState beforeEvent;
	private ActivityState afterEvent;
	private List<StateDiscoveryListener> theListeners = new ArrayList<StateDiscoveryListener>();
	private List<TerminationListener> endListeners = new ArrayList<TerminationListener>();

	public SimpleStrategy (Comparator c) {
		super();
		setComparator(c);
	}

	public void addState(ActivityState newActivity) {
		for (StateDiscoveryListener listener: getListeners()) {
			listener.onNewState(newActivity);
		}
		this.guiNodes.add(newActivity);
	}

	public boolean compareState(ActivityState theActivity) {
		this.afterEvent = theActivity;
		this.positiveComparation = true;
		String name = theActivity.getName();
		if (theActivity.isExit()) {
			Log.i(TAG, "Exit state. Not performing comparation for activity " + name);
			return false;
		}

		if (!COMPARATOR_TYPE.equals(NULL_COMPARATOR)){
			for (ActivityState stored: guiNodes) {
				if (getComparator().compare(theActivity, stored)) {
					theActivity.setId(stored.getId());
					Log.i(TAG, "This activity state is equivalent to " + stored.getId());
					return true;
				}
			}	
		}

		this.positiveComparation = false;
		if (ENABLE_MODEL) {
			if (!COMPARATOR_TYPE.equals(NULL_COMPARATOR)) Log.i(TAG, "Registering activity " + name + " (id: " + theActivity.getId() + ") as a new found state");
			addState (theActivity);
		}
		return false;
	}

	public boolean checkForTermination () { // Logic OR of the criterias
		for (TerminationCriteria t: this.terminators) {
			if (t.termination()) {
				for (TerminationListener tl: getEndListeners()) {
					tl.onTerminate();
				}
				return true;
			}
		}
		return false;
	}

	public boolean checkForPause () { // Logic OR of the criterias
		for (PauseCriteria p: this.pausers) {
			if (p.pause()) return true;
		}
		return false;
	}

	public void addTerminationCriteria (TerminationCriteria t) {
		t.setStrategy(this);
		this.terminators.add(t);
	}

	public void addPauseCriteria (PauseCriteria p) {
		p.setStrategy(this);
		this.pausers.add(p);
	}

	public Comparator getComparator() {
		return this.c;
	}

	public void setComparator(Comparator c) {
		this.c = c;
	}

	public final boolean checkForExploration() {
		return explorationNeeded();
	}

	protected boolean explorationNeeded() {
		return !isLastComparationPositive();
	}

	public boolean isLastComparationPositive() {
		return positiveComparation;
	}

	public void setTask(Trace theTask) {
		this.theTask = theTask;
		this.beforeEvent = theTask.getFinalTransition().getStartActivity();
	}

	public Trace getTask () {
		return this.theTask;
	}

	public ActivityState getStateBeforeEvent () {
		return this.beforeEvent;
	}

	public ActivityState getStateAfterEvent () {
		return this.afterEvent;
	}

	public List<StateDiscoveryListener> getListeners() {
		return this.theListeners;
	}

	public List<TerminationListener> getEndListeners() {
		return this.endListeners;
	}

	public void registerStateListener(StateDiscoveryListener theListener) {
		this.theListeners.add(theListener);
	}

	public void registerTerminationListener(TerminationListener theListener) {
		this.endListeners.add(theListener);
	}

}
