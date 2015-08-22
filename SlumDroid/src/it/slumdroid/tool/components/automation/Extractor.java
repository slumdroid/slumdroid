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

package it.slumdroid.tool.components.automation;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;
import it.slumdroid.tool.model.ActivityDescription;

// TODO: Auto-generated Javadoc
/**
 * The Class TrivialExtractor.
 */
public class Extractor {

	/** The all views. */
	private ArrayList<View> allViews = new ArrayList<View>();

	/**
	 * Gets the all views.
	 *
	 * @return the all views
	 */
	public ArrayList<View> getAllViews() {
		return this.allViews;
	}

	/**
	 * Extract state.
	 */
	public void extractState() {
		this.allViews = new ArrayList<View>(Automation.getRobotium().getViews());
	}

	/**
	 * Describe activity.
	 *
	 * @return the activity description
	 */
	public ActivityDescription describeActivity() {
		return new ActivityDescription() {

			public Iterator<View> iterator() {
				return getAllViews().iterator();
			}

			public int getWidgetIndex(View view) {
				return getAllViews().indexOf(view);
			}

			public String getActivityName() {
				return Automation.getCurrentActivity().getClass().getSimpleName();
			}

			public String getActivityTitle() {
				return Automation.getCurrentActivity().getTitle().toString();
			}

			public String toString() {
				return getActivityName();
			}

		};
	}

}
