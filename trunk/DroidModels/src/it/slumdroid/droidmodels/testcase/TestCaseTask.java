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
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class TestCaseTask.
 */
public class TestCaseTask extends ElementWrapper implements Task {

	/** The Constant TAG. */
	public final static String TAG = "TASK";

	/**
	 * Instantiates a new test case task.
	 */
	public TestCaseTask() {
		super();
	}

	/**
	 * Instantiates a new test case task.
	 *
	 * @param task the task
	 */
	public TestCaseTask(Element task) {
		super(task);
	}

	/**
	 * Instantiates a new test case task.
	 *
	 * @param session the session
	 */
	public TestCaseTask(GuiTree session) {
		this(session.getDom());
	}

	/**
	 * Instantiates a new test case task.
	 *
	 * @param dom the dom
	 */
	public TestCaseTask(Document dom) {
		super(dom, TAG);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#getId()
	 */
	public String getId() {
		return getAttribute("id");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#setId(java.lang.String)
	 */
	public void setId(String id) {
		setAttribute("id", id);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#isFailed()
	 */
	public boolean isFailed() {
		if (!hasAttribute("fail")) {
			return false;
		}
		return (getAttribute("fail").equals("true"));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#setFailed(boolean)
	 */
	public void setFailed(boolean failure) {
		setAttribute("fail", (failure)?"true":"false");
	}

	/**
	 * Sets the failed.
	 *
	 * @param failure the new failed
	 */
	public void setFailed(String failure) {
		setAttribute("fail", failure);
	}

	/**
	 * Gets the failed.
	 *
	 * @return the failed
	 */
	protected String getFailed() {
		return getAttribute("fail");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	public TestCaseTask getWrapper(Element element) {
		return new TestCaseTask (element);
	}

	// Iterator Methods
	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#transitions()
	 */
	public Iterator<Transition> transitions() {
		Element transition = getElement();
		if (transition.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Transition>(transition, new TestCaseTransition());
		}
		return null;		
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Transition> iterator() {
		return transitions();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#addTransition(it.slumdroid.droidmodels.model.Transition)
	 */
	public void addTransition(Transition theTransition) {
		appendChild(theTransition.getElement());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TestCaseTask clone() {
		TestCaseTask task = new TestCaseTask(getElement().getOwnerDocument());
		for (Transition child: this) {
			TestCaseTransition newChild = ((TestCaseTransition)child).clone();
			task.addTransition(newChild);
		}
		task.setFailed(getFailed());
		return task;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#setFinalActivity(it.slumdroid.droidmodels.model.ActivityState)
	 */
	public void setFinalActivity(ActivityState theState) {
		Transition lastTransition = getFinalTransition();
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theState);
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.Task#getFinalTransition()
	 */
	public Transition getFinalTransition() {
		Transition lastTransition = null;
		for (Transition transition: this) {
			lastTransition = transition;
		}
		return lastTransition;
	}

}
