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

package it.slumdroid.androidtest.source;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.slumdroid.droidmodels.xml.XmlGraph;

public class SourceCodeBuilder {

	public SourceCodeBuilder () {
		this ("");
	}

	public SourceCodeBuilder (String className) {
		this.s = new StringBuilder ();
		setClassName (className);
	}

	public void setClassName (String className) {
		this.className = className;
	}

	public String getClassName () {
		return this.className;
	}

	public SourceCodeBuilder moreIndent () {
		this.indent++;
		return this;
	}

	public SourceCodeBuilder lessIndent () {
		this.indent--;
		if (this.indent < 0)
			this.indent = 0;
		return this;
	}

	private void tab () {
		for (int i=0; i<indent; i++)
			this.s.append(TAB);
	}

	public SourceCodeBuilder loc (String code) {
		if ( (code.length()>0) && (code.charAt(0) == '}'))
			lessIndent();
		tab();
		if (getClassName() != "")
			code = code.replaceAll("%TEST_CLASS_NAME%", getClassName());
		s.append (code + EOL);
		if ((code.length()>0) && (code.charAt(code.length()-1) == '{'))
			moreIndent();
		return this;
	}

	public SourceCodeBuilder blank () {
		s.append(EOL);
		return this;
	}

	public String toString () {
		return s.toString();
	}

	public void includeSnippet (String resourceName) {
		InputStream u = XmlGraph.class.getClassLoader().getResourceAsStream("ext/"+resourceName);
		if (u == null)
			return;
		try {
			DataInputStream in = new DataInputStream(u);
			BufferedReader code = new BufferedReader(new InputStreamReader(in));
			include (code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void include (BufferedReader code) throws IOException {
		String line;
		while ((line = code.readLine()) != null) {
			loc(line.trim());
		}
		blank();
	}

	private int indent = 0; // Indent level
	StringBuilder s;
	private String className;

	final static String EOL = System.getProperty("line.separator");
	final static String TAB = "\t";

}