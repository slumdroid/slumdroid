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

	public TestCaseEvent (Element e) {
		super (e);
	}

	public TestCaseEvent (XmlGraph g) {
		super (g, TAG);
	}

	public TestCaseEvent (Document d) {
		super (d, TAG);
	}

	public WrapperInterface getWrapper(Element e) {
		return new TestCaseEvent (e);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String t) {
		setAttribute("type",t);
	}

	public boolean hasValue() {
		return hasAttribute("value");
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String v) {
		setAttribute("value",v);
	}

	public void setId (String id) {
		setAttribute("id",id);
	}

	public String getId() {
		return getAttribute("id");
	}

	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	public void setScreenshot(String fileName) {
		setAttribute("screenshot",fileName);		
	}

	public void setDescription (String d) {
		setAttribute("desc",d);
	}

	public boolean hasDescription () {
		return hasAttribute("desc");
	}

	public String getDescription () {
		return getAttribute("desc");
	}

	public static TestCaseEvent createEvent(GuiTree theSession) {
		TestCaseEvent event = new TestCaseEvent(theSession);
		return event;
	}

	public static TestCaseEvent createEvent(Document dom) {
		TestCaseEvent event = new TestCaseEvent(dom);
		return event;
	}

	public TestCaseEvent clone () {
		TestCaseEvent that = createEvent (getElement().getOwnerDocument());
		that.setType(this.getType());
		if (this.hasValue()) {
			that.setValue(this.getValue());
		}
		that.setId(this.getId());
		if (this.hasDescription()) {
			that.setDescription(this.getDescription());
		}
		try{
			if (this.getWidget() != null){
				that.setWidget(this.getWidget().clone());	
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return that;
	}

}
