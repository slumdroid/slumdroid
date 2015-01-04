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

package it.slumdroid.droidmodels.guitree;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class FinalActivity.
 */
public class FinalActivity extends TestCaseActivity {

	/**
	 * Instantiates a new final activity.
	 *
	 * @param item the item
	 */
	public FinalActivity(Element item) {
		super (item);
	}

	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public final static String getTag () {
		return "FINAL_STATE";
	}

	/**
	 * Creates the activity.
	 *
	 * @param session the session
	 * @return the final activity
	 */
	public static FinalActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}

	/**
	 * Creates the activity.
	 *
	 * @param dom the dom
	 * @return the final activity
	 */
	public static FinalActivity createActivity (Document dom) {
		return createActivity (dom, getTag());
	}

	/**
	 * Creates the activity.
	 *
	 * @param dom the dom
	 * @param tag the tag
	 * @return the final activity
	 */
	public static FinalActivity createActivity (Document dom, String tag) {
		return new FinalActivity (dom.createElement(tag));
	}

	/**
	 * Creates the activity.
	 *
	 * @param originalActivity the original activity
	 * @return the final activity
	 */
	public static FinalActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		FinalActivity newActivity = createActivity (dom);
		if (!originalActivity.getName().equals("")) {
			newActivity.setName(originalActivity.getName());
		}
		if (!originalActivity.getTitle().equals("")) {
			newActivity.setTitle(originalActivity.getTitle());
		}
		if (!originalActivity.getId().equals("")) { 
			newActivity.setId(originalActivity.getId());
		}
		if (!originalActivity.getUniqueId().equals("")) {
			newActivity.setUniqueId(originalActivity.getUniqueId());	
		}
		if(!originalActivity.getScreenshot().equals("")) {
			newActivity.setScreenshot(originalActivity.getScreenshot());
		}
		return newActivity;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.testcase.TestCaseActivity#clone()
	 */
	public FinalActivity clone () {
		return createActivity(this);
	}

}
