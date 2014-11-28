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

package it.slumdroid.tool.components.automation;

import static it.slumdroid.droidmodels.model.InteractionType.CHANGE_ORIENTATION;
import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.DRAG;
import static it.slumdroid.droidmodels.model.InteractionType.ENTER_TEXT;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_LONG_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.LONG_CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_ACTION;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_BACK;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_MENU;
import static it.slumdroid.droidmodels.model.InteractionType.RADIO_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.SET_BAR;
import static it.slumdroid.droidmodels.model.InteractionType.SPINNER_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.SWAP_TAB;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.tool.Resources.SLEEP_AFTER_EVENT;
import static it.slumdroid.tool.Resources.SLEEP_AFTER_RESTART;
import static it.slumdroid.tool.Resources.SLEEP_ON_THROBBER;
import static it.slumdroid.tool.Resources.TAG;
import static it.slumdroid.tool.components.automation.DroidExecutor.actionBarHome;
import static it.slumdroid.tool.components.automation.DroidExecutor.changeOrientation;
import static it.slumdroid.tool.components.automation.DroidExecutor.click;
import static it.slumdroid.tool.components.automation.DroidExecutor.drag;
import static it.slumdroid.tool.components.automation.DroidExecutor.enterText;
import static it.slumdroid.tool.components.automation.DroidExecutor.goBack;
import static it.slumdroid.tool.components.automation.DroidExecutor.longClick;
import static it.slumdroid.tool.components.automation.DroidExecutor.openMenu;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectListItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectRadioItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectSpinnerItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.setProgressBar;
import static it.slumdroid.tool.components.automation.DroidExecutor.sync;
import static it.slumdroid.tool.components.automation.DroidExecutor.writeText;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.tool.model.AbstractorUtilities;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.EventFiredListener;
import it.slumdroid.tool.model.Executor;
import it.slumdroid.tool.model.Extractor;
import it.slumdroid.tool.model.ExtractorUtilities;
import it.slumdroid.tool.model.ImageCaptor;
import it.slumdroid.tool.model.TaskProcessor;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Automation implements Executor, Extractor, TaskProcessor, ImageCaptor, EventFiredListener {
 
	private Extractor extractor;
	private Executor executor;
	private UserEvent currentEvent;
	private ImageCaptor imageCaptor;
	private TrivialExtractor trivialExtractor;

	public Automation () {
		trivialExtractor = new TrivialExtractor(); 
		setExtractor (trivialExtractor);
		this.imageCaptor = trivialExtractor;
		setExecutor (this);
		DroidExecutor.addListener(this);
	}

	// Initializations
	public void bind (ActivityInstrumentationTestCase2<?> test) {
		trivialExtractor.solo = DroidExecutor.createRobotium (test);
		afterRestart();
		refreshCurrentActivity();
	}

	public void execute (Task task) {
		this.executor.process (task);
	}

	public void process (Task task) {
		afterRestart();
		Log.i (TAG, "Playing Task " + task.getId());
		for (Transition step: task) {
			process (step);
		}
	}

	public void process (Transition transition) {
		for (UserInput input: transition) {
			setInput(input);
		}
		fireEvent (transition.getEvent());		
	}

	public void fireEvent(UserEvent event) {
		this.currentEvent = event;
		String eventType = event.getType();
		String eventValue = event.getValue();
		if (eventType.equals(PRESS_BACK) 
				|| eventType.equals(PRESS_MENU) 
				|| eventType.equals(PRESS_ACTION)
				|| eventType.equals(CHANGE_ORIENTATION)) { // Special events
			Log.i(TAG, "Firing event: type=" + eventType);
			fireEventOnView(null, eventType, null);
		} else {
			View view = null;
			String extraInfo = new String();
			if (event.getWidget().getIndex()<trivialExtractor.getAllWidgets().size()) {
				view = trivialExtractor.getAllWidgets().get(event.getWidget().getIndex()); // Search widget by index
			} 
			if (checkWidgetEquivalence(view, Integer.parseInt(event.getWidgetId()), event.getWidgetType(), event.getWidgetName()) 
					&& (view != null)) { // Widget found
				extraInfo = " index=" + event.getWidget().getIndex();
				if (eventType.equals(WRITE_TEXT) || eventType.equals(ENTER_TEXT)) {
					writeLogInfo(event, extraInfo + " value=" + eventValue);
				}
				else writeLogInfo(event, extraInfo);
				fireEventOnView (view, eventType, eventValue);
			} else if (event.getWidgetId().equals("-1")) { // Widget not found. Search widget by name
				writeLogInfo(event, extraInfo);
				fireEvent (event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			} else { // Widget not found. Search widget by id
				writeLogInfo(event, new String());
				fireEvent (Integer.parseInt(event.getWidgetId()), event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			}
		}
		this.currentEvent = null;
	}
	
	private void writeLogInfo(UserEvent event, String extraInfo){
		String toWrite = "Firing event: type=" + event.getType() + " id=" + event.getWidgetId() + " widget=" + event.getWidget().getSimpleType();
		Log.i(TAG, toWrite + extraInfo);
	}

	public void setInput(UserInput input) {
		String extraInfo = new String();
		if (input.getType().equals(CLICK)) {
			writeLogInfo(input, extraInfo);
		}
		else {
			writeLogInfo(input, extraInfo + " value=" + input.getValue());
		}
		setInput (Integer.parseInt(input.getWidgetId()), input.getType(), input.getValue(), input.getWidgetName(), input.getWidgetType());
	}
	
	private void writeLogInfo(UserInput input, String extraInfo){
		String toWrite = "Setting input: type=" + input.getType() + " id=" + input.getWidgetId() + " widget=" + input.getWidget().getSimpleType();
		Log.i(TAG, toWrite + extraInfo);
	}

	private void fireEvent (int widgetId, String widgetName, String widgetType, String eventType, String value) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) {
			view = getWidget(widgetId);
		}
		if (view == null) {
			view = ExtractorUtilities.findViewById(widgetId);
		}
		fireEventOnView(view, eventType, value);
	}

	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View view = null;
		if (widgetType.equals(BUTTON)) {
			view = trivialExtractor.solo.getButton(widgetName);
		} else if (widgetType.equals(MENU_ITEM)) {
			view = trivialExtractor.solo.getText(widgetName);
		}
		if (view == null) {
			for (View theView: trivialExtractor.getAllWidgets()) {
				if (theView instanceof Button) {
					Button candidate = (Button) theView;
					if (candidate.getText().equals(widgetName)) {
						view = candidate;
					}
				}
				if (view != null) break;
			}
		}
		fireEventOnView(view, eventType, value);
	}

	private void fireEventOnView (View view, String eventType, String value) {
		injectInteraction(view, eventType, value);
		if (SLEEP_AFTER_EVENT != 0) wait(SLEEP_AFTER_EVENT);
		waitOnThrobber();
		refreshCurrentActivity();
		extractState();
	}

	private void injectInteraction (View view, String interactionType, String value) {
		if (view != null) {
			requestView(view);
		}
		if (interactionType.equals(CLICK)) click (view);
		else if (interactionType.equals(LONG_CLICK))longClick(view);
		else if (interactionType.endsWith("Item")){
			if (interactionType.equals(LIST_SELECT)) selectListItem((ListView)view, value);
			else if (interactionType.equals(LIST_LONG_SELECT)) selectListItem((ListView)view, value, true);
			else if (interactionType.equals(SPINNER_SELECT)) selectSpinnerItem((Spinner)view, value);
			else if (interactionType.equals(RADIO_SELECT)) selectRadioItem((RadioGroup)view, value);	
		} 
		else if (interactionType.endsWith("Text")){
			if (interactionType.equals(WRITE_TEXT)) writeText((EditText)view, value);
			else enterText((EditText)view, value);	
		}
		else if (interactionType.contains("press")){
			if (interactionType.equals(PRESS_BACK)) goBack();
			else if (interactionType.equals(PRESS_MENU)) openMenu();
			else if (interactionType.equals(PRESS_ACTION)) actionBarHome();
		}
		else if (interactionType.equals(CHANGE_ORIENTATION)) changeOrientation();
		else if (interactionType.equals(SET_BAR)) setProgressBar(view, value);
		else if (interactionType.equals(DRAG)) drag(view);	
		else if (interactionType.equals(SWAP_TAB) && (value!=null)) DroidExecutor.swapTab (view, value);
	}

	private void refreshCurrentActivity() {
		ExtractorUtilities.setActivity(trivialExtractor.solo.getCurrentActivity());
	}

	private void setInput (int widgetId, String inputType, String value, String widgetName, String widgetType) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) {
			view = getWidget(widgetId);
		}
		if (view == null) {
			view = ExtractorUtilities.findViewById(widgetId);
		}
		if (view == null) {
			for (View theView: trivialExtractor.getAllWidgets()) {
				if (theView instanceof Button || theView instanceof RadioGroup) {
					if (!AbstractorUtilities.getType(theView).equals(widgetType)) continue;
					view = (AbstractorUtilities.detectName(theView).equals(widgetName))?theView:null;
				}
				if (view != null) break;
			}
		}
		injectInteraction(view, inputType, value);
	}

	protected void requestView (View view) {
		DroidExecutor.requestView(view);
	}

	public void wait (int milli) {
		DroidExecutor.wait(milli);
	}

	public void setExecutor (Executor executor) {
		this.executor = executor;
	}

	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
	}

	public void setExtractor (Extractor extractor) {
		this.extractor = extractor;
	}

	public void afterRestart() {
		if (SLEEP_AFTER_RESTART != 0) wait(SLEEP_AFTER_RESTART);
		if (SLEEP_ON_THROBBER != 0) waitOnThrobber();
	}

	public void waitOnThrobber() {
		int sleepTime = SLEEP_ON_THROBBER;
		boolean flag;
		do {
			flag = false;
			int oldId = 0;
			ArrayList<ProgressBar> bars = trivialExtractor.solo.getCurrentViews(ProgressBar.class);
			for (ProgressBar b: bars) {
				if (b.isShown() &&  b.isIndeterminate()) {
					int newId = b.getId();
					if (newId != oldId) { // Only log if the throbber changed since the last time
						oldId = newId;
					}
					flag = true;
					wait(500);
					sleepTime-=500;
				}
			}
		} while (flag && (sleepTime>0));
		sync();
	}

	public String getAppName () {
		return trivialExtractor.solo.getCurrentActivity().getApplicationInfo().toString();
	}

	public View getWidget (int id) {
		return this.extractor.getWidget(id);
	}

	public View getWidget (int theId, String theType, String theName) {
		for (View testee: getWidgetsById(theId)) {
			if (checkWidgetEquivalence(testee, theId, theType, theName)) {
				return testee;
			}
		}
		return null;
	}

	public boolean checkWidgetEquivalence (View testee, int theId, String theType, String theName) {
		String testeeType = AbstractorUtilities.getType(testee); 
		if ( !(theType.equals(testeeType)) ) return false;
		String testeeName = AbstractorUtilities.detectName(testee);
		return theName.equals(testeeName) && theId == testee.getId();
	}

	public ArrayList<View> getWidgetsById (int id) {
		ArrayList<View> theList = new ArrayList<View>();
		for (View theView: trivialExtractor.getAllWidgets()) {
			if (theView.getId() == id) {
				theList.add(theView);
			}
		}
		return theList;
	}

	public ActivityDescription describeActivity() {
		return this.extractor.describeActivity();
	}

	public void extractState() {
		this.extractor.extractState();
	}

	public Bitmap captureImage() {
		return this.imageCaptor.captureImage();	
	}

	// This methods call the Abstractor Utility methods to describe the current event
	public void onClickEventFired (View view) {
		AbstractorUtilities.describeCurrentEvent (this.currentEvent, view);
	}

	public void onLongClickEventFired (View view) {
		onClickEventFired (view);
	}

}
