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

public class TestCaseEvent extends TestCaseInteraction implements UserEvent {

	public static String TAG = "EVENT";
	
	public TestCaseEvent () {
		super();
	}

	public TestCaseEvent (Element element) {
		super (element);
	}

	public TestCaseEvent (XmlGraph graph) {
		super (graph, TAG);
	}

	public TestCaseEvent (Document dom) {
		super (dom, TAG);
	}

	public WrapperInterface getWrapper(Element element) {
		return new TestCaseEvent (element);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String type) {
		setAttribute("type", type);
	}

	public boolean hasValue() {
		return hasAttribute("value");
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String value) {
		setAttribute("value", value);
	}

	public void setId (String id) {
		setAttribute("id", id);
	}

	public String getId() {
		return getAttribute("id");
	}

	public static TestCaseEvent createEvent(GuiTree theSession) {
		return new TestCaseEvent(theSession);
	}

	public static TestCaseEvent createEvent(Document dom) {
		return new TestCaseEvent(dom);
	}

	public TestCaseEvent clone () {
		TestCaseEvent that = createEvent (getElement().getOwnerDocument());
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
