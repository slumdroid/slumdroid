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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.tool.components.exploration.ExplorationStrategy;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.Strategy;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Persistence objects.
 */
public class PersistenceFactory {

	/** The activity list file name. */
	private final String ACTIVITY_LIST_FILE_NAME = new String("activities.xml"); 

	/** The parameters file name. */
	private final String PARAMETERS_FILE_NAME = new String("parameters.obj"); 

	/** The task list file name. */
	private final String TASK_LIST_FILE_NAME = new String("tasklist.xml");

	/** The session. */
	private Session theSession;

	/** The scheduler. */
	private TraceDispatcher scheduler;

	/** The strategy. */
	private Strategy theStrategy;

	/** The state savers. */
	static List<SaveStateListener> stateSavers = new ArrayList<SaveStateListener>();

	/**
	 * Instantiates a new persistence factory.
	 *
	 * @param theSession the the session
	 */
	public PersistenceFactory(Session theSession) {
		setTheSession(theSession);
	}

	/**
	 * Instantiates a new persistence factory.
	 *
	 * @param theSession the the session
	 * @param scheduler the scheduler
	 */
	public PersistenceFactory(Session theSession, TraceDispatcher scheduler) {
		this (theSession);
		setDispatcher(scheduler);
	}

	/**
	 * Instantiates a new persistence factory.
	 *
	 * @param theSession the the session
	 * @param scheduler the scheduler
	 * @param theStrategy the the strategy
	 */
	public PersistenceFactory(Session theSession, TraceDispatcher scheduler, Strategy theStrategy) {
		this (theSession, scheduler);
		setStrategy(theStrategy);
	}

	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
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

	/**
	 * Gets the the session.
	 *
	 * @return the the session
	 */
	public Session getTheSession() {
		return this.theSession;
	}

	/**
	 * Sets the the session.
	 *
	 * @param theSession the new the session
	 */
	public void setTheSession(Session theSession) {
		this.theSession = theSession;
	}

	/**
	 * Gets the dispatcher.
	 *
	 * @return the dispatcher
	 */
	public TraceDispatcher getDispatcher() {
		return this.scheduler;
	}

	/**
	 * Sets the dispatcher.
	 *
	 * @param scheduler the new dispatcher
	 */
	public void setDispatcher(TraceDispatcher scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Gets the strategy.
	 *
	 * @return the strategy
	 */
	public Strategy getStrategy() {
		return this.theStrategy;
	}

	/**
	 * Sets the strategy.
	 *
	 * @param theStrategy the new strategy
	 */
	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	/**
	 * Register for saving state.
	 *
	 * @param listener the listener
	 */
	public static void registerForSavingState (SaveStateListener listener) {
		stateSavers.add(listener);
	}

}
