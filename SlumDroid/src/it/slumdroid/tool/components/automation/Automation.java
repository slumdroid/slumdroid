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

package it.slumdroid.tool.components.automation;

import static it.slumdroid.droidmodels.model.InteractionType.CHANGE_ORIENTATION;
import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.DRAG;
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
import static it.slumdroid.tool.components.automation.DroidExecutor.getInstrumentation;
import static it.slumdroid.tool.components.automation.DroidExecutor.goBack;
import static it.slumdroid.tool.components.automation.DroidExecutor.longClick;
import static it.slumdroid.tool.components.automation.DroidExecutor.openMenu;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectListItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectRadioItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.selectSpinnerItem;
import static it.slumdroid.tool.components.automation.DroidExecutor.setProgressBar;
import static it.slumdroid.tool.components.automation.DroidExecutor.swapTab;
import static it.slumdroid.tool.components.automation.DroidExecutor.unLockScreen;
import static it.slumdroid.tool.components.automation.DroidExecutor.writeText;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.tool.components.abstractor.AbstractorUtilities;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Executor;
import it.slumdroid.tool.model.Extractor;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Automation implements Executor, Extractor {

	private TrivialExtractor extractor;

	public Automation () {
		setExtractor(new TrivialExtractor()); 
	}

	// Initializations
	public void bind (ActivityInstrumentationTestCase2<?> test) {
		getExtractor().solo = DroidExecutor.createRobotium (test);
		unLockScreen();
		afterRestart();
		refreshCurrentActivity();
	}

	public void execute (Task task) {
		afterRestart();
		Log.i (TAG, "Playing Task " + task.getId());
		for (Transition step: task) {
			process (step);
		}
	}

	private void process (Transition transition) {
		for (UserInput input: transition) {
			setInput(input);
		}
		fireEvent (transition.getEvent());		
	}

	public void fireEvent(UserEvent event) {
		String eventType = event.getType();
		if (eventType.equals(PRESS_BACK) 
				|| eventType.equals(PRESS_MENU) 
				|| eventType.equals(PRESS_ACTION)
				|| eventType.equals(CHANGE_ORIENTATION)) { // Special events
			Log.i(TAG, "Firing event: " + eventType);
			fireEventOnView(null, eventType, null);
		} else {
			View view = null;
			if (event.getWidget().getIndex() < getExtractor().getAllWidgets().size()) {
				view = getExtractor().getAllWidgets().get(event.getWidget().getIndex()); // Search widget by index
			}
			String eventValue = event.getValue();
			if ( (view != null) && 
					checkWidgetEquivalence(view, Integer.parseInt(event.getWidgetId()), event.getWidgetType(), event.getWidgetName())) { // Widget found
				writeLogInfo(event);
				fireEventOnView (view, eventType, eventValue);
			} else if (event.getWidgetId().equals("-1")) { // Search widget by name
				writeLogInfo(event);
				fireEvent (event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			} else { // Search widget by id
				writeLogInfo(event);
				fireEvent (Integer.parseInt(event.getWidgetId()), event.getWidgetName(), event.getWidget().getSimpleType(), eventType, eventValue);
			}
		}
	}

	private void writeLogInfo(UserEvent event) {
		String eventType = event.getType();
		String eventId = event.getWidgetId();
		String eventSimpleType = event.getWidget().getSimpleType();
		String toWrite = "Firing event: " + eventType + " widgetId=" + eventId + " widgetType=" + eventSimpleType;
		String extraInfo = new String(); 
		if (!event.getValue().equals("")) {
			extraInfo += " value=" + event.getValue();
		}
		Log.i(TAG, toWrite + extraInfo);
	}

	public void setInput(UserInput input) {
		int widgetId = Integer.parseInt(input.getWidgetId());
		String inputType = input.getType();
		String value = input.getValue();
		String widgetName = input.getWidgetName();
		String widgetType = input.getWidgetType();
		String inputSimpleType = input.getWidget().getSimpleType();
		String toWrite = "Setting input: " + inputType + " widgetId=" + widgetId + " widgetType=" + inputSimpleType;
		if (!input.getValue().equals("")) {
			toWrite += " value=" + input.getValue();
		}
		Log.i(TAG, toWrite);
		setInput(widgetId, inputType, value, widgetName, widgetType);
	}

	private void fireEvent (int widgetId, String widgetName, String widgetType, String eventType, String value) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) view = getWidget(widgetId);
		if (view == null) view = ExtractorUtilities.findViewById(widgetId);
		fireEventOnView(view, eventType, value);
	}

	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View view = null;
		if (widgetType.equals(BUTTON)) view = getExtractor().solo.getButton(widgetName);
		else if (widgetType.equals(MENU_ITEM)) view = getExtractor().solo.getText(widgetName);
		if (view == null) {
			for (View theView: getExtractor().getAllWidgets()) {
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
		if (SLEEP_ON_THROBBER != 0) waitOnThrobber();
		refreshCurrentActivity();
		extractState();
	}

	private void injectInteraction (View view, String interactionType, String value) {
		if (view != null) requestView(view);

		if (interactionType.equals(CLICK)) click (view);
		else if (interactionType.equals(LONG_CLICK)) longClick(view);
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
		else if (interactionType.equals(SWAP_TAB) && (value!=null)) swapTab (view, value);
	}

	private void refreshCurrentActivity() {
		ExtractorUtilities.setActivity(getExtractor().solo.getCurrentActivity());
	}

	private void setInput (int widgetId, String inputType, String value, String widgetName, String widgetType) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) view = getWidget(widgetId);
		if (view == null) view = ExtractorUtilities.findViewById(widgetId);
		if (view == null) {
			for (View theView: getExtractor().getAllWidgets()) {
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

	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
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
			ArrayList<ProgressBar> bars = getExtractor().solo.getCurrentViews(ProgressBar.class);
			for (ProgressBar bar: bars) {
				if (bar.isShown() &&  bar.isIndeterminate()) {
					int newId = bar.getId();
					if (newId != oldId) { // Only log if the throbber changed since the last time
						oldId = newId;
					}
					flag = true;
					wait(500);
					sleepTime-=500;
				}
			}
		} while (flag && (sleepTime > 0));
		getInstrumentation().waitForIdleSync();
	}

	public View getWidget (int id) {
		return this.getExtractor().getWidget(id);
	}

	public View getWidget (int theId, String theType, String theName) {
		for (View theView: getWidgetsById(theId)) {
			if (checkWidgetEquivalence(theView, theId, theType, theName)) {
				return theView;
			}
		}
		return null;
	}

	public boolean checkWidgetEquivalence (View view, int theId, String theType, String theName) {
		String widgetType = AbstractorUtilities.getType(view); 
		if (!(theType.equals(widgetType))) return false;
		String widgetName = AbstractorUtilities.detectName(view);
		return theName.equals(widgetName) && theId == view.getId();
	}

	public ArrayList<View> getWidgetsById (int id) {
		ArrayList<View> theList = new ArrayList<View>();
		for (View theView: getExtractor().getAllWidgets()) {
			if (theView.getId() == id) {
				theList.add(theView);
			}
		}
		return theList;
	}

	public ActivityDescription describeActivity() {
		return this.getExtractor().describeActivity();
	}

	public void extractState() {
		this.getExtractor().extractState();
	}

	public TrivialExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(TrivialExtractor extractor) {
		this.extractor = extractor;
	}

}
