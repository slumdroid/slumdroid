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

package it.slumdroid.tool.components.abstractor;

import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TOAST;
import static it.slumdroid.tool.Resources.SCREENSHOT_ENABLED;
import static it.slumdroid.tool.components.abstractor.AbstractorUtilities.detectName;
import static it.slumdroid.tool.components.abstractor.AbstractorUtilities.getType;
import static it.slumdroid.tool.components.abstractor.AbstractorUtilities.setCount;
import static it.slumdroid.tool.components.abstractor.AbstractorUtilities.setValue;
import it.slumdroid.droidmodels.guitree.FinalActivity;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.guitree.StartActivity;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.model.WidgetType;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;
import it.slumdroid.droidmodels.testcase.TestCaseEvent;
import it.slumdroid.droidmodels.testcase.TestCaseInput;
import it.slumdroid.droidmodels.testcase.TestCaseTask;
import it.slumdroid.droidmodels.testcase.TestCaseTransition;
import it.slumdroid.droidmodels.testcase.TestCaseWidget;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.utilities.AllPassFilter;
import it.slumdroid.tool.utilities.SessionParams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import android.view.View;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class GuiTreeAbstractor.
 */
public class GuiTreeAbstractor implements Abstractor, SaveStateListener {

	/** The session. */
	private GuiTree theSession;

	/** The base activity. */
	private StartActivity baseActivity;

	/** The filters. */
	private HashSet<AllPassFilter> filters;

	/** The detector. */
	private TypeDetector detector;

	/** The event id. */
	private int eventId = 0;

	/** The input id. */
	private int inputId = 0;

	/** The activity id. */
	private int activityId = 0;

	/** The Constant ACTOR_NAME. */
	public final static String ACTOR_NAME = "GuiTreeAbstractor";

	/** The Constant EVENT_PARAM_NAME. */
	private static final String EVENT_PARAM_NAME = "eventId";

	/** The Constant INPUT_PARAM_NAME. */
	private static final String INPUT_PARAM_NAME = "inputId";

	/** The Constant ACTIVITY_PARAM_NAME. */
	private static final String ACTIVITY_PARAM_NAME = "activityId";

	/**
	 * Instantiates a new gui tree abstractor.
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public GuiTreeAbstractor () throws ParserConfigurationException {
		this (new GuiTree());
	}

	/**
	 * Instantiates a new gui tree abstractor.
	 *
	 * @param theSession the session
	 */
	public GuiTreeAbstractor(GuiTree theSession) {
		super();
		this.filters = new HashSet<AllPassFilter>();
		this.detector = new TypeDetector();
		setTheSession(theSession);
		PersistenceFactory.registerForSavingState(this);
	}

	/**
	 * Gets the the session.
	 *
	 * @return the the session
	 */
	public GuiTree getTheSession() {
		return this.theSession;
	}

	/**
	 * Sets the the session.
	 *
	 * @param theSession the new the session
	 */
	public void setTheSession(GuiTree theSession) {
		this.theSession = theSession;
	}

	/**
	 * Gets the type detector.
	 *
	 * @return the type detector
	 */
	public TypeDetector getTypeDetector () {
		return this.detector;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#createActivity(it.slumdroid.tool.model.ActivityDescription)
	 */
	public ActivityState createActivity (ActivityDescription theDescription) {
		return createActivity (theDescription, false);
	}

	// If the boolean parameter is omitted, the overloading method will default to a Final Activity
	/**
	 * Creates the activity.
	 *
	 * @param theDescription the description
	 * @param start the start
	 * @return the activity state
	 */
	public ActivityState createActivity (ActivityDescription theDescription, boolean start) {
		ActivityState newActivity = (start)?StartActivity.createActivity(getTheSession()):FinalActivity.createActivity(getTheSession());
		for (AllPassFilter filter: this.filters) {
			filter.clear();
		}
		boolean hasDescription = updateDescription(newActivity, theDescription);
		if (!hasDescription) {
			newActivity.markAsExit();
			newActivity.setUniqueId(getUniqueActivityId());
			return newActivity;
		}
		newActivity.setName(theDescription.getActivityName());
		newActivity.setTitle(theDescription.getActivityTitle());
		newActivity.setId(newActivity.getUniqueId());
		newActivity.setUniqueId(getUniqueActivityId());
		if (SCREENSHOT_ENABLED) {
			newActivity.setScreenshot(newActivity.getUniqueId() + ".png");	
		}
		return newActivity;
	}

	/**
	 * Creates the widget.
	 *
	 * @param view the view
	 * @return the test case widget
	 */
	public TestCaseWidget createWidget (View view) {
		TestCaseWidget widget = TestCaseWidget.createWidget(getTheSession());
		String id = String.valueOf(view.getId());
		String name = detectName(view);
		widget.setIdNameType(id, name, getType(view));
		widget.setSimpleType(getTypeDetector().getSimpleType(view));
		setCount (view,widget);
		setValue (view,widget);
		if (view instanceof TextView) {
			int type = ((TextView)view).getInputType();
			if (type != 0) {
				widget.setTextType(new WidgetType(type, name, widget.getValue()).convert());
			}	
		}
		widget.setAvailable((view.isEnabled())?"true":"false");
		widget.setClickable((view.isClickable())?"true":"false");
		widget.setLongClickable((view.isLongClickable())?"true":"false");
		return widget;
	}

	/**
	 * Update description.
	 *
	 * @param newActivity the new activity
	 * @param theDescription the description
	 * @return true, if successful
	 */
	public boolean updateDescription (ActivityState newActivity, ActivityDescription theDescription) {
		boolean hasDescription = false;
		for (View view: theDescription) {
			hasDescription = true;
			if (!view.isShown()) {
				continue;
			}
			TestCaseWidget widget = createWidget (view);
			widget.setIndex(theDescription.getWidgetIndex(view));
			if (widget.getIndex() == 0 
					&& widget.getSimpleType().equals(TEXT_VIEW)) {
				widget.setSimpleType(TOAST);
			}
			((ElementWrapper) newActivity).appendChild(widget.getElement());
			for (AllPassFilter filter: this.filters) {
				filter.loadItem(widget);
			}
		}
		return hasDescription;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#setBaseActivity(it.slumdroid.tool.model.ActivityDescription)
	 */
	public void setBaseActivity (ActivityDescription theDescription) {
		this.baseActivity = (StartActivity) createActivity(theDescription, true);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#getBaseActivity()
	 */
	public ActivityState getBaseActivity () {
		return this.baseActivity;
	}

	/**
	 * Sets the start activity.
	 *
	 * @param theStep the the step
	 * @param theActivity the the activity
	 */
	public void setStartActivity (Transition theStep, ActivityState theActivity) {
		theStep.setStartActivity (stubActivity(theActivity));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#setFinalActivity(it.slumdroid.droidmodels.model.Task, it.slumdroid.droidmodels.model.ActivityState)
	 */
	public void setFinalActivity (Task theTask, ActivityState theActivity) {
		theTask.setFinalActivity (stubActivity(theActivity));
	}

	/**
	 * Stub activity.
	 *
	 * @param theActivity the the activity
	 * @return the test case activity
	 */
	private TestCaseActivity stubActivity (ActivityState theActivity) {
		TestCaseActivity theStub = ((TestCaseActivity)theActivity).clone();
		return theStub;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	public Iterator<AllPassFilter> iterator() {
		return this.filters.iterator();
	}

	/**
	 * Adds the filter.
	 *
	 * @param filter the filter
	 */
	public void addFilter(AllPassFilter filter) {
		this.filters.add(filter);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#createEvent(it.slumdroid.droidmodels.model.WidgetState, java.lang.String)
	 */
	public UserEvent createEvent (WidgetState target, String type) {
		TestCaseEvent newEvent = TestCaseEvent.createEvent(getTheSession());
		if (target != null) {
			newEvent.setWidget (target.clone());
		}
		newEvent.setType(type);
		newEvent.setId(getUniqueEventId());
		return newEvent;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#createInput(it.slumdroid.droidmodels.model.WidgetState, java.lang.String, java.lang.String)
	 */
	public UserInput createInput(WidgetState target, String value, String type) {
		TestCaseInput newInput = TestCaseInput.createInput(getTheSession());
		newInput.setWidget (target.clone());
		newInput.setValue(value);
		newInput.setType(type);
		newInput.setId(getUniqueInputId());
		return newInput;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#createTask(it.slumdroid.droidmodels.model.Task, it.slumdroid.droidmodels.model.Transition)
	 */
	public Task createTask(Task head, Transition tail) {
		TestCaseTask task;
		if (head != null) {
			task = ((TestCaseTask)head).clone();
		} else {
			task = new TestCaseTask (getTheSession());
		}
		task.addTransition(tail);
		return task;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#importTask(org.w3c.dom.Element)
	 */
	public Task importTask (Element fromXml) {
		TestCaseTask imported = new TestCaseTask (getTheSession());
		Element task = (Element)getTheSession().getDom().adoptNode(fromXml);
		imported.setElement(task);
		return imported;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#importState(org.w3c.dom.Element)
	 */
	public ActivityState importState (Element fromXml) {
		return getTheSession().importState(fromXml);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Abstractor#createTransition(it.slumdroid.droidmodels.model.ActivityState, java.util.Collection, it.slumdroid.droidmodels.model.UserEvent)
	 */
	public Transition createTransition (ActivityState start, Collection<UserInput> inputs, UserEvent event) {
		Transition transition = TestCaseTransition.createTransition(start.getElement().getOwnerDocument());
		try {
			setStartActivity(transition, StartActivity.createActivity(start));
			for (UserInput inPut: inputs) {
				transition.addInput(inPut);
			}
			transition.setEvent (event);
		}
		catch (DOMException e) {
			e.printStackTrace();
		}
		return transition;
	}

	/**
	 * Gets the unique event id.
	 *
	 * @return the unique event id
	 */
	public String getUniqueEventId () {
		int ret = this.eventId;
		this.eventId++;
		return "e" + ret;
	}

	/**
	 * Gets the unique activity id.
	 *
	 * @return the unique activity id
	 */
	public String getUniqueActivityId () {
		int ret = this.activityId;
		this.activityId++;
		return "a" + ret;
	}

	/**
	 * Gets the unique input id.
	 *
	 * @return the unique input id
	 */
	public String getUniqueInputId () {
		int ret = this.inputId;
		this.inputId++;
		return "i" + ret;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onSavingState()
	 */
	public SessionParams onSavingState() {
		SessionParams state = new SessionParams();
		state.store(EVENT_PARAM_NAME, this.eventId);
		state.store(INPUT_PARAM_NAME, this.inputId);
		state.store(ACTIVITY_PARAM_NAME, this.activityId);
		return state;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onLoadingState(it.slumdroid.tool.utilities.SessionParams)
	 */
	public void onLoadingState(SessionParams sessionParams) {
		this.eventId = sessionParams.getInt(EVENT_PARAM_NAME);
		this.inputId = sessionParams.getInt(INPUT_PARAM_NAME);
		this.activityId = sessionParams.getInt(ACTIVITY_PARAM_NAME);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#getListenerName()
	 */
	public String getListenerName() {
		return ACTOR_NAME;
	}

}