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
 * The Interface ActivityState.
 */
public interface ActivityState extends WrapperInterface, Iterable<WidgetState> {

	/** The failure. */
	public static String FAILURE = "fail";
	
	/** The crash. */
	public static String CRASH = "crash";
	
	/** The exit. */
	public static String EXIT = "exit";

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName (String name);
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title);
	
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
	public void setId (String id);
	
	/**
	 * Gets the unique id.
	 *
	 * @return the unique id
	 */
	public String getUniqueId();
	
	/**
	 * Sets the unique id.
	 *
	 * @param id the new unique id
	 */
	public void setUniqueId (String id);
	
	/**
	 * Gets the screenshot.
	 *
	 * @return the screenshot
	 */
	public String getScreenshot();
	
	/**
	 * Sets the screenshot.
	 *
	 * @param screenshot the new screenshot
	 */
	public void setScreenshot (String screenshot);
	
	/**
	 * Checks for widget.
	 *
	 * @param widget the widget
	 * @return true, if successful
	 */
	public boolean hasWidget (WidgetState widget);
	
	/**
	 * Checks if is exit.
	 *
	 * @return true, if is exit
	 */
	public boolean isExit();
	
	/**
	 * Checks if is crash.
	 *
	 * @return true, if is crash
	 */
	public boolean isCrash();
	
	/**
	 * Checks if is failure.
	 *
	 * @return true, if is failure
	 */
	public boolean isFailure();
	
	/**
	 * Mark as exit.
	 */
	public void markAsExit();
	
}
