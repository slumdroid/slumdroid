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

package it.slumdroid.droidmodels.guitree;

import it.slumdroid.droidmodels.model.*;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;
import it.slumdroid.droidmodels.testcase.TestCaseTrace;
import it.slumdroid.droidmodels.xml.NodeListWrapper;
import it.slumdroid.droidmodels.xml.XmlGraph;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GuiTree extends XmlGraph implements Session {

	public GuiTree () throws ParserConfigurationException {
		super ("guitree.dtd", TAG);
		this.guiTree = getBuilder().newDocument();
		Element rootElement = this.guiTree.createElement(TAG);
		this.guiTree.appendChild(rootElement);
	}

	public Document getDom() {
		return this.guiTree;
	}

	public void parse(File f) throws SAXException, IOException, ParserConfigurationException {
		this.guiTree = getBuilder().parse(f);
	}

	public void parse(String xml) {
		try {
			this.guiTree = getBuilder().parse((new InputSource(new StringReader(xml))));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	// Sets attributes for the whole graph (not the nodes!) They are stored as attributes of the root node
	public void setAttribute (String key, String value) {
		this.getDom().getDocumentElement().setAttribute(key, value);
	}

	public String getDateTime () {
		return this.getDom().getDocumentElement().getAttribute("date_time");
	}

	public void setDateTime (String d) {
		getDom().getDocumentElement().setAttribute("date_time", d);
	}

	public ActivityState getBaseActivity () {
		return traces().next().transitions().next().getStartActivity();
	}

	public static GuiTree fromXml (File f) throws ParserConfigurationException, SAXException, IOException {
		GuiTree g = new GuiTree();
		g.parse(f);
		return g;
	}

	public void addTrace (Trace t) {
		getDom().getDocumentElement().appendChild(t.getElement());
	}

	public void addCrashedTrace (Trace t) {
		addFailedTrace (t,ActivityState.CRASH);
	}

	public void addFailedTrace (Trace t) {
		addFailedTrace (t,ActivityState.FAILURE);
	}

	protected void addFailedTrace (Trace t, String failType) {
		t.setFailed(true);
		FinalActivity fail = FinalActivity.createActivity(this);
		fail.setName(failType);
		fail.setId(failType);
		fail.setTitle(failType);
		t.setFinalActivity(fail);
		addTrace(t);
	}

	public void removeTrace (Trace t) {
		getDom().getDocumentElement().removeChild(t.getElement());
	}

	// Iterator Methods
	public Iterator<Trace> traces() {
		Element session = getDom().getDocumentElement();
		if (session.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Trace> (session, new TestCaseTrace());
		}
		return null;		
	}

	public Iterator<Trace> iterator() {
		return traces();
	}

	public TestCaseActivity importState (Element fromXml) {
		Element state = (Element)getDom().adoptNode(fromXml);
		TestCaseActivity imported = (state.getNodeName().equals(FinalActivity.getTag()))?FinalActivity.createActivity(this):StartActivity.createActivity(this);
		imported.setElement(state);
		return imported;
	}

	private Document guiTree;
	public final static String TAG = "SESSION";

}
