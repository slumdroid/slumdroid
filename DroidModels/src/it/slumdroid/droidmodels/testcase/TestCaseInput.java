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
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WrapperInterface;
import it.slumdroid.droidmodels.xml.XmlGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static it.slumdroid.droidmodels.model.InteractionType.CLICK;

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseInput.
 */
public class TestCaseInput extends TestCaseInteraction implements UserInput {

	/** The tag. */
	public static String TAG = "INPUT";

	/**
	 * Instantiates a new test case input.
	 */
	public TestCaseInput () {
		super();
	}

	/**
	 * Instantiates a new test case input.
	 *
	 * @param element the element
	 */
	public TestCaseInput (Element element) {
		super (element);
	}

	/**
	 * Instantiates a new test case input.
	 *
	 * @param graph the graph
	 */
	public TestCaseInput (XmlGraph graph) {
		super (graph, TAG);
	}

	/**
	 * Instantiates a new test case input.
	 *
	 * @param dom the dom
	 */
	public TestCaseInput (Document dom) {
		super (dom, TAG);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public WrapperInterface getWrapper(Element element) {
		return new TestCaseInput (element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getType()
	 */
	public String getType() {
		return getAttribute("type");
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return getWidget().getName();
	}


	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getValue()
	 */
	public String getValue() {
		return getAttribute("value");
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
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setType(java.lang.String)
	 */
	public void setType (String type) {
		setAttribute("type", type);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setValue(java.lang.String)
	 */
	public void setValue (String value) {
		setAttribute("value", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#setId(java.lang.String)
	 */
	public void setId (String id) {
		setAttribute("id", id);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.UserInteraction#getId()
	 */
	public String getId() {
		return getAttribute("id");
	}

	/**
	 * Creates the input.
	 *
	 * @param theSession the the session
	 * @return the test case input
	 */
	public static TestCaseInput createInput(GuiTree theSession) {
		return new TestCaseInput(theSession);
	}

	/**
	 * Creates the input.
	 *
	 * @param theSession the the session
	 * @return the test case input
	 */
	public static TestCaseInput createInput(Document theSession) {
		return new TestCaseInput(theSession);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TestCaseInput clone () {
		TestCaseInput i = createInput(this.getElement().getOwnerDocument());
		i.setType(this.getType());
		if (!this.getType().equals(CLICK)){
			if (this.hasValue()) {
				i.setValue(this.getValue());
			}
		}
		i.setId(this.getId());
		i.setWidget(this.getWidget().clone());
		return i;
	}

}