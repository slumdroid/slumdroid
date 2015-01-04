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

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseWidget.
 */
public class TestCaseWidget extends ElementWrapper implements WidgetState {

	/** The Constant TAG. */
	public final static String TAG = "WIDGET";

	/**
	 * Instantiates a new test case widget.
	 */
	public TestCaseWidget () {
		super();
	}

	/**
	 * Instantiates a new test case widget.
	 *
	 * @param element the element
	 */
	public TestCaseWidget (Element element) {
		super(element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public WrapperInterface getWrapper(Element element) {
		return new TestCaseWidget (element);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getId()
	 */
	public String getId() {
		return getAttribute("id");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getName()
	 */
	public String getName() {
		return getAttribute("name");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isAvailable()
	 */
	public boolean isAvailable() {
		if (!hasAttribute("available")) return true;
		return (getAttribute("available").equals("true"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isClickable()
	 */
	public boolean isClickable() {
		if (!hasAttribute("clickable")) return true;
		return (getAttribute("clickable").equals("true"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isLongClickable()
	 */
	public boolean isLongClickable() {
		if (!hasAttribute("long_clickable")) return true;
		return (getAttribute("long_clickable").equals("true"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getCount()
	 */
	public int getCount() {
		if (!hasAttribute("count")) return 1;
		return Integer.parseInt(getAttribute("count"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getValue()
	 */
	public String getValue() {
		return getAttribute("value");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getType()
	 */
	public String getType() {
		return guessType(); 
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getIndex()
	 */
	public int getIndex() {
		if (!hasAttribute("index")) return 0;
		return Integer.parseInt(getAttribute("index"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setIndex(int)
	 */
	public void setIndex (int index) {
		setAttribute("index", String.valueOf(index));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getTextType()
	 */
	public String getTextType() {
		return hasAttribute("text_type")?getAttribute("text_type"):"";
	}

	/**
	 * Guess type.
	 *
	 * @return the string
	 */
	public String guessType() {
		String type = getAttribute("type");
		return (type.indexOf('@') == -1)?type:type.substring(0, type.indexOf('@'));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setId(java.lang.String)
	 */
	public void setId(String id) {
		setAttribute("id", id);

	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setName(java.lang.String)
	 */
	public void setName(String name) {
		setAttribute("name", name);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setValue(java.lang.String)
	 */
	public void setValue (String value) {
		setAttribute("value", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setAvailable(java.lang.String)
	 */
	public void setAvailable (String value) {
		setAttribute("available", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setClickable(java.lang.String)
	 */
	public void setClickable (String value) {
		setAttribute("clickable", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setLongClickable(java.lang.String)
	 */
	public void setLongClickable (String value) {
		setAttribute("long_clickable", value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setType(java.lang.String)
	 */
	public void setType(String type) {
		setAttribute("type", type);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setSimpleType(java.lang.String)
	 */
	public void setSimpleType(String type) {
		setAttribute("simple_type", type);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setTextType(java.lang.String)
	 */
	public void setTextType(String inputType) {
		setAttribute("text_type", inputType);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setCount(int)
	 */
	public void setCount(int count) {
		setAttribute("count", String.valueOf(count));
	}

	/**
	 * Sets the id name type.
	 *
	 * @param id the id
	 * @param name the name
	 * @param type the type
	 */
	public void setIdNameType (String id, String name, String type) {
		setId (id);
		if (!name.equals("")) setName (name);
		setType (type);
	}

	/**
	 * Creates the widget.
	 *
	 * @param dom the dom
	 * @return the test case widget
	 */
	public static TestCaseWidget createWidget (Document dom) {
		return new TestCaseWidget (dom.createElement(TAG));		
	}

	/**
	 * Creates the widget.
	 *
	 * @param session the session
	 * @return the test case widget
	 */
	public static TestCaseWidget createWidget (GuiTree session) {
		return createWidget (session.getDom());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
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

	/**
	 * Gets the available.
	 *
	 * @return the available
	 */
	protected String getAvailable () {
		return getAttribute("available");
	}

	/**
	 * Gets the clickable.
	 *
	 * @return the clickable
	 */
	protected String getClickable() {
		return getAttribute("clickable");
	}

	/**
	 * Gets the long clickable.
	 *
	 * @return the long clickable
	 */
	protected String getLongClickable() {
		return getAttribute("long_clickable");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getSimpleType()
	 */
	public String getSimpleType() {
		return getAttribute("simple_type");
	}

}
