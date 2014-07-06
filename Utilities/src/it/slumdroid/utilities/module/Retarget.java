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

package it.slumdroid.utilities.module;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Retarget {

	public void retarget(String fileName, String targetPackage) {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element manifest = document.getDocumentElement();
		Element target = (Element)manifest.getElementsByTagName("instrumentation").item(0);
		target.setAttribute("android:targetPackage", targetPackage);

		String newManifest = new String();
		try {
			newManifest = toXml(manifest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter autput;
		try {
			autput = new PrintWriter (fileName);
			autput.println(newManifest);
			autput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String toXml (Element dom) throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(dom);
		StringWriter autput = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.transform(theDom, new StreamResult(autput));
		return autput.toString();
	}

}
