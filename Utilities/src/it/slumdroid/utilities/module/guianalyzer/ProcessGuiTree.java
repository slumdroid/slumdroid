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

public class ProcessGuiTree {

	protected String[] widgetClasses;
	public int numWidgets = 0;
	public int numPerturbatedWidgets = 0;
	private HashMap<String, WidgetState> Widgets = null;
	private HashMap<String, String> Interactions = null;
	private HashMap<String, String> Screens = null;
	private GuiTree guiTree = null;

	public ProcessGuiTree (String inputFileName) {
		try {
			guiTree = GuiTree.fromXml(new File(inputFileName));
			processFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFile () throws Exception {
		Widgets = new HashMap<String, WidgetState>();
		Screens = new HashMap<String, String>();
		Interactions = new HashMap<String, String>();
		try {
			for (Task task: guiTree) {
				for (Transition transition: task) {
					for (UserInput input: transition) {
						if (input.getType().equals(WRITE_TEXT)) {                       	
							if (!Screens.containsKey(input.getWidgetId())) {
								Widgets.put(input.getWidgetId(), input.getWidget());
								Screens.put(input.getWidgetId(), transition.getStartActivity().getId() + ".jpg");
								Interactions.put(input.getWidgetId(), "Input");
							}
						}
					}
					UserEvent event = transition.getEvent();
					if (event.getType().equals(WRITE_TEXT)) {
						if (!Screens.containsKey(event.getWidgetId())) {
							Widgets.put(event.getWidgetId(), event.getWidget());
							Screens.put(event.getWidgetId(), transition.getStartActivity().getId() + ".jpg");
							Interactions.put(event.getWidgetId(), "Event");
						} else {
							Interactions.remove(event.getWidgetId());
							Interactions.put(event.getWidgetId(), "Input & Event");
						}
					}
					if (event.getType().equals(ENTER_TEXT)) {
						if (!Screens.containsKey(event.getWidgetId())) {
							Widgets.put(event.getWidgetId(), event.getWidget());
							Screens.put(event.getWidgetId(), transition.getStartActivity().getId() + ".jpg");
							Interactions.put(event.getWidgetId(), "Event");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		numWidgets = Widgets.size();
	}

	public HashMap<String, WidgetState> getWidgets () {
		return Widgets;
	}

	public HashMap<String, String> getScreens () {
		return Screens;  
	}

	public HashMap<String, String> getInteractions () {
		return Interactions;  
	}

}