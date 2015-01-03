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
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.model.WrapperInterface;
import it.slumdroid.droidmodels.xml.ElementWrapper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCaseWidget extends ElementWrapper implements WidgetState {
	
	public final static String TAG = "WIDGET";

	public TestCaseWidget () {
		super();
	}

	public TestCaseWidget (Element e) {
		super(e);
	}

	public WrapperInterface getWrapper(Element e) {
		return new TestCaseWidget (e);
	}

	public String getId() {
		return getAttribute("id");
	}

	public String getName() {
		return getAttribute("name");
	}

	public boolean isAvailable() {
		if (!hasAttribute("available")) return true;
		return (getAttribute("available").equals("true"));
	}

	public boolean isClickable() {
		if (!hasAttribute("clickable")) return true;
		return (getAttribute("clickable").equals("true"));
	}

	public boolean isLongClickable() {
		if (!hasAttribute("long_clickable")) return true;
		return (getAttribute("long_clickable").equals("true"));
	}

	public int getCount() {
		if (!hasAttribute("count")) return 1;
		return Integer.parseInt(getAttribute("count"));
	}

	public String getValue() {
		return getAttribute("value");
	}

	public String getType() {
		return guessType(); 
	}

	public int getIndex() {
		if (!hasAttribute("index")) return 0;
		return Integer.parseInt(getAttribute("index"));
	}

	public void setIndex (int index) {
		setAttribute("index", String.valueOf(index));
	}

	public String getTextType() {
		return hasAttribute("text_type")?getAttribute("text_type"):"";
	}

	public String guessType() {
		String type = getAttribute("type");
		return (type.indexOf('@') == -1)?type:type.substring(0, type.indexOf('@'));
	}

	public void setId(String id) {
		setAttribute("id", id);

	}

	public void setName(String name) {
		setAttribute("name",name);
	}

	public void setValue (String value) {
		setAttribute("value", value);
	}

	public void setAvailable (String a) {
		setAttribute("available", a);
	}

	public void setClickable (String c) {
		setAttribute("clickable", c);
	}

	public void setLongClickable (String c) {
		setAttribute("long_clickable", c);
	}

	public void setType(String type) {
		setAttribute("type",type);
	}

	public void setSimpleType(String type) {
		setAttribute("simple_type",type);
	}

	public void setTextType(String inputType) {
		setAttribute("text_type",inputType);
	}

	public void setCount(int count) {
		setAttribute("count", String.valueOf(count));
	}

	public void setIdNameType (String id, String name, String type) {
		setId (id);
		if (!name.equals("")) setName (name);
		setType (type);
	}

	public static TestCaseWidget createWidget (Document dom) {
		Element el = dom.createElement(TAG);
		return new TestCaseWidget (el);		
	}

	public static TestCaseWidget createWidget (GuiTree session) {
		return createWidget (session.getDom());
	}

	public TestCaseWidget clone() {
		Element el = this.getElement().getOwnerDocument().createElement(TAG);
		TestCaseWidget newOne = new TestCaseWidget(el);
		newOne.setIdNameType(this.getId(), this.getName(), this.getType());
		newOne.setSimpleType(this.getSimpleType());
		if (!this.getTextType().equals("")) newOne.setTextType(this.getTextType());
		if (!this.getAvailable().equals("")) newOne.setAvailable(this.getAvailable());
		if (!this.getClickable().equals("")) newOne.setClickable(this.getClickable());
		if (!this.getLongClickable().equals("")) newOne.setLongClickable(this.getLongClickable());
		if (!this.getValue().equals("")) newOne.setValue(this.getValue());
		if (this.getCount() != 1) {
			newOne.setCount(this.getCount());
		}
		newOne.setIndex(this.getIndex());
		return newOne;
	}

	protected String getAvailable () {
		return getAttribute("available");
	}

	protected String getClickable() {
		return getAttribute("clickable");
	}

	protected String getLongClickable() {
		return getAttribute("long_clickable");
	}

	public String getSimpleType() {
		return getAttribute("simple_type");
	}

}
