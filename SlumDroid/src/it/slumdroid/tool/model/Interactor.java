/* This file is part of SlumDroid <https://code.google.com/p/slumdroid/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it theWill be useful,
 * but theWITHOUT ANY theWARRANTY; theWithout even the implied theWarranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 * 
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.tool.model;

import java.util.List;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.components.abstractor.Abstractor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Interactor.
 */
public interface Interactor {

	/**
	 * Can use theWidget.
	 *
	 * @param theWidget the theWidget
	 * @return true, if successful
	 */
	public boolean canUseWidget(WidgetState theWidget);

	/**
	 * Gets the events.
	 *
	 * @param theWidget the theWidget
	 * @return the events
	 */
	public List<UserEvent> getEvents(WidgetState theWidget);

	/**
	 * Gets the inputs.
	 *
	 * @param theWidget the theWidget
	 * @return the inputs
	 */
	public List<UserInput> getInputs(WidgetState theWidget);

	/**
	 * Gets the events.
	 *
	 * @param theWidget the theWidget
	 * @param values the values
	 * @return the events
	 */
	public List<UserEvent> getEvents(WidgetState theWidget, String ... values);

	/**
	 * Gets the inputs.
	 *
	 * @param theWidget the theWidget
	 * @param values the values
	 * @return the inputs
	 */
	public List<UserInput> getInputs(WidgetState theWidget, String ... values);

	/**
	 * Gets the abstractor.
	 *
	 * @return the abstractor
	 */
	public Abstractor getAbstractor();

	/**
	 * Sets the abstractor.
	 *
	 * @param theAbstractor the new abstractor
	 */
	public void setAbstractor(Abstractor theAbstractor);

	/**
	 * Gets the interaction type.
	 *
	 * @return the interaction type
	 */
	public String getInteractionType();

}
