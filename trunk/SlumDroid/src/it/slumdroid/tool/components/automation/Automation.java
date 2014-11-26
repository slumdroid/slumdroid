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

	public void execute (Task t) {
		this.executor.process (t);
	}

	public void process (Task t) {
		afterRestart();
		Log.i (TAG, "Playing Task " + t.getId());
		for (Transition step: t) {
			process (step);
		}
	}

	public void process (Transition step) {
		for (UserInput i: step) {
			setInput(i);
		}
		fireEvent (step.getEvent());		
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
			View v = null;
			if (event.getWidget().getIndex()<trivialExtractor.getAllWidgets().size()) {
				v = trivialExtractor.getAllWidgets().get(event.getWidget().getIndex()); // Search widget by index
			}
			if ((v!=null) && checkWidgetEquivalence(v, Integer.parseInt(event.getWidgetId()), event.getWidgetType(), event.getWidgetName())) { // Widget found
				if (eventType.equals(WRITE_TEXT) || eventType.equals(ENTER_TEXT)) {
					Log.i(TAG, "Firing event: type=" + eventType + " index=" + event.getWidget().getIndex() + " widget="+ event.getWidgetType() + " value=" + eventValue);
				}
				else Log.i(TAG, "Firing event: type=" + eventType + " index=" + event.getWidget().getIndex() + " widget="+ event.getWidgetType());
				fireEventOnView (v, eventType, eventValue);
			} else if (event.getWidgetId().equals("-1")) { // Widget not found. Search widget by name
				Log.i(TAG, "Firing event: type=" + eventType + " name=" + event.getWidgetName() + " widget="+ event.getWidgetType());
				fireEvent (event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			} else { // Widget not found. Search widget by id
				Log.i(TAG, "Firing event: type=" + eventType + " id=" + event.getWidgetId() + " widget="+ event.getWidgetType());
				fireEvent (Integer.parseInt(event.getWidgetId()), event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			}
		}
		this.currentEvent = null;
	}

	public void setInput(UserInput input) {
		if (input.getType().equals(CLICK)){
			Log.i(TAG, "Setting input: type=" + input.getType() + " id=" + input.getWidgetId() + " widget=" + input.getWidgetType());
		} 
		else Log.i(TAG, "Setting input: type=" + input.getType() + " id=" + input.getWidgetId() + " widget=" + input.getWidgetType() + " value=" + input.getValue());
		setInput (Integer.parseInt(input.getWidgetId()), input.getType(), input.getValue(), input.getWidgetName(), input.getWidgetType());
	}

	private void fireEvent (int widgetId, String widgetName, String widgetType, String eventType, String value) {
		View v = getWidget(widgetId, widgetType, widgetName);
		if (v == null) {
			v = getWidget(widgetId);
		}
		if (v == null) {
			v = ExtractorUtilities.findViewById(widgetId);
		}
		fireEventOnView(v, eventType, value);
	}

	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View v = null;
		if (widgetType.equals(BUTTON)) {
			v = trivialExtractor.solo.getButton(widgetName);
		} else if (widgetType.equals(MENU_ITEM)) {
			v = trivialExtractor.solo.getText(widgetName);
		}
		if (v == null) {
			for (View w: trivialExtractor.getAllWidgets()) {
				if (w instanceof Button) {
					Button candidate = (Button) w;
					if (candidate.getText().equals(widgetName)) {
						v = candidate;
					}
				}
				if (v!=null) break;
			}
		}
		fireEventOnView(v, eventType, value);
	}

	private void fireEventOnView (View view, String eventType, String value) {
		injectInteraction(view, eventType, value);
		if (SLEEP_AFTER_EVENT!=0) wait(SLEEP_AFTER_EVENT);
		waitOnThrobber();
		refreshCurrentActivity();
		extractState();
	}

	private void injectInteraction (View view, String interactionType, String value) {
		if (view!=null) {
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
		View v = getWidget(widgetId, widgetType, widgetName);
		if (v == null) {
			v = getWidget(widgetId);
		}
		if (v == null) {
			v = ExtractorUtilities.findViewById(widgetId);
		}
		if (v == null) {
			for (View w: trivialExtractor.getAllWidgets()) {
				if (w instanceof Button || w instanceof RadioGroup) {
					if (!AbstractorUtilities.getType(w).equals(widgetType)) continue;
					v = (AbstractorUtilities.detectName(w).equals(widgetName))?w:null;
				}
				if (v!=null) break;
			}
		}
		injectInteraction(v, inputType, value);
	}

	protected void requestView (View view) {
		DroidExecutor.requestView(view);
	}

	public void wait (int milli) {
		DroidExecutor.wait(milli);
	}

	public void setExecutor (Executor e) {
		this.executor = e;
	}

	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
	}

	public void setExtractor (Extractor e) {
		this.extractor = e;
	}

	public void afterRestart() {
		if (SLEEP_AFTER_RESTART != 0) wait(SLEEP_AFTER_RESTART);
		if (SLEEP_ON_THROBBER != 0) waitOnThrobber();
	}

	public void waitOnThrobber() {
		int sleepTime = SLEEP_ON_THROBBER;
		if (sleepTime == 0) return;
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
	public void onClickEventFired (View v) {
		AbstractorUtilities.describeCurrentEvent (this.currentEvent, v);
	}

	public void onLongClickEventFired (View view) {
		onClickEventFired (view);
	}

}
