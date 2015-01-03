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

package it.slumdroid.droidmodels.testcase;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCaseActivity extends ElementWrapper implements ActivityState {

	public TestCaseActivity() {
		super();
	}

	public TestCaseActivity (Element e) {
		super(e);
	}

	public TestCaseActivity getWrapper(Element e) {
		return new TestCaseActivity(e);
	}

	public Iterator<WidgetState> iterator() {
		return new NodeListWrapper<WidgetState> (this.element, new TestCaseWidget());
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getTitle() {
		return getAttribute("title");
	}

	public void setTitle(String title) {
		setAttribute("title", title);
	}

	public String getId() {
		return getAttribute("id");
	}

	public void setId(String id) {
		setAttribute("id", id);		
	}

	public String getUniqueId() {
		return getAttribute("unique_id");
	}

	public void setUniqueId(String id) {
		setAttribute("unique_id", id);		
	}

	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	public void setScreenshot(String fileName) {
		setAttribute("screenshot", fileName);		
	}

	public static TestCaseActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		return new TestCaseActivity (el);		
	}

	public static TestCaseActivity createActivity (Document dom) {
		return createActivity (dom, getTag());
	}

	public static TestCaseActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}

	// The main purpose of this method is to create the start activity of a transition from the final activity of the previous one
	public static TestCaseActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		TestCaseActivity newActivity = createActivity (dom);		
		return newActivity;
	}

	public TestCaseActivity clone () {
		return createActivity(this);
	}

	public boolean hasWidget(WidgetState widgetToCheck) {
		for (WidgetState stored: this) {
			if (widgetToCheck.equals(stored))
				return true;
		}
		return false;
	}

	public static String getTag () {
		return "STATE";
	}

	public boolean isExit() {
		return getId().equals(EXIT);
	}

	public boolean isCrash() {
		return getId().equals(CRASH);
	}

	public boolean isFailure() {
		return getId().equals(FAILURE);
	}

	public void markAsExit() {
		setId(EXIT);		
	}

	public void markAsCrash() {
		setId(CRASH);	
	}

	public void markAsFailure() {
		setId(FAILURE);
	}

}
