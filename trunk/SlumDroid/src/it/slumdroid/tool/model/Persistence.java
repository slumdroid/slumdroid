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

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import android.app.Activity;

// TODO: Auto-generated Javadoc
/**
 * The Interface Persistence.
 */
public interface Persistence {

	/**
	 * Save.
	 */
	public void save();

	/**
	 * Sets the session.
	 *
	 * @param theSession the new session
	 */
	public void setSession(Session theSession);

	/**
	 * Adds the task.
	 *
	 * @param theTask the task
	 */
	public void addTask(Task theTask);

	/**
	 * Sets the context.
	 *
	 * @param activity the new context
	 */
	public void setContext(Activity activity);

	/**
	 * Exists.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public boolean exists(String fileName);

}
