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

package it.slumdroid.tool.components.engine;

import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.Plan;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.SessionParams;
import it.slumdroid.tool.utilities.strategy.criteria.OnExitPause;

import java.util.Random;

import android.util.Log;
import it.slumdroid.droidmodels.model.Trace;
import it.slumdroid.droidmodels.model.Transition;
import static it.slumdroid.tool.Resources.*;
import static it.slumdroid.tool.components.scheduler.TraceDispatcher.SchedulerAlgorithm.DEPTH_FIRST;

public class RandomEngine extends SystematicEngine {

	Random taskLottery;
	boolean first;

	public RandomEngine () {
		super();
		Log.i(TAG, "Starting Random Testing");

		setMaxEventsSelector(0);
		setMaxTasksInScheduler(2);
		setOnlyFinalTransition(true);
		setPauseTraces(0);

		this.taskLottery = new SaveStateRandom(RANDOM_SEED);
		this.theStrategyFactory.setMoreCriterias(new OnExitPause());

		this.first = true;
	}

	@Override
	protected void setUp () {
		super.setUp();
	}

	@Override
	protected void setupAfterResume() {
		planFirstTests(getAbstractor().getBaseActivity());
	}

	@Override
	protected void planTests (Trace theTask, Plan thePlan) {
		int n;
		int max;
		Transition t;
		while (!thePlan.isEmpty()) {
			max = thePlan.size();
			n = getRandom(max);
			t = thePlan.getTask(n);
			getScheduler().addTasks(getNewTask(theTask, t));
			thePlan.removeTask(n);
		}
	}

	@Override
	protected void doNotPlanTests() { 
		this.first = true; 
	}


	@Override
	public TraceDispatcher getNewScheduler() {
		return new TraceDispatcher(DEPTH_FIRST);
	}

	@Override
	protected void process(Trace theTask) {
		if (this.first) super.process(theTask);
		else getExecutor().process(theTask.getFinalTransition());
		this.first=false;
	}

	public int getRandom (int max) {
		int n = taskLottery.nextInt(max);
		return n;
	}

	@SuppressWarnings("serial")
	class SaveStateRandom extends Random implements SaveStateListener {

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

}
