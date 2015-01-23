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
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.tool.components.abstractor.AbstractorUtilities;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Executor;
import it.slumdroid.tool.model.Extractor;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.robotium.solo.Solo;

// TODO: Auto-generated Javadoc
/**
 * The Class Automation.
 */
public class Automation implements Executor, Extractor {

	/** The extractor. */
	private TrivialExtractor extractor;

	/** The executor. */
	private static DroidExecutor executor;

	/**
	 * Instantiates a new automation.
	 */
	public Automation () {
		this.extractor = new TrivialExtractor(); 
	}

	// Initializations
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Executor#bind(android.test.ActivityInstrumentationTestCase2)
	 */
	public void bind (ActivityInstrumentationTestCase2<?> test) {
		executor = new DroidExecutor(test);
		getRobotium().unlockScreen();
		afterRestart();
		getRobotium().getCurrentActivity();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Executor#execute(it.slumdroid.droidmodels.model.Task)
	 */
	public void execute (Task task) {
		afterRestart();
		Log.i (TAG, "Playing Task " + task.getId());
		for (Transition step: task) {
			process (step);
		}
	}

	/**
	 * Process.
	 *
	 * @param transition the transition
	 */
	private void process (Transition transition) {
		for (UserInput input: transition) {
			setInput(input);
		}
		UserEvent event = transition.getEvent();
		String eventType = event.getType();
		if (eventType.contains("press") 
				|| eventType.equals(CHANGE_ORIENTATION)) { // Special Interactions
			Log.i(TAG, "Firing event: " + eventType);
			if (eventType.contains("press")) {
				if (eventType.equals(PRESS_BACK)) {
					getRobotium().goBack();
					return;
				}
				if (eventType.equals(PRESS_MENU)) {
					getRobotium().sendKey(Solo.MENU);
					return;
				}
				if (eventType.equals(PRESS_ACTION)) {
					getRobotium().clickOnActionBarHomeButton();
					return;
				}	
			} 
			if (eventType.equals(CHANGE_ORIENTATION)) {
				getExecutor().changeOrientation();
				return;
			}
		} else {
			fireEvent (event);	
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Executor#fireEvent(it.slumdroid.droidmodels.model.UserEvent)
	 */
	public void fireEvent(UserEvent event) {
		View view = null;
		if (event.getWidget().getIndex() < getExtractor().getAllWidgets().size()) {
			view = getExtractor().getAllWidgets().get(event.getWidget().getIndex()); // Search widget by index
		}
		if ( (view != null) && 
				checkWidgetEquivalence(view, Integer.parseInt(event.getWidgetId()), event.getWidgetType(), event.getWidgetName())) { // Widget found
			writeLogInfo(event);
			fireEventOnView (view, event.getType(), event.getValue());
		} else {
			if (event.getWidgetId().equals("-1")) { // Search widget by name
				writeLogInfo(event);
				fireEvent (event.getWidgetName(), event.getWidget().getSimpleType(), event.getType(), event.getValue());
			} else { // Search widget by id
				writeLogInfo(event);
				fireEvent (Integer.parseInt(event.getWidgetId()), event.getWidgetName(), event.getWidget().getSimpleType(), event.getType(), event.getValue());
			}	
		}
	}

	/**
	 * Write log info.
	 *
	 * @param event the event
	 */
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

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Executor#setInput(it.slumdroid.droidmodels.model.UserInput)
	 */
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

	/**
	 * Fire event.
	 *
	 * @param widgetId the widget id
	 * @param widgetName the widget name
	 * @param widgetType the widget type
	 * @param eventType the event type
	 * @param value the value
	 */
	private void fireEvent (int widgetId, String widgetName, String widgetType, String eventType, String value) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) {
			view = getWidget(widgetId);
		}
		if (view == null) {
			view = getRobotium().getCurrentActivity().findViewById(widgetId);
		}
		fireEventOnView(view, eventType, value);
	}

	/**
	 * Fire event.
	 *
	 * @param widgetName the widget name
	 * @param widgetType the widget type
	 * @param eventType the event type
	 * @param value the value
	 */
	private void fireEvent (String widgetName, String widgetType, String eventType, String value) {
		View view = null;
		if (widgetType.equals(BUTTON)) {
			getRobotium().clickOnButton(widgetName);
			return;
		}
		if (widgetType.equals(MENU_ITEM)) {
			view = getRobotium().getText(widgetName);
		}
		if (view == null) {
			for (View theView: getExtractor().getAllWidgets()) {
				if (theView instanceof Button) {
					Button candidate = (Button) theView;
					if (candidate.getText().equals(widgetName)) {
						view = candidate;
					}
				}
				if (view != null) {
					break;
				}
			}
		}
		fireEventOnView(view, eventType, value);
	}

	/**
	 * Fire event on view.
	 *
	 * @param view the view
	 * @param eventType the event type
	 * @param value the value
	 */
	private void fireEventOnView (View view, String eventType, String value) {
		injectInteraction(view, eventType, value);
		if (SLEEP_AFTER_EVENT != 0) {
			wait(SLEEP_AFTER_EVENT);
		}
		if (SLEEP_ON_THROBBER != 0) {
			waitOnThrobber();
		}
		getRobotium().getCurrentActivity();
		extractState();
	}

	/**
	 * Inject interaction.
	 *
	 * @param view the view
	 * @param interactionType the interaction type
	 * @param value the value
	 */
	private void injectInteraction (View view, String interactionType, String value) {
		requestView(view);
		if (interactionType.equals(CLICK)) {
			getExecutor().click (view);
			return;
		}
		if (interactionType.equals(LONG_CLICK)) {
			getExecutor().longClick(view);
			return;
		}	
		if (interactionType.endsWith("Item")) {
			if (interactionType.equals(LIST_SELECT)) {
				getExecutor().selectListItem((ListView)view, value);
				return;
			}
			if (interactionType.equals(LIST_LONG_SELECT)) {
				getExecutor().selectLongListItem((ListView)view, value);
				return;
			}
			if (interactionType.equals(SPINNER_SELECT)) {
				getExecutor().selectSpinnerItem((Spinner)view, value);
				return;
			}
			if (interactionType.equals(RADIO_SELECT)) {
				getExecutor().selectRadioItem((RadioGroup)view, value);
				return;
			}
		} 
		if (interactionType.endsWith("Text")) {
			if (interactionType.equals(WRITE_TEXT)) {
				getExecutor().writeText((EditText)view, value);
				return;
			}
			if (interactionType.equals(ENTER_TEXT)) {
				getExecutor().enterText((EditText)view, value);
				return;
			}
		}
		if (interactionType.equals(SET_BAR)) {
			getExecutor().setProgressBar(view, value);
			return;
		}
		if (interactionType.equals(SWAP_TAB) 
				&& (value != null)) {
			getExecutor().swapTab (view, value);
			return;
		}
	}

	/**
	 * Sets the input.
	 *
	 * @param widgetId the widget id
	 * @param inputType the input type
	 * @param value the value
	 * @param widgetName the widget name
	 * @param widgetType the widget type
	 */
	private void setInput (int widgetId, String inputType, String value, String widgetName, String widgetType) {
		View view = getWidget(widgetId, widgetType, widgetName);
		if (view == null) {
			view = getWidget(widgetId);
		}
		if (view == null) {
			view = getRobotium().getCurrentActivity().findViewById(widgetId);
		}
		if (view == null) {
			for (View theView: getExtractor().getAllWidgets()) {
				if (theView instanceof Button || theView instanceof RadioGroup) {
					if (!AbstractorUtilities.getType(theView).equals(widgetType)) {
						continue;
					}
					view = (AbstractorUtilities.detectName(theView).equals(widgetName))?theView:null;
				}
				if (view != null) {
					break;
				}
			}
		}
		injectInteraction(view, inputType, value);
	}

	/**
	 * Request view.
	 *
	 * @param view the view
	 */
	protected void requestView (View view) {
		getExecutor().requestView(view);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Executor#wait(int)
	 */
	public void wait (int milli) {
		getExecutor().wait(milli);
	}

	/**
	 * After restart.
	 */
	public void afterRestart() {
		if (SLEEP_AFTER_RESTART != 0) {
			wait(SLEEP_AFTER_RESTART);
		}
		if (SLEEP_ON_THROBBER != 0) {
			waitOnThrobber();
		}
	}

	/**
	 * Wait on throbber.
	 */
	public void waitOnThrobber() {
		int sleepTime = SLEEP_ON_THROBBER;
		boolean flag;
		do {
			flag = false;
			ArrayList<ProgressBar> bars = getRobotium().getCurrentViews(ProgressBar.class);
			for (ProgressBar bar: bars) {
				if (bar.isShown() &&  bar.isIndeterminate()) {
					flag = true;
					wait(50);
					sleepTime-=50;
				}
			}
		} while (flag && (sleepTime > 0));
		getExecutor().sync();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#getWidget(int)
	 */
	public View getWidget (int id) {
		return this.getExtractor().getWidget(id);
	}

	/**
	 * Gets the widget.
	 *
	 * @param theId the the id
	 * @param theType the the type
	 * @param theName the the name
	 * @return the widget
	 */
	public View getWidget (int theId, String theType, String theName) {
		for (View theView: getWidgetsById(theId)) {
			if (checkWidgetEquivalence(theView, theId, theType, theName)) {
				return theView;
			}
		}
		return null;
	}

	/**
	 * Check widget equivalence.
	 *
	 * @param view the view
	 * @param theId the the id
	 * @param theType the the type
	 * @param theName the the name
	 * @return true, if successful
	 */
	public boolean checkWidgetEquivalence (View view, int theId, String theType, String theName) {
		String widgetType = AbstractorUtilities.getType(view); 
		if (!theType.equals(widgetType)) {
			return false;
		}
		String widgetName = AbstractorUtilities.detectName(view);
		return theName.equals(widgetName) && theId == view.getId();
	}

	/**
	 * Gets the widgets by id.
	 *
	 * @param id the id
	 * @return the widgets by id
	 */
	public ArrayList<View> getWidgetsById (int id) {
		ArrayList<View> theList = new ArrayList<View>();
		for (View theView: getExtractor().getAllWidgets()) {
			if (theView.getId() == id) {
				theList.add(theView);
			}
		}
		return theList;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#describeActivity()
	 */
	public ActivityDescription describeActivity() {
		return this.getExtractor().describeActivity();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#extractState()
	 */
	public void extractState() {
		this.getExtractor().extractState();
	}

	/**
	 * Gets the extractor.
	 *
	 * @return the extractor
	 */
	public TrivialExtractor getExtractor() {
		return extractor;
	}

	/**
	 * Gets the executor.
	 *
	 * @return the executor
	 */
	public static DroidExecutor getExecutor() {
		return executor;
	}

	/**
	 * Gets the robotium.
	 *
	 * @return the robotium
	 */
	public static Solo getRobotium() {
		return getExecutor().getRobotium();
	}

}
