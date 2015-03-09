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

package it.slumdroid.tool.model;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.Collection;

import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Interface Abstractor.
 */
public interface Abstractor {

	/**
	 * Creates the activity.
	 *
	 * @param theDescription the description
	 * @return the activity state
	 */
	public ActivityState createActivity(ActivityDescription theDescription);

	/**
	 * Sets the base activity.
	 *
	 * @param theDescription the new base activity
	 */
	public void setBaseActivity(ActivityDescription theDescription);

	/**
	 * Gets the base activity.
	 *
	 * @return the base activity
	 */
	public ActivityState getBaseActivity();

	/**
	 * Creates the event.
	 *
	 * @param type the type
	 * @return the user event
	 */
	public UserEvent createEvent(String type);
	
	/**
	 * Creates the event.
	 *
	 * @param target the target
	 * @param type the type
	 * @return the user event
	 */
	public UserEvent createEvent(WidgetState target, String type);

	/**
	 * Creates the input.
	 *
	 * @param target the target
	 * @param value the value
	 * @param type the type
	 * @return the user input
	 */
	public UserInput createInput(WidgetState target, String value, String type);

	/**
	 * Creates the task.
	 *
	 * @param theTask the task
	 * @param theTransition the transition
	 * @return the task
	 */
	public Task createTask(Task theTask, Transition theTransition);
	
	/**
	 * Creates the transition.
	 *
	 * @param theState the state
	 * @param event the event
	 * @return the transition
	 */
	public Transition createTransition(ActivityState theState, UserEvent event);

	/**
	 * Creates the transition.
	 *
	 * @param start the start
	 * @param inputs the inputs
	 * @param event the event
	 * @return the transition
	 */
	public Transition createTransition(ActivityState start, Collection<UserInput> inputs, UserEvent event);

	/**
	 * Import task.
	 *
	 * @param element the element
	 * @return the task
	 */
	public Task importTask(Element element);

	/**
	 * Import state.
	 *
	 * @param fromXml the from xml
	 * @return the activity state
	 */
	public ActivityState importState(Element fromXml);

	/**
	 * Sets the final activity.
	 *
	 * @param theTask the task
	 * @param theState the state
	 */
	public void setFinalActivity(Task theTask, ActivityState theState);

}
