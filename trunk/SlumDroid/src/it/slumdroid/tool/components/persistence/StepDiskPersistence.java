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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.utilities.SessionParams;
import android.content.ContextWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class StepDiskPersistence.
 */
public class StepDiskPersistence extends DiskPersistence implements SaveStateListener {

	/** The step. */
	private int step = 1;

	/** The count. */
	private int count = 0;

	/** The footer. */
	private String footer = new String();

	/** The first. */
	private boolean first = true;

	/** The last. */
	private boolean last = false;

	/** The actor name. */
	public final String ACTOR_NAME = "StepDiskPersistence";

	/** The param name. */
	public final String PARAM_NAME = "footer";

	/** The xml body begin. */
	private final String XML_BODY_BEGIN = "    <TASK";

	/** The xml body end. */
	private final String XML_BODY_END = "/TASK>";

	/**
	 * Instantiates a new step disk persistence.
	 */
	public StepDiskPersistence () {
		super();
		PersistenceFactory.registerForSavingState(this);
	}

	/**
	 * Instantiates a new step disk persistence.
	 *
	 * @param theSession the the session
	 */
	public StepDiskPersistence (Session theSession) {
		super(theSession);
		PersistenceFactory.registerForSavingState(this);
	}

	/**
	 * Instantiates a new step disk persistence.
	 *
	 * @param theStep the the step
	 */
	public StepDiskPersistence (int theStep) {
		this();
		setStep(theStep);
	}

	/**
	 * Instantiates a new step disk persistence.
	 *
	 * @param theSession the the session
	 * @param theStep the the step
	 */
	public StepDiskPersistence (Session theSession, int theStep) {
		this (theSession);
		setStep (theStep);
	}

	/**
	 * Sets the step.
	 *
	 * @param theStep the new step
	 */
	public void setStep (int theStep) {
		this.step = theStep;		
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.DiskPersistence#addTask(it.slumdroid.droidmodels.model.Task)
	 */
	@Override
	public void addTask (Task task) {
		super.addTask (task);
		this.count++;
		if (this.count == this.step) {
			saveStep();
			this.count = 0;
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.DiskPersistence#generate()
	 */
	@Override
	public String generate() {
		return generateXML() + System.getProperty("line.separator");
	}

	/**
	 * Generate xml.
	 *
	 * @return the string
	 */
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
			return new String();
		}
		// Return the body of the XML graph
		return graph.substring(bodyBegin,bodyEnd);
	}

	/**
	 * Save step.
	 */
	public void saveStep () {
		save(isLast());
		for (Task task: getSession()) {
			getSession().removeTask(task);
		}
		setNotFirst();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.DiskPersistence#save()
	 */
	@Override
	public void save () {
		save (true);
	}

	/**
	 * Save.
	 *
	 * @param last the last
	 */
	public void save (boolean last) {
		if (!isFirst()) {
			this.mode = ContextWrapper.MODE_APPEND;
		}
		if (last) {
			setLast();		
		}
		super.save();
	}

	/**
	 * Checks if is first.
	 *
	 * @return true, if is first
	 */
	public boolean isFirst () {
		return this.first;
	}

	/**
	 * Checks if is last.
	 *
	 * @return true, if is last
	 */
	public boolean isLast () {
		return this.last;
	}

	/**
	 * Sets the not first.
	 */
	public void setNotFirst () {
		this.first = false;
	}

	/**
	 * Sets the last.
	 */
	public void setLast () {
		this.last = true;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#getListenerName()
	 */
	@Override
	public String getListenerName() {
		return ACTOR_NAME;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onSavingState()
	 */
	@Override
	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.footer);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onLoadingState(it.slumdroid.tool.utilities.SessionParams)
	 */
	@Override
	public void onLoadingState(SessionParams sessionParams) {
		this.footer = sessionParams.get(PARAM_NAME);
	}

}