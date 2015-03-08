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

package it.slumdroid.utilities.module.guianalyzer;

import static it.slumdroid.droidmodels.model.InteractionType.ENTER_TEXT;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;

import java.io.File;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessGuiTree.
 */
public class ProcessGuiTree {

	/** The Widgets. */
	private HashMap<String, WidgetState> Widgets;

	/** The Interactions. */
	private HashMap<String, String> Interactions;

	/** The Screens. */
	private HashMap<String, String> Screens;

	/** The gui tree. */
	private GuiTree guiTree;

	/**
	 * Instantiates a new process gui tree.
	 *
	 * @param inputFileName the input file name
	 */
	public ProcessGuiTree(String inputFileName) {
		try {
			this.guiTree = GuiTree.fromXml(new File(inputFileName));
			this.Widgets = new HashMap<String, WidgetState>();
			this.Screens = new HashMap<String, String>();
			this.Interactions = new HashMap<String, String>();
			processFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process file.
	 *
	 * @throws Exception the exception
	 */
	private void processFile() throws Exception {
		try {
			for (Task task: this.guiTree) {
				for (Transition transition: task) {
					for (UserInput input: transition) {
						if (input.getType().equals(WRITE_TEXT)) {                       	
							if (!this.Screens.containsKey(input.getWidgetId())) {
								this.Widgets.put(input.getWidgetId(), input.getWidget());
								this.Screens.put(input.getWidgetId(), transition.getStartActivity().getScreenshot());
								this.Interactions.put(input.getWidgetId(), "Input");	
							}
						}
					}
					UserEvent event = transition.getEvent();
					if (event.getType().equals(WRITE_TEXT)) {
						if (!this.Screens.containsKey(event.getWidgetId())) {
							this.Widgets.put(event.getWidgetId(), event.getWidget());
							this.Screens.put(event.getWidgetId(), transition.getStartActivity().getScreenshot());
							this.Interactions.put(event.getWidgetId(), "Event");
						} else {
							if (this.Interactions.get(event.getWidgetId()).equals("Input")) {
								this.Interactions.remove(event.getWidgetId());
								this.Interactions.put(event.getWidgetId(), "Input & Event");	
							}
						}
					}
					if (event.getType().equals(ENTER_TEXT)) {
						if (!this.Screens.containsKey(event.getWidgetId())) {
							this.Widgets.put(event.getWidgetId(), event.getWidget());
							this.Screens.put(event.getWidgetId(), transition.getStartActivity().getScreenshot());
							this.Interactions.put(event.getWidgetId(), "Event");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the widgets.
	 *
	 * @return the widgets
	 */
	public HashMap<String, WidgetState> getWidgets() {
		return Widgets;
	}

	/**
	 * Gets the screens.
	 *
	 * @return the screens
	 */
	public HashMap<String, String> getScreens() {
		return Screens;  
	}

	/**
	 * Gets the interactions.
	 *
	 * @return the interactions
	 */
	public HashMap<String, String> getInteractions() {
		return Interactions;  
	}

	/**
	 * Gets the num widgets.
	 *
	 * @return the num widgets
	 */
	public int getNumWidgets() {
		return Widgets.size();
	}

}