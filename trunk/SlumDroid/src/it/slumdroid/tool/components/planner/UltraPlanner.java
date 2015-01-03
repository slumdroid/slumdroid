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
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.TOAST;
import static it.slumdroid.tool.Resources.EXTRA_INPUTS;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.testcase.TestCaseEvent;
import it.slumdroid.droidmodels.testcase.TestCaseInput;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.EventHandler;
import it.slumdroid.tool.model.Filter;
import it.slumdroid.tool.model.InputHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UltraPlanner {

	protected Filter eventFilter, inputFilter;
	protected EventHandler user;
	protected InputHandler formFiller;
	protected Abstractor abstractor;
	protected boolean includeAction;
	protected boolean includeMenu;
	protected boolean includeRotation;

	private void addPlanForActivityWidgets (Plan planner, ActivityState activityState) {
		setIncludeAction(false);
		setIncludeMenu(true);
		setIncludeRotation(true);
		for (WidgetState widget: getEventFilter()) {
			reductionActions(widget, activityState);
			Collection<UserEvent> events = getUser().handleEvent(widget);
			for (UserEvent event: events) {                           
				if (event == null) continue;
				else{
					if (event.getType().equals(LIST_SELECT) 
							|| event.getType().equals(LIST_LONG_SELECT)) {
						Collection<UserInput> inputs = new ArrayList<UserInput>();
						Transition transition = getAbstractor().createStep(activityState, inputs, event);                          
						planner.addTask(transition);
					} else {
						if (includeEvent(event)) {
							adjacentValues(planner, activityState, event);
						}
					}
				}
			}
		}    
	}

	private void reductionActions(WidgetState widget , ActivityState activityState) {
		if (!activityState.getId().equals("a0") // a0 is id of initial START_STATE 
				&& widget.getSimpleType().equals(TOAST)) {
			setIncludeMenu(false);
		}
		if (widget.getSimpleType().equals(DIALOG_TITLE)) {
			setIncludeMenu(false);
		}
		if (widget.getSimpleType().equals(PREFERENCE_LIST)) {
			setIncludeMenu(false);
			setIncludeRotation(false);
		}
		if (widget.getSimpleType().equals(EXPAND_MENU)){
			setIncludeRotation(false);
		}
		if (widget.getSimpleType().equals(ACTION_HOME) 
				&& widget.isClickable() 
				&& widget.isAvailable()){
			setIncludeAction(true);
		}	
	}

	private void adjacentValues(Plan planner, ActivityState activityState, UserEvent event){
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
			if (includeInput(input, event)) inputs.add(input);
		}
		Transition transition = getAbstractor().createStep(activityState, inputs, event);                          
		planner.addTask(transition);
		for(int widget = 0; widget < numWidgets; widget++) {                                                                  
			for(int inPut = 1; inPut <= macroInputs.get(widget).size() - 1; inPut++) {
				Collection<UserInput> combinations = new ArrayList<UserInput>();
				for(int component = 0; component < numWidgets; component++) {
					if(component == widget) {
						UserInput input = macroInputs.get(widget).get(inPut);
						if (includeInput(input, event)) combinations.add(input);
						continue;
					}
					UserInput input = macroInputs.get(component).get(0);
					if (includeInput(input, event)) combinations.add(((TestCaseInput) input).clone());
				}
				transition = getAbstractor().createStep(activityState, combinations,((TestCaseEvent) event).clone());                                        
				planner.addTask(transition);                           
			}                                                               
		}
	}

	private boolean includeEvent(UserEvent event) {
		if (event.getType().equals(WRITE_TEXT) && EXTRA_INPUTS != null) {
			for (String s: EXTRA_INPUTS) {
				String[] widgets = s.split("( )?,( )?");
				if (widgets[1].equals(event.getWidget().getId())) {
					return false;					
				}
			}
		}
		return true;
	}

	private boolean includeInput(UserInput input, UserEvent event) {
		return (input != null) 
				&& !((input.getWidget().getId().equals(event.getWidget().getId())) 
						&& (input.getType().equals(event.getType())));
	}  

	public Plan getPlanForActivity (ActivityState activityState) {
		Plan planner = new Plan();
		addPlanForActivityWidgets(planner, activityState);
		UserEvent event;
		Transition transition;
		if (isIncludeMenu()) {
			event = getAbstractor().createEvent(null, PRESS_MENU);
			transition = getAbstractor().createStep(activityState, new HashSet<UserInput>(), event);
			planner.addTask(transition);
		}
		if (isIncludeAction()) {
			event = getAbstractor().createEvent(null, PRESS_ACTION);
			transition = getAbstractor().createStep(activityState, new HashSet<UserInput>(), event);
			planner.addTask(transition);
		}
		if (isIncludeRotation()) {
			event = getAbstractor().createEvent(null, CHANGE_ORIENTATION);
			transition = getAbstractor().createStep(activityState, new HashSet<UserInput>(), event);
			planner.addTask(transition);
		}
		event = getAbstractor().createEvent(null, PRESS_BACK);
		transition = getAbstractor().createStep(activityState, new HashSet<UserInput>(), event);
		planner.addTask(transition);
		return planner;
	}

	public Filter getEventFilter() {
		return this.eventFilter;
	}

	public void setEventFilter(Filter eventFilter) {
		this.eventFilter = eventFilter;
	}

	public Filter getInputFilter() {
		return this.inputFilter;
	}

	public void setInputFilter(Filter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public EventHandler getUser() {
		return this.user;
	}

	public void setUser(EventHandler user) {
		this.user = user;
	}

	public InputHandler getFormFiller() {
		return this.formFiller;
	}

	public void setFormFiller(InputHandler formFiller) {
		this.formFiller = formFiller;
	}

	public Abstractor getAbstractor() {
		return this.abstractor;
	}

	public void setAbstractor(Abstractor abstractor) {
		this.abstractor = abstractor;
	}

	public boolean isIncludeAction() {
		return includeAction;
	}

	public void setIncludeAction(boolean includeAction) {
		this.includeAction = includeAction;
	}

	public boolean isIncludeMenu() {
		return includeMenu;
	}

	public void setIncludeMenu(boolean includeMenu) {
		this.includeMenu = includeMenu;
	}

	public boolean isIncludeRotation() {
		return includeRotation;
	}

	public void setIncludeRotation(boolean includeRotation) {
		this.includeRotation = includeRotation;
	}

}