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

import it.slumdroid.droidmodels.guitree.FinalActivity;
import it.slumdroid.droidmodels.guitree.StartActivity;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.NodeListWrapper;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestCaseTransition extends ElementWrapper implements Transition {

	public final static String TAG = "TRANSITION";

	public TestCaseTransition () {
		super();
	}

	public TestCaseTransition (Element transition)	{
		super(transition);
	}

	public TestCaseTransition (Document dom) {
		super (dom, TAG);
	}

	public NodeList eventProperties () {
		return getElement().getChildNodes();
	}

	public TestCaseTransition getWrapper(Element e) {
		return new TestCaseTransition(e);
	}

	public StartActivity getStartActivity() {
		return new StartActivity ((Element) eventProperties().item(0));
	}

	public void setStartActivity (ActivityState a) {
		getElement().replaceChild(a.getElement(), getStartActivity().getElement());
	}	

	public Iterator<UserInput> inputs() {
		return new NodeListWrapper<UserInput>((Element) eventProperties().item(1), new TestCaseInput());
	}

	public Iterator<UserInput> iterator() {
		return inputs();
	}

	public void addInput (UserInput i) {
		eventProperties().item(1).appendChild(i.getElement());
	}

	public TestCaseEvent getEvent() {
		return new TestCaseEvent ((Element) eventProperties().item(2));
	}

	public void setEvent (UserEvent e) {
		getElement().replaceChild(e.getElement(), getEvent().getElement());
	}

	public FinalActivity getFinalActivity() {
		return new FinalActivity ((Element) eventProperties().item(3));
	}

	public void setFinalActivity (ActivityState a) {
		getElement().replaceChild(a.getElement(), getFinalActivity().getElement());
	}

	public String getId() {
		return getElement().getAttribute("id_transition");
	}

	public void setId (String id) {
		getElement().setAttribute("id_transition",id);
	}

	public static TestCaseTransition createTransition (Document dom) {
		TestCaseTransition transition = new TestCaseTransition(dom);
		StartActivity sa = StartActivity.createActivity(dom);
		transition.appendChild(sa); 
		Element inputz = dom.createElement("INPUTS");
		transition.appendChild(inputz);
		TestCaseEvent e = TestCaseEvent.createEvent(dom);
		transition.appendChild(e); 
		FinalActivity fa = FinalActivity.createActivity(dom);
		transition.appendChild(fa); 
		return transition;
	}

	public TestCaseTransition clone () {
		TestCaseTransition theTransition = createTransition (this.getElement().getOwnerDocument());
		theTransition.setStartActivity(this.getStartActivity().clone());
		for (UserInput i: this) {
			theTransition.addInput (((TestCaseInput)i).clone());
		}
		theTransition.setEvent(this.getEvent().clone());
		theTransition.setFinalActivity(this.getFinalActivity().clone());
		return theTransition;
	}

}
