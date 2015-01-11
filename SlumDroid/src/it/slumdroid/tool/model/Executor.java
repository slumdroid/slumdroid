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
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import android.test.ActivityInstrumentationTestCase2;

// TODO: Auto-generated Javadoc
/**
 * The Interface Executor.
 */
public interface Executor {

	/**
	 * Bind.
	 *
	 * @param engine the engine
	 */
	public void bind (ActivityInstrumentationTestCase2<?> engine);

	/**
	 * Execute.
	 *
	 * @param theTask the task
	 */
	public void execute (Task theTask);

	/**
	 * Fire event.
	 *
	 * @param theEvent the event
	 */
	public void fireEvent (UserEvent theEvent);

	/**
	 * Sets the input.
	 *
	 * @param theInput the new input
	 */
	public void setInput (UserInput theInput);

	/**
	 * Wait.
	 *
	 * @param milli the milli
	 */
	public void wait (int milli);

}
