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

import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.ImageStorage;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.utilities.ScreenshotFactory;
import it.slumdroid.tool.utilities.strategy.CustomStrategy;

import java.util.ArrayList;
import java.util.List;

import it.slumdroid.droidmodels.model.Session;

public class PersistenceFactory {

	private final static String ACTIVITY_LIST_FILE_NAME = "activities.xml"; 
	private final static String PARAMETERS_FILE_NAME = "parameters.obj"; 
	private final static String TASK_LIST_FILE_NAME = "tasklist.xml";

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
		Persistence thePersistence;
		ResumingPersistence resumer = new ResumingPersistence();
		thePersistence = resumer;
		resumer.setTaskList(getDispatcher().getScheduler().getTaskList());
		resumer.setTaskListFile(TASK_LIST_FILE_NAME);
		resumer.setActivityFile(ACTIVITY_LIST_FILE_NAME);
		resumer.setParametersFile(PARAMETERS_FILE_NAME);
		getStrategy().registerTerminationListener(resumer);
		for (SaveStateListener saver: stateSavers) {
			resumer.registerListener(saver);
		}
		getDispatcher().registerListener(resumer);
		if (getStrategy() instanceof CustomStrategy) {
			((CustomStrategy)getStrategy()).registerStateListener(resumer);				
		}
		thePersistence.setSession(getTheSession());
		if (thePersistence instanceof ImageStorage) {
			ScreenshotFactory.setTheImageStorage((ImageStorage)thePersistence);
		}
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
