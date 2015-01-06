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

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface Interactor.
 */
public interface Interactor {

	/**
	 * Can use widget.
	 *
	 * @param widget the widget
	 * @return true, if successful
	 */
	public boolean canUseWidget (WidgetState widget);

	/**
	 * Gets the events.
	 *
	 * @param widget the widget
	 * @return the events
	 */
	public List<UserEvent> getEvents (WidgetState widget);

	/**
	 * Gets the inputs.
	 *
	 * @param widget the widget
	 * @return the inputs
	 */
	public List<UserInput> getInputs (WidgetState widget);

	/**
	 * Gets the events.
	 *
	 * @param widget the widget
	 * @param values the values
	 * @return the events
	 */
	public List<UserEvent> getEvents (WidgetState widget, String ... values);

	/**
	 * Gets the inputs.
	 *
	 * @param widget the widget
	 * @param values the values
	 * @return the inputs
	 */
	public List<UserInput> getInputs (WidgetState widget, String ... values);

	/**
	 * Gets the abstractor.
	 *
	 * @return the abstractor
	 */
	public Abstractor getAbstractor ();

	/**
	 * Sets the abstractor.
	 *
	 * @param abstractor the new abstractor
	 */
	public void setAbstractor (Abstractor abstractor);

	/**
	 * Gets the interaction type.
	 *
	 * @return the interaction type
	 */
	public String getInteractionType ();

}
