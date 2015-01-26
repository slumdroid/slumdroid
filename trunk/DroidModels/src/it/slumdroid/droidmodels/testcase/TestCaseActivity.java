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

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseActivity.
 */
public class TestCaseActivity extends ElementWrapper implements ActivityState {

	/**
	 * Instantiates a new test case activity.
	 */
	public TestCaseActivity() {
		super();
	}

	/**
	 * Instantiates a new test case activity.
	 *
	 * @param element the element
	 */
	public TestCaseActivity (Element element) {
		super(element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public TestCaseActivity getWrapper(Element element) {
		return new TestCaseActivity(element);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<WidgetState> iterator() {
		return new NodeListWrapper<WidgetState> (this.element, new TestCaseWidget());
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#getName()
	 */
	public String getName() {
		return getAttribute("name");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#setName(java.lang.String)
	 */
	public void setName(String name) {
		setAttribute("name", name);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#getTitle()
	 */
	public String getTitle() {
		return getAttribute("title");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		setAttribute("title", title);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#getId()
	 */
	public String getId() {
		return getAttribute("id");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#setId(java.lang.String)
	 */
	public void setId(String id) {
		setAttribute("id", id);		
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#getUniqueId()
	 */
	public String getUniqueId() {
		return getAttribute("unique_id");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#setUniqueId(java.lang.String)
	 */
	public void setUniqueId(String id) {
		setAttribute("unique_id", id);		
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#getScreenshot()
	 */
	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#setScreenshot(java.lang.String)
	 */
	public void setScreenshot(String fileName) {
		setAttribute("screenshot", fileName);		
	}

	/**
	 * Creates the activity.
	 *
	 * @param dom the dom
	 * @param tag the tag
	 * @return the test case activity
	 */
	public static TestCaseActivity createActivity (Document dom, String tag) {
		return new TestCaseActivity (dom.createElement(tag));		
	}

	/**
	 * Creates the activity.
	 *
	 * @param dom the dom
	 * @return the test case activity
	 */
	public static TestCaseActivity createActivity (Document dom) {
		return createActivity (dom, getTag());
	}

	/**
	 * Creates the activity.
	 *
	 * @param session the session
	 * @return the test case activity
	 */
	public static TestCaseActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}

	// The main purpose of this method is to create the start activity  
	// of a transition from the final activity of the previous one
	/**
	 * Creates the activity.
	 *
	 * @param originalActivity the original activity
	 * @return the test case activity
	 */
	public static TestCaseActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		TestCaseActivity newActivity = createActivity (dom);		
		return newActivity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TestCaseActivity clone () {
		return createActivity(this);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#hasWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean hasWidget(WidgetState widgetToCheck) {
		for (WidgetState stored: this) {
			if (widgetToCheck.equals(stored))
				return true;
		}
		return false;
	}

	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public static String getTag () {
		return "STATE";
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#isExit()
	 */
	public boolean isExit() {
		return getId().equals(EXIT);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#isCrash()
	 */
	public boolean isCrash() {
		return getId().equals(CRASH);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#isFailure()
	 */
	public boolean isFailure() {
		return getId().equals(FAILURE);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#markAsExit()
	 */
	public void markAsExit() {
		setName(EXIT);
		setId(EXIT);		
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#markAsCrash()
	 */
	public void markAsCrash() {
		setId(CRASH);	
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.ActivityState#markAsFailure()
	 */
	public void markAsFailure() {
		setId(FAILURE);
	}

}
