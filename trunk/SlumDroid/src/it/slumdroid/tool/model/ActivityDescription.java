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

import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * The Interface ActivityDescription.
 */
public interface ActivityDescription extends Iterable<View> {

	/**
	 * Gets the activity name.
	 *
	 * @return the activity name
	 */
	public String getActivityName ();

	/**
	 * Gets the activity title.
	 *
	 * @return the activity title
	 */
	public String getActivityTitle ();

	/**
	 * Gets the widget index.
	 *
	 * @param view the view
	 * @return the widget index
	 */
	public int getWidgetIndex (View view);

}
