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

import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Extractor;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.SparseArray;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * The Class TrivialExtractor.
 */
public class TrivialExtractor implements Extractor {

	/** The views. */
	private SparseArray<View> theViews = new SparseArray<View> ();
	
	/**
	 * Gets the widgets.
	 *
	 * @return the widgets
	 */
	public SparseArray<View> getWidgets () {
		return this.theViews;
	}

	/** The all views. */
	private ArrayList<View> allViews = new ArrayList<View>();

	/**
	 * Gets the all widgets.
	 *
	 * @return the all widgets
	 */
	public ArrayList<View> getAllWidgets () {
		return this.allViews;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#extractState()
	 */
	public void extractState() {
		getWidgets().clear();
		getAllWidgets().clear();	
		ArrayList<View> viewList = Automation.getRobotium().getViews();
		if (viewList.size() != 0) {
			for (View view: viewList) {
				this.allViews.add(view);
				if (view.getId() > 0) {
					this.theViews.put(view.getId(), view); // Add only if the widget has a valid ID
				}
			}	
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#getWidget(int)
	 */
	public View getWidget (int key) {
		return getWidgets().get(key);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Extractor#describeActivity()
	 */
	public ActivityDescription describeActivity() {
		return new ActivityDescription() {

			public Iterator<View> iterator() {
				return getAllWidgets().iterator();
			}

			public int getWidgetIndex(View view) {
				return getAllWidgets().indexOf(view);
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
