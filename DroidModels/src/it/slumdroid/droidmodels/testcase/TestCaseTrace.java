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

package it.slumdroid.droidmodels.testcase;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Trace;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCaseTrace extends ElementWrapper implements Trace {

	public final static String TAG = "TRACE";

	public TestCaseTrace () {
		super();
	}

	public TestCaseTrace (Element trace) {
		super(trace);
	}

	public TestCaseTrace (GuiTree session) {
		this (session.getDom());
	}

	public TestCaseTrace (Document dom) {
		super (dom, TAG);
	}

	public String getId () {
		return getAttribute("id");
	}

	public void setId (String id) {
		setAttribute("id",id);
	}

	public boolean isFailed() {
		if (!hasAttribute("fail")) return false;
		return (getAttribute("fail").equals("true"));
	}

	public boolean isAsync() {
		if (!hasAttribute("async")) return false;
		return (getAttribute("async").equals("true"));
	}

	public void setFailed(boolean failure) {
		setAttribute("fail", (failure)?"true":"false");
	}

	public void setFailed(String failure) {
		setAttribute("fail", failure);
	}

	protected String getFailed() {
		return getAttribute("fail");
	}

	public TestCaseTrace getWrapper(Element e) {
		return new TestCaseTrace (e);
	}

	// Iterator Methods
	public Iterator<Transition> transitions () {
		Element t = getElement();
		if (t.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Transition> (t, new TestCaseTransition());
		}
		return null;		
	}

	public Iterator<Transition> iterator() {
		return transitions();
	}

	public void addTransition(Transition tail) {
		appendChild(tail.getElement());
	}

	@Override
	public TestCaseTrace clone () {
		TestCaseTrace t = new TestCaseTrace (getElement().getOwnerDocument());
		for (Transition child: this) {
			TestCaseTransition newChild = ((TestCaseTransition)child).clone();
			t.addTransition(newChild);
		}
		t.setFailed(getFailed());
		return t;
	}

	public void setFinalActivity(ActivityState theActivity) {
		Transition lastTransition = getFinalTransition();
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theActivity);
		}
	}

	public Transition getFinalTransition() {
		Transition lastTransition = null;
		for (Transition t: this) {
			lastTransition = t;
		}
		return lastTransition;
	}

}
