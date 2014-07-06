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

	@Override
	public void setElement (Element e) {
		super.setElement(e);
		this.description = (Element) getElement().getChildNodes().item(0);
	}

	public Iterator<WidgetState> iterator() {
		if (this.description.getNodeName().equals(DESC_TAG)) {
			return new NodeListWrapper<WidgetState> (this.description, new TestCaseWidget());
		}
		return null;
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name",name);
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
		setAttribute("id",id);		
	}

	public String getUniqueId() {
		return getAttribute("unique_id");
	}

	public void setUniqueId(String id) {
		setAttribute("unique_id",id);		
	}

	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	public void setScreenshot(String fileName) {
		setAttribute("screenshot",fileName);		
	}

	public static TestCaseActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		Element desc = dom.createElement(DESC_TAG);
		el.appendChild(desc);	
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
		newActivity.copyDescriptionFrom(originalActivity);		
		return newActivity;
	}

	public void copyDescriptionFrom (ActivityState originalActivity) {
		this.setDescriptionId(originalActivity.getDescriptionId());
		for (WidgetState w: originalActivity) {
			this.addWidget (w.clone());
		}
	}

	public void resetDescription () {
		for (WidgetState w: this) {
			this.description.removeChild(w.getElement());
		}
	}

	public void setDescriptionId (String id) {
		this.description.setAttribute("id", id);
	}

	public String getDescriptionId() {
		if (!this.description.hasAttribute("id")) return "";
		return this.description.getAttribute("id");
	}

	public TestCaseActivity clone () {
		return createActivity(this);
	}

	public void addWidget(WidgetState w) {
		this.description.appendChild(w.getElement());
	}

	public boolean hasWidget(WidgetState widgetToCheck) {
		for (WidgetState stored: this) {
			if (widgetToCheck.equals(stored))
				return true;
		}
		return false;
	}

	public static String getTag () {
		return "ACTIVITY";
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

	private Element description;	
	public final static String DESC_TAG = "DESCRIPTION";

}
