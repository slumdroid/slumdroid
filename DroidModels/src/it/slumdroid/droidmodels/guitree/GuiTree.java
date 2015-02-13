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

package it.slumdroid.droidmodels.guitree;

import static it.slumdroid.droidmodels.model.ActivityState.CRASH;
import static it.slumdroid.droidmodels.model.ActivityState.FAILURE;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;
import it.slumdroid.droidmodels.testcase.TestCaseTask;
import it.slumdroid.droidmodels.xml.NodeListWrapper;
import it.slumdroid.droidmodels.xml.XmlGraph;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class GuiTree.
 */
public class GuiTree extends XmlGraph implements Session {

	/** The gui tree. */
	private Document guiTree;

	/** The Constant TAG. */
	public final static String TAG = "SESSION";

	/**
	 * Instantiates a new gui tree.
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public GuiTree () throws ParserConfigurationException {
		super ("guitree.dtd", TAG);
		this.guiTree = getBuilder().newDocument();
		Element rootElement = this.guiTree.createElement(TAG);
		this.guiTree.appendChild(rootElement);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.xml.XmlGraph#getDom()
	 */
	public Document getDom() {
		return this.guiTree;
	}

	/**
	 * Parses the.
	 *
	 * @param file the file
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public void parse(File file) throws SAXException, IOException, ParserConfigurationException {
		this.guiTree = getBuilder().parse(file);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#parse(java.lang.String)
	 */
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

	// Sets attributes for the whole graph (not the nodes!) 
	// They are stored as attributes of the root node
	/**
	 * Sets the attribute.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setAttribute (String key, String value) {
		this.getDom().getDocumentElement().setAttribute(key, value);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#getDateTime()
	 */
	public String getDateTime () {
		return this.getDom().getDocumentElement().getAttribute("date_time");
	}

	/**
	 * Sets the date time.
	 *
	 * @param date the new date time
	 */
	public void setDateTime (String date) {
		getDom().getDocumentElement().setAttribute("date_time", date);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#getBaseActivity()
	 */
	public ActivityState getBaseActivity () {
		return tasks().next().transitions().next().getStartActivity();
	}

	/**
	 * From xml.
	 *
	 * @param file the file
	 * @return the gui tree
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GuiTree fromXml (File file) throws ParserConfigurationException, SAXException, IOException {
		GuiTree guiT = new GuiTree();
		guiT.parse(file);
		return guiT;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#addTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void addTask (Task task) {
		getDom().getDocumentElement().appendChild(task.getElement());
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#addCrashedTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void addCrashedTask (Task task) {
		addFailedTask (task, CRASH);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#addFailedTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void addFailedTask (Task task) {
		addFailedTask (task, FAILURE);
	}

	/**
	 * Adds the failed task.
	 *
	 * @param task the task
	 * @param failType the fail type
	 */
	protected void addFailedTask (Task task, String failType) {
		task.setFailed(true);
		FinalActivity fail = FinalActivity.createActivity(this);
		fail.setName(failType);
		fail.setId(failType);
		task.setFinalActivity(fail);
		addTask(task);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#removeTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void removeTask (Task task) {
		getDom().getDocumentElement().removeChild(task.getElement());
	}

	// Iterator Methods
	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Session#tasks()
	 */
	public Iterator<Task> tasks() {
		Element session = getDom().getDocumentElement();
		if (session.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Task> (session, new TestCaseTask());
		}
		return null;		
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Task> iterator() {
		return tasks();
	}

	/**
	 * Import state.
	 *
	 * @param fromXml the from xml
	 * @return the test case activity
	 */
	public TestCaseActivity importState (Element fromXml) {
		Element state = (Element)getDom().adoptNode(fromXml);
		TestCaseActivity imported = (state.getNodeName().equals(FinalActivity.getTag()))?FinalActivity.createActivity(this):StartActivity.createActivity(this);
		imported.setElement(state);
		return imported;
	}

}
