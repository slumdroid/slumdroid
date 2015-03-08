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

package it.slumdroid.droidmodels.model;

// TODO: Auto-generated Javadoc
/**
 * The Interface Transition.
 */
public interface Transition extends Iterable<UserInput>, WrapperInterface {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId();

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id);

	/**
	 * Gets the start activity.
	 *
	 * @return the start activity
	 */
	public ActivityState getStartActivity();

	/**
	 * Sets the start activity.
	 *
	 * @param theState the new start activity
	 */
	public void setStartActivity(ActivityState theState);

	/**
	 * Adds the input.
	 *
	 * @param theInput the the input
	 */
	public void addInput(UserInput theInput);

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public UserEvent getEvent();

	/**
	 * Sets the event.
	 *
	 * @param theEvent the new event
	 */
	public void setEvent(UserEvent theEvent);

	/**
	 * Gets the final activity.
	 *
	 * @return the final activity
	 */
	public ActivityState getFinalActivity();

	/**
	 * Sets the final activity.
	 *
	 * @param theState the new final activity
	 */
	public void setFinalActivity(ActivityState theState);

}