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

package it.slumdroid.tool.model;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface EventHandler.
 */
public interface EventHandler {

	/**
	 * Handle event.
	 *
	 * @param theWidget the widget
	 * @return the list
	 */
	public List<UserEvent> handleEvent(WidgetState theWidget);

}
