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

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.XmlGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseInteraction.
 */
public abstract class TestCaseInteraction extends ElementWrapper {

	/**
	 * Instantiates a new test case interaction.
	 */
	public TestCaseInteraction() {
		super();
	}

	/**
	 * Instantiates a new test case interaction.
	 *
	 * @param element the element
	 */
	public TestCaseInteraction(Element element) {
		super(element);
	}

	/**
	 * Instantiates a new test case interaction.
	 *
	 * @param graph the graph
	 * @param tag the tag
	 */
	public TestCaseInteraction(XmlGraph graph, String tag) {
		super(graph, tag);
	}

	/**
	 * Instantiates a new test case interaction.
	 *
	 * @param dom the dom
	 * @param tag the tag
	 */
	public TestCaseInteraction(Document dom, String tag) {
		super(dom, tag);
	}

	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public WidgetState getWidget() {
		return new TestCaseWidget((Element)getElement().getChildNodes().item(0));
	}

	/**
	 * Sets the widget.
	 *
	 * @param newChild the new widget
	 */
	public void setWidget(WidgetState newChild) {
		Element oldChild = getWidget().getElement();
		if (oldChild != null) getElement().removeChild(oldChild);
		if (newChild != null) getElement().appendChild(newChild.getElement());
	}

	/**
	 * Gets the widget name.
	 *
	 * @return the widget name
	 */
	public String getWidgetName() {
		return getWidget().getName();
	}

	/**
	 * Sets the widget name.
	 *
	 * @param name the new widget name
	 */
	public void setWidgetName(String name) {
		getWidget().setName(name);
	}

	/**
	 * Gets the widget type.
	 *
	 * @return the widget type
	 */
	public String getWidgetType() {
		return getWidget().getType();
	}

	/**
	 * Sets the widget type.
	 *
	 * @param type the new widget type
	 */
	public void setWidgetType(String type) {
		getWidget().setType(type);
	}

	/**
	 * Gets the widget id.
	 *
	 * @return the widget id
	 */
	public String getWidgetId() {
		return getWidget().getId();
	}

	/**
	 * Sets the widget id.
	 *
	 * @param widgetId the new widget id
	 */
	public void setWidgetId(String widgetId) {
		getWidget().setId(widgetId);
	}

}
