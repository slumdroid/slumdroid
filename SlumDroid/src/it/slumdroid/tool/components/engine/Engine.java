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
import it.slumdroid.tool.components.persistence.ResumingPersistence;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.*;
import it.slumdroid.tool.utilities.ScreenshotFactory;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import it.slumdroid.droidmodels.model.*;
import it.slumdroid.droidmodels.xml.XmlGraph;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import static it.slumdroid.tool.Resources.*;

@SuppressWarnings("rawtypes")
public abstract class Engine extends ActivityInstrumentationTestCase2 implements SaveStateListener {

	public final static String ACTOR_NAME = "Engine";

	private Executor theExecutor;
	private Extractor theExtractor;
	private Abstractor theAbstractor;
	private Planner thePlanner;
	private TraceDispatcher theScheduler;
	private Strategy theStrategy;
	private Persistence thePersistence;
	private Session theSession;
	private ImageCaptor theImageCaptor;

	private final static String PARAM_NAME = "taskId";
	private final static String FILE_NAME = "guitree.xml"; 
	private int id = 0;

	@SuppressWarnings("unchecked")
	public Engine() {
		super(theClass);
		PersistenceFactory.registerForSavingState(this);
	}

	public abstract Session getNewSession();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (getImageCaptor()!=null) ScreenshotFactory.setImageCaptor(getImageCaptor());
		getExecutor().bind(this);
		getExtractor().extractState();
		Activity a = getExtractor().getActivity();
		getPersistence().setFileName(FILE_NAME);
		getPersistence().setContext(a);
		ActivityDescription d = getExtractor().describeActivity();
		getAbstractor().setBaseActivity(d);
		if (resume()) setupAfterResume();
		else setupFirstStart();
	}

	protected void setupFirstStart() {
		ActivityState baseActivity = getAbstractor().getBaseActivity(); 
		if (ENABLE_MODEL) {
			getStrategy().addState(baseActivity);
			Log.i(TAG, "Start Activity saved");
		}
		if (screenshotEnabled()) takeScreenshot (baseActivity);
		planFirstTests(baseActivity);
	}

	protected void setupAfterResume() {
		// do nothing
	}

	public void testAndCrawl() {
		for (Trace theTask: getScheduler()) {
			getStrategy().setTask(theTask);
			process(theTask);
			ActivityDescription d = getExtractor().describeActivity();
			ActivityState theActivity = getAbstractor().createActivity(d);
			getStrategy().compareState(theActivity);
			if (screenshotEnabled()) takeScreenshot(theActivity);
			getExecutor().wait(SLEEP_AFTER_TASK);
			getAbstractor().setFinalActivity (theTask, theActivity);
			getPersistence().addTrace(theTask);
			if (canPlanTests(theActivity)) planTests(theTask, theActivity);
			else doNotPlanTests();
			if ( (getStrategy().checkForTermination()) || (getStrategy().checkForPause()) ) break;
		}
	}

	protected void process(Trace theTask) {
		getExecutor().process(theTask);
	}

	protected boolean canPlanTests (ActivityState theActivity){
		return (!(theActivity.isExit()) && getStrategy().checkForExploration());
	}

	@Override
	protected void tearDown() throws Exception {
		if ((getStrategy().getTask() != null) && (getStrategy().getTask().isFailed())) {
			getSession().addFailedTrace(getStrategy().getTask());
		}
		getPersistence().save();
		getExecutor().finalize();
		super.tearDown();
	}

	public boolean resume() {
		if (!(getPersistence() instanceof ResumingPersistence)) {
			return false;
		}

		ResumingPersistence r = (ResumingPersistence)getPersistence();
		if (!r.canHasResume()) return false;

		importTaskList(r);		
		importActivitiyList(r);

		r.loadParameters();
		r.setNotFirst();
		r.saveStep();

		return true;
	}

	public void importActivitiyList(ResumingPersistence r) {
		if (getStrategy().getComparator() instanceof StatelessComparator) {
			return;
		}
		List<String> entries;
		Session sandboxSession = getNewSession();
		Element e;
		entries = r.readStateFile();
		List<ActivityState> stateList = new ArrayList<ActivityState>();
		ActivityState s;
		for (String state: entries) {
			sandboxSession.parse(state);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			s = getAbstractor().importState (e);
			stateList.add(s);
		}
		for (ActivityState state: stateList) {
			getStrategy().addState(state);
		}
	}

	public void importTaskList(ResumingPersistence r) {
		List<String> entries;
		Session sandboxSession = getNewSession();
		Element e;
		entries = r.readTaskFile();
		List<Trace> taskList = new ArrayList<Trace>();
		Trace t;
		for (String trace: entries) {
			sandboxSession.parse(trace);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			t = getAbstractor().importTask (e);
			if (t.isFailed()) 
				getSession().addCrashedTrace(t);
			else 
				taskList.add(t);
		}
		getScheduler().addTasks(taskList);
	}

	protected void planFirstTests (ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForActivity(theActivity);
		planTests (null, thePlan);
	}

	protected void planTests (Trace theTask, ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForActivity(theActivity);
		planTests (theTask, thePlan);
	}

	protected void planTests (Trace baseTask, Plan thePlan) {
		List<Trace> tasks = new ArrayList<Trace>();
		for (Transition t: thePlan) {
			tasks.add(getNewTask(baseTask, t));
		}
		getScheduler().addPlannedTasks(tasks);
	}

	protected void doNotPlanTests() { 
		// do nothing 
	}

	protected Trace getNewTask (Trace theTask, Transition t) {
		Trace newTrace = getAbstractor().createTrace(theTask, t);
		newTrace.setId(nextId());
		return newTrace;
	}

	public SessionParams onSavingState () {
		return new SessionParams (PARAM_NAME, this.id);
	}

	public void onLoadingState(SessionParams sessionParams) {
		this.id = sessionParams.getInt(PARAM_NAME);
	}

	public Executor getExecutor() {
		return this.theExecutor;
	}

	public void setExecutor(Executor theExecutor) {
		this.theExecutor = theExecutor;
	}

	public Extractor getExtractor() {
		return this.theExtractor;
	}

	public void setExtractor(Extractor theExtractor) {
		this.theExtractor = theExtractor;
	}

	public ImageCaptor getImageCaptor() {
		return this.theImageCaptor;
	}

	public void setImageCaptor(ImageCaptor theImageCaptor) {
		this.theImageCaptor = theImageCaptor;
	}

	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}

	public void setAbstractor(Abstractor theAbstractor) {
		this.theAbstractor = theAbstractor;
	}

	public Planner getPlanner() {
		return this.thePlanner;
	}

	public void setPlanner(Planner thePlanner) {
		this.thePlanner = thePlanner;
	}

	public TraceDispatcher getScheduler () {
		return this.theScheduler;
	}

	public void setScheduler (TraceDispatcher theScheduler) {
		this.theScheduler = theScheduler;
	}

	public Strategy getStrategy() {
		return this.theStrategy;
	}

	public void setStrategy(Strategy theStrategy) {
		this.theStrategy = theStrategy;
	}

	public Persistence getPersistence() {
		return this.thePersistence;
	}

	public void setPersistence(Persistence thePersistence) {
		this.thePersistence = thePersistence;
	}

	public Session getSession() {
		return this.theSession;
	}

	public void setSession(Session theSession) {
		this.theSession = theSession;
	}

	public int getLastId() {
		return this.id;
	}

	protected String nextId () {
		int num = id;
		id++;
		return String.valueOf(num);
	}

	public String getListenerName () {
		return ACTOR_NAME;
	}

	public boolean screenshotEnabled() {
		return SCREENSHOT_ENABLED;
	}

	public String screenshotName (String stateId) {
		return stateId + "." + ScreenshotFactory.getFileExtension();
	}

	private void takeScreenshot(ActivityState theActivity) {
		if (theActivity.isExit()) return;
		String fileName = screenshotName(theActivity.getUniqueId());
		if (ScreenshotFactory.saveScreenshot(fileName)) {
			theActivity.setScreenshot(fileName);
		}
	}

}
