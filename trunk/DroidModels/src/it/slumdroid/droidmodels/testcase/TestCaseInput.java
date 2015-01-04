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

public class TestCaseInput extends TestCaseInteraction implements UserInput {

	public static String TAG = "INPUT";

	public TestCaseInput () {
		super();
	}

	public TestCaseInput (Element element) {
		super (element);
	}

	public TestCaseInput (XmlGraph graph) {
		super (graph, TAG);
	}

	public TestCaseInput (Document dom) {
		super (dom, TAG);
	}

	public WrapperInterface getWrapper(Element element) {
		return new TestCaseInput (element);
	}

	public String getType() {
		return getAttribute("type");
	}

	public String getName() {
		return getWidget().getName();
	}


	public String getValue() {
		return getAttribute("value");
	}

	public boolean hasValue() {
		return hasAttribute("value");
	}

	public void setType (String type) {
		setAttribute("type", type);
	}

	public void setValue (String value) {
		setAttribute("value", value);
	}

	public void setId (String id) {
		setAttribute("id", id);
	}

	public String getId() {
		return getAttribute("id");
	}

	public static TestCaseInput createInput(GuiTree theSession) {
		return new TestCaseInput(theSession);
	}

	public static TestCaseInput createInput(Document theSession) {
		return new TestCaseInput(theSession);
	}

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