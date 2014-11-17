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

import it.slumdroid.tool.components.persistence.SaveStateRandom;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.components.strategy.criteria.OnExitPause;
import it.slumdroid.tool.model.Plan;

import java.util.Random;

import android.util.Log;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import static it.slumdroid.tool.Resources.*;
import static it.slumdroid.tool.Resources.SchedulerAlgorithm.DEPTH_FIRST;

public class RandomEngine extends SystematicEngine {

	Random taskLottery;
	boolean first;

	public RandomEngine () {
		super();
		Log.i(TAG, "Starting Random Testing");
		setMaxEventsSelector(0);
		setMaxTasksInScheduler(2);
		setOnlyFinalTransition(true);
		setPauseTasks(0);
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
	protected void planTests (Task theTask, Plan thePlan) {
		int n;
		int max;
		Transition t;
		while (!thePlan.isEmpty()) {
			max = thePlan.size();
			n = taskLottery.nextInt(max);
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
	protected void process(Task theTask) {
		if (this.first) super.process(theTask);
		else getExecutor().process(theTask.getFinalTransition());
		this.first=false;
	}

}
