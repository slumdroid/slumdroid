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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.SessionParams;
import android.content.ContextWrapper;

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;

import static it.slumdroid.tool.Resources.ENABLE_MODEL;

public class StepDiskPersistence extends DiskPersistence implements SaveStateListener {

	private int step = 1;
	private int count = 0;
	private String footer = "";
	private boolean first = true;
	private boolean last = false;

	public final static String ACTOR_NAME = "StepDiskPersistence";
	public final static String PARAM_NAME = "footer";

	private final static String XML_BODY_BEGIN = "    <TASK";
	private final static String XML_BODY_END = "/TASK>";

	public StepDiskPersistence () {
		super();
		PersistenceFactory.registerForSavingState(this);
	}

	public StepDiskPersistence (Session theSession) {
		super(theSession);
		PersistenceFactory.registerForSavingState(this);
	}

	public StepDiskPersistence (int theStep) {
		this();
		setStep(theStep);
	}

	public StepDiskPersistence (Session theSession, int theStep) {
		this (theSession);
		setStep (theStep);
	}

	public void setStep (int theStep) {
		this.step = theStep;		
	}

	@Override
	public void addTask (Task t) {
		super.addTask (t);
		this.count++;
		if (this.count == this.step) {
			saveStep();
			this.count=0;
		}
	}

	@Override
	public String generate() {
		return generateXML() + System.getProperty("line.separator");
	}

	public String generateXML () {
		String graph = super.generate();
		// Session is smaller than the step: fall back to DiskPersistence behavior and save all
		if (isFirst() && isLast()) {
			return graph;
		}
		int bodyBegin = graph.indexOf(XML_BODY_BEGIN);
		int bodyEnd = graph.lastIndexOf(XML_BODY_END) + XML_BODY_END.length();
		// First step: return header and body, save the footer for the final step
		if (isFirst()) {
			this.footer = graph.substring(bodyEnd);
			return graph.substring(0,bodyEnd);
		}
		// Final step: return the body (if any) and the footer
		if (isLast()) {
			return (bodyBegin == -1)?(this.footer):graph.substring(bodyBegin);
		}
		if ( (bodyBegin == -1) || (bodyEnd == -1) ) { // Empty body
			return "";
		}
		// Return the body of the XML graph
		return graph.substring(bodyBegin,bodyEnd);
	}

	public void saveStep () {
		if (ENABLE_MODEL) save(isLast());
		for (Task t: getSession()) {
			getSession().removeTask(t);
		}
		setNotFirst();
	}

	@Override
	public void save () {
		save (true);
	}

	public void save (boolean last) {
		if (!isFirst()) this.mode = ContextWrapper.MODE_APPEND;
		if (last) setLast();		
		if (ENABLE_MODEL) super.save();
	}

	public boolean isFirst () {
		return this.first;
	}

	public boolean isLast () {
		return this.last;
	}

	public void setNotFirst () {
		this.first = false;
	}

	public void setLast () {
		this.last = true;
	}

	@Override
	public String getListenerName() {
		return ACTOR_NAME;
	}

	@Override
	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.footer);
	}

	@Override
	public void onLoadingState(SessionParams sessionParams) {
		this.footer = sessionParams.get(PARAM_NAME);
	}

}