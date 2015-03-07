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

import it.slumdroid.tool.utilities.SessionParams;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving saveState events.
 * The class that is interested in processing a saveState
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSaveStateListener<code> method. When
 * the saveState event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SaveStateEvent
 */
public interface SaveStateListener {

	/**
	 * Gets the listener name.
	 *
	 * @return the listener name
	 */
	public String getListenerName();

	/**
	 * On saving state.
	 *
	 * @return the session params
	 */
	public SessionParams onSavingState();

	/**
	 * On loading state.
	 *
	 * @param sessionParams the session params
	 */
	public void onLoadingState(SessionParams sessionParams);

}
