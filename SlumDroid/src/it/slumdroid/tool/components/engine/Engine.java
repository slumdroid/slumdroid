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

import static it.slumdroid.tool.Resources.SCREENSHOT_ENABLED;
import static it.slumdroid.tool.Resources.SLEEP_AFTER_TASK;
import static it.slumdroid.tool.Resources.TAG;
import static it.slumdroid.tool.Resources.theClass;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.xml.XmlGraph;
import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.components.persistence.ResumingPersistence;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Executor;
import it.slumdroid.tool.model.Extractor;
import it.slumdroid.tool.model.ImageCaptor;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.Plan;
import it.slumdroid.tool.model.Planner;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.SessionParams;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.utilities.ScreenshotFactory;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

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
		if (getImageCaptor() != null) ScreenshotFactory.setImageCaptor(getImageCaptor());
		getExecutor().bind(this);
		Activity activity = getExtractor().getActivity();
		getPersistence().setContext(activity);
		getPersistence().setFileName(FILE_NAME);
		if (resume()) setupAfterResume();
		else setupFirstStart();
	}

	protected void setupFirstStart() {
		Log.i(TAG, "Ripping starts");
		getExtractor().extractState();
		ActivityDescription description = getExtractor().describeActivity();
		getAbstractor().setBaseActivity(description);
		ActivityState baseActivity = getAbstractor().getBaseActivity(); 
		getStrategy().addState(baseActivity);
		Log.i(TAG, "Start Activity State saved");
		if (SCREENSHOT_ENABLED) takeScreenshot (baseActivity);
		planFirstTests(baseActivity);
	}

	protected void setupAfterResume() {
		// do nothing
	}

	public void testAndCrawl() {
		for (Task theTask: getScheduler()) {
			getStrategy().setTask(theTask);
			process(theTask);
			ActivityDescription description = getExtractor().describeActivity();
			ActivityState theActivity = getAbstractor().createActivity(description);
			getStrategy().compareState(theActivity);
			if (SCREENSHOT_ENABLED) takeScreenshot(theActivity);
			if (SLEEP_AFTER_TASK != 0) getExecutor().wait(SLEEP_AFTER_TASK);
			getAbstractor().setFinalActivity (theTask, theActivity);
			getPersistence().addTask(theTask);
			if (canPlanTests(theActivity)) planTests(theTask, theActivity);
			else doNotPlanTests();
			if (getStrategy().checkForPause()) break;
		}
	}

	protected void process(Task theTask) {
		getExecutor().process(theTask);
	}

	protected boolean canPlanTests (ActivityState theActivity){
		return !(theActivity.isExit()) && getStrategy().checkForExploration();
	}

	@Override
	protected void tearDown() throws Exception {
		if ((getStrategy().getTask() != null) && (getStrategy().getTask().isFailed())) {
			getSession().addFailedTask(getStrategy().getTask());
		}
		getPersistence().save();
		super.tearDown();
	}

	public boolean resume() {
		if (!(getPersistence() instanceof ResumingPersistence)) {
			return false;
		}
		ResumingPersistence resumer = (ResumingPersistence)getPersistence();
		if (!resumer.canHasResume()) return false;
		importTaskList(resumer);		
		importActivitiyList(resumer);
		resumer.loadParameters();
		resumer.setNotFirst();
		resumer.saveStep();
		return true;
	}

	public void importActivitiyList(ResumingPersistence resumer) {
		Session sandboxSession = getNewSession();
		List<String> entries = resumer.readStateFile();
		List<ActivityState> stateList = new ArrayList<ActivityState>();
		for (String row: entries) {
			sandboxSession.parse(row);
			Element element = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			ActivityState state = getAbstractor().importState (element);
			stateList.add(state);
		}
		for (ActivityState state: stateList) {
			getStrategy().addState(state);
		}
	}

	public void importTaskList(ResumingPersistence r) {
		Session sandboxSession = getNewSession();
		List<String> entries = r.readTaskFile();
		List<Task> taskList = new ArrayList<Task>();
		for (String row: entries) {
			sandboxSession.parse(row);
			Element element = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			Task task = getAbstractor().importTask (element);
			if (task.isFailed()) getSession().addCrashedTask(task);
			else taskList.add(task);
		}
		getScheduler().addTasks(taskList);
	}

	protected void planFirstTests (ActivityState theActivity) {
		planTests (null, theActivity);
	}

	protected void planTests (Task theTask, ActivityState theActivity) {
		Plan thePlan = getPlanner().getPlanForActivity(theActivity);
		planTests (theTask, thePlan);
	}

	protected void planTests (Task baseTask, Plan thePlan) {
		List<Task> tasks = new ArrayList<Task>();
		for (Transition transition: thePlan) {
			tasks.add(getNewTask(baseTask, transition));
		}
		getScheduler().addPlannedTasks(tasks);
	}

	protected void doNotPlanTests() { 
		// do nothing 
	}

	protected Task getNewTask (Task theTask, Transition t) {
		Task newTask = getAbstractor().createTask(theTask, t);
		newTask.setId(nextId());
		return newTask;
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

	public String screenshotName (String stateId) {
		return stateId + "." + ScreenshotFactory.getFileExtension();
	}

	private void takeScreenshot(ActivityState theActivity) {
		if (theActivity.isExit()) return;
		String fileName = screenshotName(theActivity.getUniqueId());
		if (ScreenshotFactory.saveScreenshot(fileName)) {
			theActivity.setScreenshot(fileName);
			Log.i(TAG,"Saved image on disk: " + fileName);
		} else{
			Log.i(TAG,"Image is not saved on disk: " + fileName);
		}
	}

}
