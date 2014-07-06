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

package it.slumdroid.droidmodels.xml;

import it.slumdroid.droidmodels.model.WrapperInterface;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ElementWrapper implements WrapperInterface {

	public ElementWrapper() {
		super();
	}

	public ElementWrapper (Element e) {
		super();
		setElement(e);
	}

	public ElementWrapper (XmlGraph g, String tag) {
		this (g.getDom(), tag);
	}

	public ElementWrapper (Document d, String tag) {
		this (d.createElement(tag));
	}

	public void setElement(Element e) {
		this.element = e;
	}

	public Element getElement() {
		return this.element;
	}

	public void setAttribute(String name, String value) {
		getElement().setAttribute(name, value);
	}

	public String getAttribute (String name) {
		return getElement().getAttribute(name);
	}

	public boolean hasAttribute (String name) {
		return getElement().hasAttribute(name);
	}

	public void appendChild (Element child) {
		getElement().appendChild(child);
	}

	public void appendChild (ElementWrapper child) {
		getElement().appendChild(child.getElement());
	}

	public String toXml () throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(getElement());
		StringWriter autput = new StringWriter();
		getTransformer().transform(theDom, new StreamResult(autput));
		return autput.toString();
	}

	protected Transformer getTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		if (t instanceof Transformer) return t;
		t = TransformerFactory.newInstance().newTransformer();
		return t;
	}

	protected Element element;
	protected static Transformer t;

}
