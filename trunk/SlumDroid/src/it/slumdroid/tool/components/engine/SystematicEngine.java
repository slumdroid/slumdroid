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

package it.slumdroid.tool.components.engine;

import static it.slumdroid.tool.Resources.CLASS_NAME;
import static it.slumdroid.tool.Resources.PACKAGE_NAME;
import static it.slumdroid.tool.Resources.SCREENSHOT_ENABLED;
import static it.slumdroid.tool.Resources.SLEEP_AFTER_TASK;
import static it.slumdroid.tool.Resources.TAG;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.xml.XmlGraph;
import it.slumdroid.tool.components.abstractor.GuiTreeAbstractor;
import it.slumdroid.tool.components.automation.Automation;
import it.slumdroid.tool.components.exploration.CompositionalComparator;
import it.slumdroid.tool.components.exploration.ExplorationStrategy;
import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.components.persistence.ResumingPersistence;
import it.slumdroid.tool.components.planner.Plan;
import it.slumdroid.tool.components.planner.UltraPlanner;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.utilities.AllPassFilter;
import it.slumdroid.tool.utilities.SessionParams;
import it.slumdroid.tool.utilities.UserFactory;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import android.util.Log;
import android.view.KeyEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class SystematicEngine.
 */
@SuppressWarnings("rawtypes")
public class SystematicEngine extends android.test.ActivityInstrumentationTestCase2 implements SaveStateListener {

	/** The Constant ACTOR_NAME. */
	public final static String ACTOR_NAME = "SystematicEngine";

	/** The Constant PARAM_NAME. */
	private final static String PARAM_NAME = "taskId";

	/** The id. */
	private int id = 0;

	/** The abstractor. */
	private GuiTreeAbstractor theAbstractor;

	/** The automation. */
	private Automation theAutomation;

	/** The persistence. */
	private Persistence thePersistence;

	/** The persistence factory. */
	protected PersistenceFactory thePersistenceFactory;

	/** The planner. */
	private UltraPlanner thePlanner;

	/** The scheduler. */
	private TraceDispatcher theScheduler;

	/** The strategy. */
	private Strategy theStrategy;

	/**
	 * Instantiates a new systematic engine.
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public SystematicEngine () throws ClassNotFoundException {
		super(Class.forName(CLASS_NAME));
		PersistenceFactory.registerForSavingState(this);
		setScheduler(new TraceDispatcher());
		setAutomation(new Automation());
		try {
			GuiTree.setValidation(false);
			setAbstractor(new GuiTreeAbstractor());
			getTheGuiTree().setDateTime(new GregorianCalendar().getTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPlanner (new UltraPlanner());
		AllPassFilter inputFilter = new AllPassFilter();
		getPlanner().setInputFilter (inputFilter);
		getAbstractor().addFilter (inputFilter);
		AllPassFilter eventFilter = new AllPassFilter();
		getPlanner().setEventFilter (eventFilter);
		getAbstractor().addFilter (eventFilter);
		getPlanner().setUser(UserFactory.getUser(getAbstractor()));
		getPlanner().setFormFiller(UserFactory.getUser(getAbstractor()));
		getPlanner().setAbstractor(getAbstractor());
		setPersistenceFactory(new PersistenceFactory (getTheGuiTree(), getScheduler()));
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp () throws Exception {
		super.setUp();
		setStrategy (new ExplorationStrategy(new CompositionalComparator()));
		getPersistenceFactory().setStrategy(this.theStrategy);
		setPersistence (getPersistenceFactory().getPersistence());
		getAutomation().bind(this);
		getAutomation().getExtractor().extractState();
		getPersistence().setContext(Automation.getCurrentActivity());
		ActivityDescription description = getAutomation().getExtractor().describeActivity();
		getAbstractor().setBaseActivity(description);
		if (!resume()) {
			setupFirstStart();
		}
	}	

	/**
	 * Setup first start.
	 */
	protected void setupFirstStart() {
		Log.i(TAG, "Ripping starts");
		ActivityState baseActivity = getAbstractor().getBaseActivity(); 
		getStrategy().addState(baseActivity);
		Log.i(TAG, "Initial Start Activity State saved");
		planFirstTests(baseActivity);
		if (SCREENSHOT_ENABLED) {
			getAutomation().wait(1000);
			takeScreenshot (baseActivity);
		}
	}

	/**
	 * Test and crawl.
	 */
	public void testAndCrawl() {
		for (Task theTask: getScheduler()) {
			getStrategy().setTask(theTask);
			getAutomation().execute(theTask);
			ActivityDescription theDescription = getAutomation().getExtractor().describeActivity();
			ActivityState theState = getAbstractor().createActivity(theDescription);
			if (theState.isExit()) {
				Log.i(TAG, "Exit state");
				try {
					int homeButton = KeyEvent.KEYCODE_HOME;
					String command = "adb shell input keyevent " + homeButton;
					Runtime.getRuntime().exec(command);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				getStrategy().compareState(theState);
				if (SCREENSHOT_ENABLED) {
					takeScreenshot(theState);
				}
			}
			if (SLEEP_AFTER_TASK != 0) {
				getAutomation().wait(SLEEP_AFTER_TASK);
			}
			getAbstractor().setFinalActivity (theTask, theState);
			getPersistence().addTask(theTask);
			if (canPlanTests(theState)) {
				planTests(theTask, theState);
			}
			break;
		}
	}

	/**
	 * Can plan tests.
	 *
	 * @param theState the the state
	 * @return true, if successful
	 */
	protected boolean canPlanTests (ActivityState theState){
		return !theState.isExit() && getStrategy().checkForExploration();
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if ((getStrategy().getTask() != null) 
				&& (getStrategy().getTask().isFailed())) {
			getTheGuiTree().addFailedTask(getStrategy().getTask());
		}
		getPersistence().save();
		Automation.getRobotium().finishOpenedActivities();
		super.tearDown();
	}

	/**
	 * Resume.
	 *
	 * @return true, if successful
	 */
	public boolean resume() {
		if (!(getPersistence() instanceof ResumingPersistence)) {
			return false;
		}
		ResumingPersistence resumer = (ResumingPersistence)getPersistence();
		if (!resumer.canHasResume()) {
			return false;
		}
		importTaskList(resumer);		
		importActivitiyList(resumer);
		resumer.loadParameters();
		resumer.setNotFirst();
		resumer.saveStep();
		return true;
	}

	/**
	 * Import activitiy list.
	 *
	 * @param resumer the resumer
	 */
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

	/**
	 * Import task list.
	 *
	 * @param resumer the resumer
	 */
	public void importTaskList(ResumingPersistence resumer) {
		Session sandboxSession = getNewSession();
		List<String> entries = resumer.readTaskFile();
		List<Task> taskList = new ArrayList<Task>();
		for (String row: entries) {
			sandboxSession.parse(row);
			Element element = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
			Task task = getAbstractor().importTask (element);
			if (task.isFailed()) {
				getTheGuiTree().addCrashedTask(task);
			} else {
				taskList.add(task);
			}
		}
		getScheduler().addTasks(taskList);
	}

	/**
	 * Plan first tests.
	 *
	 * @param theState the state
	 */
	protected void planFirstTests (ActivityState theState) {
		planTests (null, theState);
	}

	/**
	 * Plan tests.
	 *
	 * @param theTask the task
	 * @param theState the state
	 */
	protected void planTests (Task theTask, ActivityState theState) {
		Plan thePlan = getPlanner().getPlanForActivity(theState);
		planTests (theTask, thePlan);
	}

	/**
	 * Plan tests.
	 *
	 * @param baseTask the base task
	 * @param thePlan the plan
	 */
	protected void planTests (Task baseTask, Plan thePlan) {
		List<Task> tasks = new ArrayList<Task>();
		for (Transition transition: thePlan) {
			tasks.add(getNewTask(baseTask, transition));
		}
		getScheduler().addPlannedTasks(tasks);
	}

	/**
	 * Gets the new task.
	 *
	 * @param theTask the task
	 * @param transition the transition
	 * @return the new task
	 */
	protected Task getNewTask (Task theTask, Transition transition) {
		Task newTask = getAbstractor().createTask(theTask, transition);
		newTask.setId(nextId());
		return newTask;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onSavingState()
	 */
	public SessionParams onSavingState () {
		return new SessionParams (PARAM_NAME, this.id);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onLoadingState(it.slumdroid.tool.utilities.SessionParams)
	 */
	public void onLoadingState(SessionParams sessionParams) {
		this.id = sessionParams.getInt(PARAM_NAME);
	}

	/**
	 * Gets the automation.
	 *
	 * @return the automation
	 */
	public Automation getAutomation() {
		return theAutomation;
	}

	/**
	 * Sets the automation.
	 *
	 * @param theAutomation the new automation
	 */
	public void setAutomation(Automation theAutomation) {
		this.theAutomation = theAutomation;
	}

	/**
	 * Gets the abstractor.
	 *
	 * @return the abstractor
	 */
	public GuiTreeAbstractor getAbstractor() {
		return this.theAbstractor;
	}

	/**
	 * Sets the abstractor.
	 *
	 * @param theAbstractor the new abstractor
	 */
	public void setAbstractor(GuiTreeAbstractor theAbstractor) {
		this.theAbstractor = theAbstractor;
	}

	/**
	 * Gets the gui tree.
	 *
	 * @return the gui tree
	 */
	private GuiTree getTheGuiTree() {
		return getAbstractor().getTheSession();
	}

	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
	public Persistence getPersistence() {
		return this.thePersistence;
	}

	/**
	 * Sets the persistence.
	 *
	 * @param thePersistence the new persistence
	 */
	public void setPersistence(Persistence thePersistence) {
		this.thePersistence = thePersistence;
	}

	/**
	 * Gets the persistence factory.
	 *
	 * @return the persistence factory
	 */
	public PersistenceFactory getPersistenceFactory() {
		return thePersistenceFactory;
	}

	/**
	 * Sets the persistence factory.
	 *
	 * @param thePersistenceFactory the new persistence factory
	 */
	public void setPersistenceFactory(PersistenceFactory thePersistenceFactory) {
		this.thePersistenceFactory = thePersistenceFactory;
	}

	/**
	 * Gets the planner.
	 *
	 * @return the planner
	 */
	public UltraPlanner getPlanner() {
		return this.thePlanner;
	}

	/**
	 * Sets the planner.
	 *
	 * @param thePlanner the new planner
	 */
	public void setPlanner(UltraPlanner thePlanner) {
		this.thePlanner = thePlanner;
	}

	/**
	 * Gets the scheduler.
	 *
	 * @return the scheduler
	 */
	public TraceDispatcher getScheduler () {
		return this.theScheduler;
	}

	/**
	 * Sets the scheduler.
	 *
	 * @param theScheduler the new scheduler
	 */
	public void setScheduler (TraceDispatcher theScheduler) {
		this.theScheduler = theScheduler;
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
	 * Gets the last id.
	 *
	 * @return the last id
	 */
	public int getLastId() {
		return this.id;
	}

	/**
	 * Next id.
	 *
	 * @return the string
	 */
	protected String nextId () {
		int num = id;
		id++;
		return String.valueOf(num);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#getListenerName()
	 */
	public String getListenerName () {
		return ACTOR_NAME;
	}

	/**
	 * Take screenshot.
	 *
	 * @param theState the state
	 */
	private void takeScreenshot(ActivityState theState) {
		try {
			String fileName = theState.getUniqueId() + ".png";
			String command = "adb shell screencap -p " + "/data/data/" + PACKAGE_NAME + "/files/" + fileName;
			Runtime.getRuntime().exec(command);
			Log.i(TAG,"Saved image on disk: " + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			theState.setScreenshot(new String());
		}
	}

	/**
	 * Gets the new session.
	 *
	 * @return the new session
	 */
	public Session getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

}