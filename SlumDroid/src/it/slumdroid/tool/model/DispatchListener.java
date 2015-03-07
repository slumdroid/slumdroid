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

import it.slumdroid.droidmodels.model.Task;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving dispatch events.
 * The class that is interested in processing a dispatch
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDispatchListener<code> method. When
 * the dispatch event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DispatchEvent
 */
public interface DispatchListener {

	/**
	 * On task dispatched.
	 *
	 * @param theTask the task
	 */
	public void onTaskDispatched(Task theTask);

}
