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

package it.slumdroid.droidmodels.guitree;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FinalActivity extends TestCaseActivity {

	public FinalActivity(Element item) {
		super (item);
	}

	public final static String getTag () {
		return "FINAL_STATE";
	}

	public static FinalActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}

	public static FinalActivity createActivity (Document dom) {
		return (FinalActivity) createActivity (dom, getTag());
	}

	public static FinalActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		Element desc = dom.createElement(DESC_TAG);
		el.appendChild(desc);
		return new FinalActivity (el);
	}

	public static FinalActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		FinalActivity newActivity = createActivity (dom);
		newActivity.setName(originalActivity.getName());
		newActivity.setTitle(originalActivity.getTitle());
		newActivity.setId(originalActivity.getId());
		newActivity.copyDescriptionFrom(originalActivity);
		newActivity.setUniqueId(originalActivity.getUniqueId());
		newActivity.setScreenshot(originalActivity.getScreenshot());
		return newActivity;
	}

	public FinalActivity clone () {
		return createActivity(this);
	}

}
