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

package it.slumdroid.tool.components;

import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TOAST;
import static it.slumdroid.tool.utilities.AbstractorUtilities.detectName;
import static it.slumdroid.tool.utilities.AbstractorUtilities.getType;
import static it.slumdroid.tool.utilities.AbstractorUtilities.setCount;
import static it.slumdroid.tool.utilities.AbstractorUtilities.setValue;
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
import it.slumdroid.tool.model.Filter;
import it.slumdroid.tool.model.FilterHandler;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.TypeDetector;
import it.slumdroid.tool.utilities.SessionParams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import android.view.View;
import android.widget.TextView;

public class GuiTreeAbstractor implements Abstractor, FilterHandler, SaveStateListener {

	private GuiTree theSession;
	private StartActivity baseActivity;
	private HashSet<Filter> filters;
	private TypeDetector detector;
	
	private int eventId = 0;
	private int inputId = 0;
	private int activityId = 0;
	
	public final static String ACTOR_NAME = "GuiTreeAbstractor";
	private static final String EVENT_PARAM_NAME = "eventId";
	private static final String INPUT_PARAM_NAME = "inputId";
	private static final String ACTIVITY_PARAM_NAME = "activityId";

	public GuiTreeAbstractor () throws ParserConfigurationException {
		this (new GuiTree());
	}

	public GuiTreeAbstractor(GuiTree s) {
		super();
		this.filters = new HashSet<Filter>();
		setTheSession(s);
		PersistenceFactory.registerForSavingState(this);
	}

	public GuiTree getTheSession() {
		return this.theSession;
	}

	public void setTheSession(GuiTree theSession) {
		this.theSession = theSession;
	}

	public TypeDetector getTypeDetector () {
		return this.detector;
	}

	public void setTypeDetector (TypeDetector type) {
		this.detector = type;
	}

	public ActivityState createActivity (ActivityDescription desc) {
		return createActivity (desc,false);
	}

	// If the boolean parameter is omitted, the overloading method will default to a Final Activity
	public ActivityState createActivity (ActivityDescription desc, boolean start) {
		ActivityState newActivity = (start)?StartActivity.createActivity(getTheSession()):FinalActivity.createActivity(getTheSession());
		newActivity.setName(desc.getActivityName());
		newActivity.setTitle(desc.getActivityTitle());
		newActivity.setUniqueId(getUniqueActivityId());
		newActivity.setId(getUniqueActivityId());
		for (Filter f: this.filters) {
			f.clear();
		}
		boolean hasDescription = updateDescription(newActivity, desc, false);
		if (!hasDescription) newActivity.markAsExit();
		return newActivity;
	}

	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc) {
		return updateDescription  (newActivity, desc, true);
	}

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
			if (type != 0){
				widget.setTextType(new WidgetType(type, name, widget.getValue()).convert());
			}	
		}
		widget.setAvailable((view.isEnabled())?"true":"false");
		widget.setClickable((view.isClickable())?"true":"false");
		widget.setLongClickable((view.isLongClickable())?"true":"false");
		return widget;
	}

	public boolean updateDescription (ActivityState newActivity, ActivityDescription desc, boolean detectDuplicates) {
		boolean hasDescription = false;
		for (View view: desc) {
			hasDescription = true;
			if (!view.isShown()) continue;
			TestCaseWidget widget = createWidget (view);
			widget.setIndex(desc.getWidgetIndex(view));
			if (widget.getIndex() == 0 && widget.getSimpleType().equals(TEXT_VIEW)) {
				widget.setSimpleType(TOAST);
			}
			if (detectDuplicates && newActivity.hasWidget(widget)) continue;
			((ElementWrapper) newActivity).appendChild(widget.getElement());
			for (Filter f: this.filters) {
				f.loadItem(widget);
			}
		}
		return hasDescription;
	}

	public void setBaseActivity (ActivityDescription desc) {
		this.baseActivity = (StartActivity) createActivity(desc,true);
	}

	public ActivityState getBaseActivity () {
		return this.baseActivity;
	}

	public void setStartActivity (Transition theStep, ActivityState theActivity) {
		theStep.setStartActivity (stubActivity(theActivity));
	}

	public void setFinalActivity (Task theTask, ActivityState theActivity) {
		theTask.setFinalActivity (stubActivity(theActivity));
	}

	private TestCaseActivity stubActivity (ActivityState theActivity) {
		TestCaseActivity theStub = ((TestCaseActivity)theActivity).clone();
		return theStub;
	}

	public Iterator<Filter> iterator() {
		return this.filters.iterator();
	}

	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}

	public UserEvent createEvent (String type) {
		return createEvent (null, type);
	}

	public UserEvent createEvent (WidgetState target, String type) {
		TestCaseEvent newEvent = TestCaseEvent.createEvent(getTheSession());
		if (target != null) newEvent.setWidget (target.clone());
		newEvent.setType(type);
		newEvent.setId(getUniqueEventId());
		return newEvent;
	}

	public UserInput createInput(WidgetState target, String value, String type) {
		TestCaseInput newInput = TestCaseInput.createInput(getTheSession());
		newInput.setWidget (target.clone());
		newInput.setValue(value);
		newInput.setType(type);
		newInput.setId(getUniqueInputId());
		return newInput;
	}

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

	public Task importTask (Element fromXml) {
		TestCaseTask imported = new TestCaseTask (getTheSession());
		Element task = (Element)getTheSession().getDom().adoptNode(fromXml);
		imported.setElement(task);
		return imported;
	}

	public ActivityState importState (Element fromXml) {
		return getTheSession().importState(fromXml);
	}

	public Transition createStep (ActivityState start, Collection<UserInput> inputs, UserEvent event) {
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

	public String getUniqueEventId () {
		int ret = this.eventId;
		this.eventId++;
		return "e" + ret;
	}

	public String getUniqueActivityId () {
		int ret = this.activityId;
		this.activityId++;
		return "a" + ret;
	}

	public String getUniqueInputId () {
		int ret = this.inputId;
		this.inputId++;
		return "i" + ret;
	}

	public SessionParams onSavingState() {
		SessionParams state = new SessionParams();
		state.store(EVENT_PARAM_NAME, this.eventId);
		state.store(INPUT_PARAM_NAME, this.inputId);
		state.store(ACTIVITY_PARAM_NAME, this.activityId);
		return state;
	}

	public void onLoadingState(SessionParams sessionParams) {
		this.eventId = sessionParams.getInt(EVENT_PARAM_NAME);
		this.inputId = sessionParams.getInt(INPUT_PARAM_NAME);
		this.activityId = sessionParams.getInt(ACTIVITY_PARAM_NAME);
	}

	public String getListenerName() {
		return ACTOR_NAME;
	}

}