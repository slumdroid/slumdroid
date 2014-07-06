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

package it.slumdroid.guianalyzer.tools;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter {

	private Document doc;
	private Element root;

	public XmlWriter () {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument(); 
		} catch (ParserConfigurationException ex) {
			System.err.println(ex.getMessage());
		}

		root = doc.createElement("widgets");
		doc.appendChild(root);

	}

	public void add (String id,  String textType, String name, String value,  String applied) {

		Element idEl = doc.createElement("id");
		idEl.setAttribute("value", id);
		root.appendChild(idEl);

		Element nameEl = doc.createElement("Description");
		nameEl.appendChild(doc.createTextNode(name));
		idEl.appendChild(nameEl);

		Element valueEl = doc.createElement("Value");
		valueEl.appendChild(doc.createTextNode(value));
		idEl.appendChild(valueEl);

		Element textTypeEl = doc.createElement("WidgetType");
		textTypeEl.appendChild(doc.createTextNode(textType));
		idEl.appendChild(textTypeEl);

		Element appliedEl = doc.createElement("AppliedPerturbation");
		appliedEl.appendChild(doc.createTextNode(applied));
		idEl.appendChild(appliedEl);

	}

	public void write (String outputFileName) {

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource domSource = new DOMSource(doc);
			StreamResult outputstream = new StreamResult(outputFileName);
			transformer.transform(domSource, outputstream);
		} catch (TransformerException ex) {
			System.err.println(ex.getMessage());
		}

	}
}
