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
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WrapperInterface;
import it.slumdroid.droidmodels.xml.XmlGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseEvent.
 */
public class TestCaseEvent extends TestCaseInteraction implements UserEvent {

	/** The tag. */
	public static String TAG = "EVENT";

	/**
	 * Instantiates a new test case event.
	 */
	public TestCaseEvent() {
		super();
	}

	/**
	 * Instantiates a new test case event.
	 *
	 * @param element the element
	 */
	public TestCaseEvent(Element element) {
		super (element);
	}

	/**
	 * Instantiates a new test case event.
	 *
	 * @param graph the graph
	 */
	public TestCaseEvent(XmlGraph graph) {
		super (graph, TAG);
	}

	/**
	 * Instantiates a new test case event.
	 *
	 * @param dom the dom
	 */
	public TestCaseEvent(Document dom) {
		super (dom, TAG);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public WrapperInterface getWrapper(Element element) {
		return new TestCaseEvent (element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getType()
	 */
	public String getType() {
		return getAttribute("type");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setType(java.lang.String)
	 */
	public void setType(String type) {
		setAttribute("type", type);
	}

	/**
	 * Checks for value.
	 *
	 * @return true, if successful
	 */
	public boolean hasValue() {
		return hasAttribute("value");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getValue()
	 */
	public String getValue() {
		return getAttribute("value");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		setAttribute("value", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setId(java.lang.String)
	 */
	public void setId(String id) {
		setAttribute("id", id);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getId()
	 */
	public String getId() {
		return getAttribute("id");
	}

	/**
	 * Creates the event.
	 *
	 * @param theSession the the session
	 * @return the test case event
	 */
	public static TestCaseEvent createEvent(GuiTree theSession) {
		return new TestCaseEvent(theSession);
	}

	/**
	 * Creates the event.
	 *
	 * @param dom the dom
	 * @return the test case event
	 */
	public static TestCaseEvent createEvent(Document dom) {
		return new TestCaseEvent(dom);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TestCaseEvent clone() {
		TestCaseEvent that = createEvent(getElement().getOwnerDocument());
		that.setType(this.getType());
		if (this.hasValue()) {
			that.setValue(this.getValue());
		}
		that.setId(this.getId());
		try{
			that.setWidget(this.getWidget().clone());	
		} catch (Exception e){
			// e.printStackTrace();
		}
		return that;
	}

}
