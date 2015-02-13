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
 * The Interface UserInput.
 */
public interface UserInput extends UserInteraction {

	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public WidgetState getWidget();

	/**
	 * Sets the widget.
	 *
	 * @param widget the new widget
	 */
	public void setWidget(WidgetState widget);

	/**
	 * Gets the widget name.
	 *
	 * @return the widget name
	 */
	public String getWidgetName();

	/**
	 * Sets the widget name.
	 *
	 * @param name the new widget name
	 */
	public void setWidgetName(String name);

	/**
	 * Gets the widget type.
	 *
	 * @return the widget type
	 */
	public String getWidgetType();

	/**
	 * Sets the widget type.
	 *
	 * @param type the new widget type
	 */
	public void setWidgetType(String type);

}
