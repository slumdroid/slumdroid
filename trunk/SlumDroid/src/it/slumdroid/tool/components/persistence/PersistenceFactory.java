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

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.tool.components.exploration.ExplorationStrategy;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.Strategy;

import java.util.ArrayList;
import java.util.List;

public class PersistenceFactory {

	private final String ACTIVITY_LIST_FILE_NAME = new String("activities.xml"); 
	private final String PARAMETERS_FILE_NAME = new String("parameters.obj"); 
	private final String TASK_LIST_FILE_NAME = new String("tasklist.xml");

	private Session theSession;
	private TraceDispatcher scheduler;
	private Strategy theStrategy;
	static List<SaveStateListener> stateSavers = new ArrayList<SaveStateListener>();

	public PersistenceFactory(Session theSession) {
		setTheSession(theSession);
	}

	public PersistenceFactory(Session theSession, TraceDispatcher scheduler) {
		this (theSession);
		setDispatcher(scheduler);
	}

	public PersistenceFactory(Session theSession, TraceDispatcher scheduler, Strategy theStrategy) {
		this (theSession, scheduler);
		setStrategy(theStrategy);
	}

	public Persistence getPersistence () {
		ResumingPersistence resumer = new ResumingPersistence();
		Persistence thePersistence = resumer;
		resumer.setTaskList(getDispatcher().getScheduler().getTaskList());
		resumer.setTaskListFile(TASK_LIST_FILE_NAME);
		resumer.setActivityFile(ACTIVITY_LIST_FILE_NAME);
		resumer.setParametersFile(PARAMETERS_FILE_NAME);
		for (SaveStateListener saver: stateSavers) {
			resumer.registerListener(saver);
		}
		getDispatcher().registerListener(resumer);
		if (getStrategy() instanceof ExplorationStrategy) {
			((ExplorationStrategy)getStrategy()).registerStateListener(resumer);				
		}
		thePersistence.setSession(getTheSession());
		return thePersistence;
	}

	public Session getTheSession() {
		return this.theSession;
	}

	public void setTheSession(Session theSession) {
		this.theSession = theSession;
	}

	public TraceDispatcher getDispatcher() {
		return this.scheduler;
	}

	public void setDispatcher(TraceDispatcher scheduler) {
		this.scheduler = scheduler;
	}

	public Strategy getStrategy() {
		return this.theStrategy;
	}

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	public static void registerForSavingState (SaveStateListener s) {
		stateSavers.add(s);
	}

}
