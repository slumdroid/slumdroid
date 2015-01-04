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

public abstract class TestCaseInteraction extends ElementWrapper {

	public TestCaseInteraction () {
		super();
	}

	public TestCaseInteraction (Element element) {
		super (element);
	}

	public TestCaseInteraction (XmlGraph graph, String tag) {
		super (graph, tag);
	}

	public TestCaseInteraction (Document dom, String tag) {
		super (dom, tag);
	}

	public WidgetState getWidget() {
		return new TestCaseWidget ((Element)getElement().getChildNodes().item(0));
	}

	public void setWidget(WidgetState newChild) {
		Element oldChild = getWidget().getElement();
		if (oldChild != null) getElement().removeChild(oldChild);
		if (newChild != null) getElement().appendChild(newChild.getElement());
	}

	public String getWidgetName() {
		return getWidget().getName();
	}

	public void setWidgetName(String name) {
		getWidget().setName(name);
	}

	public String getWidgetType() {
		return getWidget().getType();
	}

	public void setWidgetType(String type) {
		getWidget().setType(type);
	}

	public String getWidgetId() {
		return getWidget().getId();
	}

	public void setWidgetId(String widgetId) {
		getWidget().setId(widgetId);
	}

}
