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

public abstract class XmlGraph {
	
	protected String doctypeSystem = new String();
	protected String doctypePublic = new String();
	protected String indent = "yes";
	protected int indentAmount = 4;
	protected String method = "xml";
	static boolean validation = true;
	static private DocumentBuilder builder = null;

	public abstract Document getDom ();

	public XmlGraph () {
		this ("","");
	}

	public XmlGraph (String systemId, String publicId) {
		this.setDoctype(systemId, publicId);
	}

	public void setDoctype (String systemId, String publicId) {
		this.doctypePublic = publicId;
		this.doctypeSystem = systemId;
	}

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

	public static boolean isValidation() {
		return validation;
	}

	public static void setValidation(boolean v) {
		validation = v;
	}

}
