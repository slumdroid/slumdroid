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
 * Copyright (C) 2014 Gennaro Imparato
 */

package it.slumdroid.droidmodels.model;

public interface ActivityState extends WrapperInterface, Iterable<WidgetState> {

	public static String FAILURE = "fail";
	public static String CRASH = "crash";
	public static String EXIT = "exit";

	public String getName();
	public void setName (String name);
	public String getTitle();
	public void setTitle(String title);
	public String getId();
	public void setId (String id);
	public String getUniqueId();
	public void setUniqueId (String id);
	public String getDescriptionId();
	public void setDescriptionId (String id);
	public String getScreenshot();
	public void setScreenshot (String screenshot);
	public void addWidget (WidgetState widget);
	public boolean hasWidget (WidgetState widget);
	public boolean isExit();
	public boolean isCrash();
	public boolean isFailure();
	public void markAsExit();
	public void markAsCrash();
	public void markAsFailure();

}
