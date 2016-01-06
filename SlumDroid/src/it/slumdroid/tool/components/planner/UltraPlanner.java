/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.components.planner;

import static it.slumdroid.droidmodels.model.InteractionType.CHANGE_ORIENTATION;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_LONG_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_ACTION;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_BACK;
import static it.slumdroid.droidmodels.model.InteractionType.PRESS_MENU;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.ACTION_HOME;
import static it.slumdroid.droidmodels.model.SimpleType.DIALOG_TITLE;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.tool.Resources.EXTRA_INPUTS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.pm.ActivityInfo;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.testcase.TestCaseEvent;
import it.slumdroid.droidmodels.testcase.TestCaseInput;
import it.slumdroid.tool.components.abstractor.Abstractor;
import it.slumdroid.tool.components.automation.Automation;
import it.slumdroid.tool.model.EventHandler;
import it.slumdroid.tool.model.InputHandler;
import it.slumdroid.tool.utilities.AllPassFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class UltraPlanner.
 */
public class UltraPlanner {

	/** The filters. */
	protected AllPassFilter eventFilter, inputFilter;

	/** The user. */
	protected EventHandler user;

	/** The form filler. */
	protected InputHandler formFiller;

	/** The abstractor. */
	protected Abstractor abstractor;

	/** The include action. */
	protected boolean includeAction;

	/** The include menu. */
	protected boolean includeMenu;

	/** The include rotation. */
	protected boolean includeRotation;

	/**
	 * Adds the plan for activity widgets.
	 *
	 * @param thePlanner the planner
	 * @param theState the state
	 */
	private void addPlanForActivityWidgets(Plan thePlanner, ActivityState theState) {
		setIncludeAction(false);
		setIncludeMenu(true);
		int orientation = Automation.getCurrentActivity().getRequestedOrientation();
		if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE 
				|| orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)  {
			setIncludeRotation(false);
		} else {
			setIncludeRotation(true);
		}
		for (WidgetState widget: getEventFilter()) {
			reductionActions(widget, theState);
			Collection<UserEvent> events = getUser().handleEvent(widget);
			for (UserEvent event: events) {                           
				if (event == null) {
					continue;
				} else {
					if (event.getType().equals(LIST_SELECT) 
							|| event.getType().equals(LIST_LONG_SELECT)) {
						Collection<UserInput> inputs = new ArrayList<UserInput>();
						Transition transition = getAbstractor().createTransition(theState, inputs, event);                          
						thePlanner.addTransition(transition);
					} else {
						if (includeEvent(event)) {
							adjacentValues(thePlanner, theState, event);
						}
					}
				}
			}
		}    
	}

	/**
	 * Reduction actions.
	 *
	 * @param theWidget the widget
	 * @param theState the state
	 */
	private void reductionActions(WidgetState theWidget , ActivityState theState) {
		if (theWidget.getSimpleType().equals(ACTION_HOME) 
				&& theWidget.isClickable() 
				&& theWidget.isAvailable()) {
			setIncludeAction(true);
			return;
		}
		if (theWidget.getSimpleType().equals(DIALOG_TITLE)) {
			setIncludeMenu(false);
			return;
		}
		if (theWidget.getSimpleType().equals(PREFERENCE_LIST)) {                
			setIncludeMenu(false);
			setIncludeRotation(false);
			return;
		}
	}

	/**
	 * Adjacent values.
	 *
	 * @param thePlanner the planner
	 * @param theState the state
	 * @param theEvent the event
	 */
	private void adjacentValues(Plan thePlanner, ActivityState theState, UserEvent theEvent) {
		ArrayList<List<UserInput>> macroInputs = new ArrayList<List<UserInput>>();
		int numWidgets = 0;
		for(WidgetState formWidget: getInputFilter()) {
			List<UserInput> alternatives = getFormFiller().handleInput(formWidget);
			if(alternatives.size() != 0) {                                             
				macroInputs.add(alternatives);
				numWidgets++;
			}                                               
		}
		Collection<UserInput> inputs = new ArrayList<UserInput>();
		for(int widget = 0; widget < numWidgets; widget++) {
			UserInput input = macroInputs.get(widget).get(0);
			if (includeInput(input, theEvent)) {
				inputs.add(input);
			}
		}
		Transition transition = getAbstractor().createTransition(theState, inputs, theEvent);                          
		thePlanner.addTransition(transition);
		for(int widget = 0; widget < numWidgets; widget++) {                                                                  
			for(int inPut = 1; inPut < macroInputs.get(widget).size(); inPut++) {
				Collection<UserInput> combinations = new ArrayList<UserInput>();
				for(int component = 0; component < numWidgets; component++) {
					if(component == widget) {
						UserInput input = macroInputs.get(widget).get(inPut);
						if (includeInput(input, theEvent)) {
							combinations.add(input);
						}
						continue;
					}
					UserInput input = macroInputs.get(component).get(0);
					if (includeInput(input, theEvent)) {
						combinations.add(((TestCaseInput)input).clone());
					}
				}
				transition = getAbstractor().createTransition(theState, combinations,((TestCaseEvent)theEvent).clone());                                        
				thePlanner.addTransition(transition);                           
			}                                                               
		}
	}

	/**
	 * Include event.
	 *
	 * @param theEvent the event
	 * @return true, if successful
	 */
	private boolean includeEvent(UserEvent theEvent) {
		if (theEvent.getType().equals(WRITE_TEXT) && EXTRA_INPUTS != null) {
			for (String extra: EXTRA_INPUTS) {
				String[] inputs = extra.split("( )?,( )?");
				if (inputs[1].equals(theEvent.getWidget().getId())) {
					return false;					
				}
			}
		}
		return true;
	}

	/**
	 * Include input.
	 *
	 * @param theInput the input
	 * @param theEvent the event
	 * @return true, if successful
	 */
	private boolean includeInput(UserInput theInput, UserEvent theEvent) {
		return (theInput != null) 
				&& !((theInput.getWidget().getId().equals(theEvent.getWidget().getId())) 
						&& (theInput.getType().equals(theEvent.getType())));
	}  

	/**
	 * Gets the plan for activity.
	 *
	 * @param theState the state
	 * @return the plan for activity
	 */
	public Plan getPlanForActivity(ActivityState theState) {
		Plan planner = new Plan();
		addPlanForActivityWidgets(planner, theState);
		UserEvent event;
		Transition transition;
		if (isIncludeMenu()) {
			event = getAbstractor().createEvent(PRESS_MENU);
			transition = getAbstractor().createTransition(theState, event);
			planner.addTransition(transition);
		}
		if (isIncludeAction()) {
			event = getAbstractor().createEvent(PRESS_ACTION);
			transition = getAbstractor().createTransition(theState, event);
			planner.addTransition(transition);
		}
		if (isIncludeRotation()) {
			event = getAbstractor().createEvent(CHANGE_ORIENTATION);
			transition = getAbstractor().createTransition(theState, event);
			planner.addTransition(transition);
		}
		event = getAbstractor().createEvent(PRESS_BACK);
		transition = getAbstractor().createTransition(theState, event);
		planner.addTransition(transition);
		return planner;
	}

	/**
	 * Gets the event filter.
	 *
	 * @return the event filter
	 */
	public AllPassFilter getEventFilter() {
		return this.eventFilter;
	}

	/**
	 * Sets the event filter.
	 *
	 * @param eventFilter the new event filter
	 */
	public void setEventFilter(AllPassFilter eventFilter) {
		this.eventFilter = eventFilter;
	}

	/**
	 * Gets the input filter.
	 *
	 * @return the input filter
	 */
	public AllPassFilter getInputFilter() {
		return this.inputFilter;
	}

	/**
	 * Sets the input filter.
	 *
	 * @param inputFilter the new input filter
	 */
	public void setInputFilter(AllPassFilter inputFilter) {
		this.inputFilter = inputFilter;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public EventHandler getUser() {
		return this.user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(EventHandler user) {
		this.user = user;
	}

	/**
	 * Gets the form filler.
	 *
	 * @return the form filler
	 */
	public InputHandler getFormFiller() {
		return this.formFiller;
	}

	/**
	 * Sets the form filler.
	 *
	 * @param formFiller the new form filler
	 */
	public void setFormFiller(InputHandler formFiller) {
		this.formFiller = formFiller;
	}

	/**
	 * Gets the abstractor.
	 *
	 * @return the abstractor
	 */
	public Abstractor getAbstractor() {
		return this.abstractor;
	}

	/**
	 * Sets the abstractor.
	 *
	 * @param abstractor the new abstractor
	 */
	public void setAbstractor(Abstractor abstractor) {
		this.abstractor = abstractor;
	}

	/**
	 * Checks if is include action.
	 *
	 * @return true, if is include action
	 */
	public boolean isIncludeAction() {
		return includeAction;
	}

	/**
	 * Sets the include action.
	 *
	 * @param includeAction the new include action
	 */
	public void setIncludeAction(boolean includeAction) {
		this.includeAction = includeAction;
	}

	/**
	 * Checks if is include menu.
	 *
	 * @return true, if is include menu
	 */
	public boolean isIncludeMenu() {
		return includeMenu;
	}

	/**
	 * Sets the include menu.
	 *
	 * @param includeMenu the new include menu
	 */
	public void setIncludeMenu(boolean includeMenu) {
		this.includeMenu = includeMenu;
	}

	/**
	 * Checks if is include rotation.
	 *
	 * @return true, if is include rotation
	 */
	public boolean isIncludeRotation() {
		return includeRotation;
	}

	/**
	 * Sets the include rotation.
	 *
	 * @param includeRotation the new include rotation
	 */
	public void setIncludeRotation(boolean includeRotation) {
		this.includeRotation = includeRotation;
	}

}