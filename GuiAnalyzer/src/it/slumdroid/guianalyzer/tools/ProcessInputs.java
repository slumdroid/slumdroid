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

package it.slumdroid.guianalyzer.tools;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.*;
import static it.slumdroid.droidmodels.model.InteractionType.*;
import it.slumdroid.guianalyzer.perturbation.PerturbationHandler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ProcessInputs {

	protected String[] widgetClasses;
	public long executionTime;
	public int totalTestGenerated = 0;
	public int numWidgets = 0;
	public int numPerturbatedWidgets = 0;
	public PerturbationHandler perturbationHandler;
	private HashMap<String, WidgetState> Widgets = null;
	private HashMap<String, String> Screens = null;
	private HashMap<String, List<WidgetState>> WidgetsPerturbated = null;
	private GuiTree guiTree = null;

	public ProcessInputs (String inputFileName, String outputFileName) throws Exception {

		try {
			File file = new File(inputFileName);
			guiTree = GuiTree.fromXml(file);
		} catch (ParserConfigurationException e) {
			throw new Exception(e.toString());
		} catch (SAXException e) {
			throw new Exception(e.toString());
		} catch (IOException e) {
			throw new Exception("File \"" + inputFileName + "\" not found.");
		}

		processFile();
	}

	private void processFile () throws Exception {
		Widgets = new HashMap<String, WidgetState>();
		Screens = new HashMap<String, String>();

		try {
			for (Task t: guiTree) {
				for (Transition tr: t) {
					for (UserInput input: tr) {
						if (input.getType().equals(WRITE_TEXT)) {                       	
							if (!Screens.containsKey(input.getWidgetId())) {
								Widgets.put(input.getWidgetId(), input.getWidget());
								Screens.put(input.getWidgetId(), tr.getStartActivity().getId() + ".jpg");
							}
						}
					}

					UserEvent event = tr.getEvent();
					if (event.getType().equals(WRITE_TEXT)) {
						if (!Screens.containsKey(event.getWidgetId())) {
							Widgets.put(event.getWidgetId(), event.getWidget());
							Screens.put(event.getWidgetId(), tr.getStartActivity().getId() + ".jpg");
						}
					}

					if (event.getType().equals(ENTER_TEXT)) {
						if (!Screens.containsKey(event.getWidgetId())) {
							Widgets.put(event.getWidgetId(), event.getWidget());
							Screens.put(event.getWidgetId(), tr.getStartActivity().getId() + ".jpg");
						}
					}

				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		numWidgets = Widgets.size();
		perturbationHandler = new PerturbationHandler(Widgets);

	}

	public void ListWidgets () {

		Collection<WidgetState> WidgetsColl = Widgets.values();

		for (WidgetState widget: WidgetsColl) {
			System.out.println("ID: " + widget.getId() + " - Name: " + widget.getName() + " - Value: " + widget.getValue());
		}

	}

	public void PerturbWidgets () {

		perturbationHandler.perturb();
		WidgetsPerturbated = perturbationHandler.getWidgetsPerturbated();
		Collection<List<WidgetState>> WidgetsPerturbatedColl = WidgetsPerturbated.values();

		numPerturbatedWidgets = 0;
		for (List<WidgetState> list: WidgetsPerturbatedColl) {
			numPerturbatedWidgets += list.size();
		}

	}

	public void ListPerturbatedWidgets () {

		Collection<List<WidgetState>> WidgetsPerturbatedColl = WidgetsPerturbated.values();

		for (List<WidgetState> list: WidgetsPerturbatedColl) {
			for (WidgetState element: list) {
				System.out.println("ID: " + element.getId() + " - Name: " + element.getName() + " - Value: " + element.getValue());
			}
		}

	}

	public HashMap<String, WidgetState> getWidgets () {
		return Widgets;
	}

	public HashMap<String, List<WidgetState>> getWidgetsPerturbated () {
		return WidgetsPerturbated;
	}

	public HashMap<String, String> getScreens () {
		return Screens;  
	}
}