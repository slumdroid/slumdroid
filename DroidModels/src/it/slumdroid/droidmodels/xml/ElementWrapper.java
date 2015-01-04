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

// TODO: Auto-generated Javadoc
/**
 * The Class ElementWrapper.
 */
public abstract class ElementWrapper implements WrapperInterface {
	
	/** The element. */
	protected Element element;
	
	/** The trasformer. */
	protected static Transformer trasformer;

	/**
	 * Instantiates a new element wrapper.
	 */
	public ElementWrapper() {
		super();
	}

	/**
	 * Instantiates a new element wrapper.
	 *
	 * @param element the element
	 */
	public ElementWrapper (Element element) {
		super();
		setElement(element);
	}

	/**
	 * Instantiates a new element wrapper.
	 *
	 * @param graph the graph
	 * @param tag the tag
	 */
	public ElementWrapper (XmlGraph graph, String tag) {
		this (graph.getDom(), tag);
	}

	/**
	 * Instantiates a new element wrapper.
	 *
	 * @param dom the dom
	 * @param tag the tag
	 */
	public ElementWrapper (Document dom, String tag) {
		this (dom.createElement(tag));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#setElement(org.w3c.dom.Element)
	 */
	public void setElement(Element element) {
		this.element = element;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getElement()
	 */
	public Element getElement() {
		return this.element;
	}

	/**
	 * Sets the attribute.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public void setAttribute(String name, String value) {
		getElement().setAttribute(name, value);
	}

	/**
	 * Gets the attribute.
	 *
	 * @param name the name
	 * @return the attribute
	 */
	public String getAttribute (String name) {
		try{
			return getElement().getAttribute(name);
		}catch (Exception ignore){
			// ignore.printStackTrace();
			return new String();
		}
	}

	/**
	 * Checks for attribute.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean hasAttribute (String name) {
		return getElement().hasAttribute(name);
	}

	/**
	 * Append child.
	 *
	 * @param child the child
	 */
	public void appendChild (Element child) {
		getElement().appendChild(child);
	}

	/**
	 * Append child.
	 *
	 * @param child the child
	 */
	public void appendChild (ElementWrapper child) {
		getElement().appendChild(child.getElement());
	}

	/**
	 * To xml.
	 *
	 * @return the string
	 * @throws TransformerFactoryConfigurationError the transformer factory configuration error
	 * @throws TransformerException the transformer exception
	 */
	public String toXml () throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(getElement());
		StringWriter autput = new StringWriter();
		getTransformer().transform(theDom, new StreamResult(autput));
		return autput.toString();
	}

	/**
	 * Gets the transformer.
	 *
	 * @return the transformer
	 * @throws TransformerConfigurationException the transformer configuration exception
	 * @throws TransformerFactoryConfigurationError the transformer factory configuration error
	 */
	protected Transformer getTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		if (trasformer instanceof Transformer) return trasformer;
		trasformer = TransformerFactory.newInstance().newTransformer();
		return trasformer;
	}

}
