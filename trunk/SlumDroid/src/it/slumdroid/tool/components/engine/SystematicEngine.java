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

package it.slumdroid.tool.components.engine;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.tool.components.GuiTreeAbstractor;
import it.slumdroid.tool.components.UltraPlanner;
import it.slumdroid.tool.components.automation.Automation;
import it.slumdroid.tool.components.comparator.CompositionalComparator;
import it.slumdroid.tool.components.persistence.PersistenceFactory;
import it.slumdroid.tool.components.scheduler.TraceDispatcher;
import it.slumdroid.tool.components.strategy.StrategyFactory;
import it.slumdroid.tool.model.Filter;
import it.slumdroid.tool.model.Strategy;
import it.slumdroid.tool.model.UserAdapter;
import it.slumdroid.tool.utilities.AllPassFilter;
import it.slumdroid.tool.utilities.SimpleRestarter;
import it.slumdroid.tool.utilities.SimpleTypeDetector;
import it.slumdroid.tool.utilities.UserFactory;

import java.util.GregorianCalendar;

import javax.xml.parsers.ParserConfigurationException;

public class SystematicEngine extends Engine {

	private Automation theAutomation;
	private GuiTreeAbstractor guiAbstractor;
	private UserAdapter user;
	private SimpleRestarter theRestarter;
	private GuiTree theGuiTree;
	protected StrategyFactory theStrategyFactory;
	protected PersistenceFactory thePersistenceFactory;

	public SystematicEngine () {
		super ();
		setScheduler(new TraceDispatcher());
		this.theAutomation = new Automation();
		this.theRestarter = new SimpleRestarter();
		setExecutor (this.theAutomation);
		setExtractor (this.theAutomation);
		setImageCaptor(this.theAutomation);
		try {
			GuiTree.setValidation(false);
			this.guiAbstractor = new GuiTreeAbstractor();
			this.theGuiTree = this.guiAbstractor.getTheSession();
			GregorianCalendar c=new GregorianCalendar();
			theGuiTree.setDateTime(c.getTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAbstractor(this.guiAbstractor);
		setSession (this.theGuiTree);
		UltraPlanner planner = new UltraPlanner();
		Filter inputFilter = new AllPassFilter();
		planner.setInputFilter (inputFilter);
		this.guiAbstractor.addFilter (inputFilter);
		Filter eventFilter = new AllPassFilter();
		planner.setEventFilter (eventFilter);
		this.guiAbstractor.addFilter (eventFilter);
		this.guiAbstractor.setTypeDetector(new SimpleTypeDetector());
		this.user = UserFactory.getUser(this.guiAbstractor);
		planner.setUser(user);
		planner.setFormFiller(user);
		planner.setAbstractor(this.guiAbstractor);
		setPlanner (planner);		
		this.theStrategyFactory = new StrategyFactory(new CompositionalComparator()); 
		this.thePersistenceFactory = new PersistenceFactory (this.theGuiTree, getScheduler());
	}

	protected void setUp (){		
		Strategy s = this.theStrategyFactory.getStrategy();
		setStrategy (s);
		this.thePersistenceFactory.setStrategy(s);
		setPersistence (this.thePersistenceFactory.getPersistence());
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		theRestarter.setRestartPoint(theAutomation.getActivity());
	}	

	public Session getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

}
