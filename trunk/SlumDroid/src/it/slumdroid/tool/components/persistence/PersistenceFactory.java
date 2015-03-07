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
import it.slumdroid.tool.model.SaveStateListener;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Persistence objects.
 */
public class PersistenceFactory {

	/** The session. */
	private Session theSession;

	/** The scheduler. */
	private TraceDispatcher scheduler;

	/** The strategy. */
	private ExplorationStrategy theStrategy;

	/** The state savers. */
	static List<SaveStateListener> stateSavers = new ArrayList<SaveStateListener>();

	/**
	 * Instantiates a new persistence factory.
	 *
	 * @param theSession the session
	 * @param theScheduler the scheduler
	 * @param theStrategy the strategy
	 */
	public PersistenceFactory(Session theSession, TraceDispatcher theScheduler, ExplorationStrategy theStrategy) {
		setTheSession(theSession);
		setDispatcher(theScheduler);
		setStrategy(theStrategy);
	}

	/**
	 * Register for saving state.
	 *
	 * @param listener the listener
	 */
	public static void registerForSavingState(SaveStateListener listener) {
		stateSavers.add(listener);
	}

	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
	public ResumingPersistence getPersistence() {
		ResumingPersistence resumer = new ResumingPersistence(getTheSession());
		resumer.setTaskList(getDispatcher().getScheduler().getTaskList());
		for (SaveStateListener saver: stateSavers) {
			resumer.registerListener(saver);
		}
		getDispatcher().registerListener(resumer);
		getStrategy().registerStateListener(resumer);				
		return resumer;
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
	public ExplorationStrategy getStrategy() {
		return this.theStrategy;
	}

	/**
	 * Sets the strategy.
	 *
	 * @param theStrategy the new strategy
	 */
	public void setStrategy(ExplorationStrategy theStrategy) {
		this.theStrategy = theStrategy;
	}

}
