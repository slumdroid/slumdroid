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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlGraph.
 */
public abstract class XmlGraph {
	
	/** The doctype system. */
	protected String doctypeSystem = new String();
	
	/** The doctype public. */
	protected String doctypePublic = new String();
	
	/** The indent. */
	protected String indent = "yes";
	
	/** The indent amount. */
	protected int indentAmount = 4;
	
	/** The method. */
	protected String method = "xml";
	
	/** The validation. */
	static boolean validation = true;
	
	/** The builder. */
	static private DocumentBuilder builder = null;

	/**
	 * Gets the dom.
	 *
	 * @return the dom
	 */
	public abstract Document getDom ();

	/**
	 * Instantiates a new xml graph.
	 */
	public XmlGraph () {
		this ("","");
	}

	/**
	 * Instantiates a new xml graph.
	 *
	 * @param systemId the system id
	 * @param publicId the public id
	 */
	public XmlGraph (String systemId, String publicId) {
		this.setDoctype(systemId, publicId);
	}

	/**
	 * Sets the doctype.
	 *
	 * @param systemId the system id
	 * @param publicId the public id
	 */
	public void setDoctype (String systemId, String publicId) {
		this.doctypePublic = publicId;
		this.doctypeSystem = systemId;
	}

	/**
	 * To xml.
	 *
	 * @return the string
	 * @throws TransformerFactoryConfigurationError the transformer factory configuration error
	 * @throws TransformerException the transformer exception
	 */
	public String toXml () throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(this.getDom());
		StringWriter autput = new StringWriter();
		Transformer trasformer = TransformerFactory.newInstance().newTransformer();
		trasformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, this.doctypeSystem);
		trasformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, this.doctypePublic);
		trasformer.setOutputProperty(OutputKeys.INDENT, this.indent);
		trasformer.setOutputProperty(OutputKeys.METHOD, this.method);
		trasformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + this.indentAmount);
		trasformer.transform(theDom, new StreamResult(autput));
		return autput.toString();
	}

	/**
	 * Gets the builder.
	 *
	 * @return the builder
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	static protected DocumentBuilder getBuilder () throws ParserConfigurationException {
		if (builder instanceof DocumentBuilder)
			return builder;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			if (isValidation()) {
				factory.setValidating(false);
				factory.setIgnoringElementContentWhitespace(true);
			}

			builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver (){
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					String dtdFileName = systemId.substring(systemId.lastIndexOf("/") + 1);
					InputStream input = XmlGraph.class.getClassLoader().getResourceAsStream("ext/" + dtdFileName);
					return (input == null)?null:new InputSource (input);
				}
			});

		} catch (ParserConfigurationException e) {
			throw e;
		}
		return builder;
	}

	/**
	 * Checks if is validation.
	 *
	 * @return true, if is validation
	 */
	public static boolean isValidation() {
		return validation;
	}

	/**
	 * Sets the validation.
	 *
	 * @param value the new validation
	 */
	public static void setValidation(boolean value) {
		validation = value;
	}

}
