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

package it.slumdroid.guianalyzer.perturbation;

import it.slumdroid.droidmodels.model.WidgetState;

import it.slumdroid.guianalyzer.model.WidgetPerturbations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerturbationHandler {

	private Collection<WidgetState> WidgetsColl;
	private HashMap<String, List<WidgetState>> WidgetsPerturbated = new HashMap<String, List<WidgetState>>();
	private HashMap<String, List<String>> WidgetsPerturbations = new HashMap<String, List<String>>();
	private HashMap<String, String> ManualPerturbations = new HashMap<String, String>();

	public PerturbationHandler (HashMap<String, WidgetState> input) {
		WidgetsColl = input.values();
	}

	public void perturb () {
		WidgetPerturbations p;

		for (WidgetState widget: WidgetsColl) {
			if (ManualPerturbations.containsKey(widget.getId())) {
				p = new Perturbations(widget, true);
				String action = ManualPerturbations.get(widget.getId());

				try {
					if (action.contains("file:")) {
						Method m = Perturbations.class.getMethod("dictionary", String.class);
						m.invoke(p, action.substring(5));
					} else if (action.contains("manual:")) {
						Method m = Perturbations.class.getMethod("manual", String.class);
						m.invoke(p, action.substring(7));
					} else if (action.contains("regex:")) {
						Method m = Perturbations.class.getMethod("manualRegEx", String.class);
						m.invoke(p, action.substring(6));
					} else {
						Method m = Perturbations.class.getDeclaredMethod(action);
						m.invoke(p);
					}

					p.addPerturbation(action);
				} catch (NoSuchMethodException ex) {
					Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				p = new Perturbations(widget, false);

				/**
				 * Calling the perturbations... There must be at least one
				 * perturbation
				 */
				int numPerturbations = 0;
				for (String method: PerturbationHandler.listPerturbations()) {
					if (!method.isEmpty()
							&& !method.equals("dictionary")
							&& !method.equals("standard")
							&& !method.equals("manual")
							&& !method.equals("manualRegEx")
							&& !method.equals("exclude")) {
						try {
							Method m = Perturbations.class.getDeclaredMethod(method);

							if ((Boolean) m.invoke(p)) {
								p.addPerturbation(method);
								numPerturbations++;
							}
						} catch (NoSuchMethodException ex) {
							Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
						} catch (SecurityException ex) {
							Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
						} catch (IllegalAccessException ex) {
							Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
						} catch (IllegalArgumentException ex) {
							Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
						} catch (InvocationTargetException ex) {
							Logger.getLogger(PerturbationHandler.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				}

				if (numPerturbations == 0) {
					p.addPerturbation("standard");
					p.standard();
				}
			}

			WidgetsPerturbated.put(widget.getId(), p.getList());
			WidgetsPerturbations.put(widget.getId(), p.getPerturbations());
		}
	}

	public HashMap<String, List<WidgetState>> getWidgetsPerturbated () {
		return WidgetsPerturbated;
	}

	public void addManualPerturbation (String widgetId, String action) {
		ManualPerturbations.put(widgetId, action);
	}

	public void removeManualPerturbation (String widgetId) {
		ManualPerturbations.remove(widgetId);
	}

	public static String[] listPerturbations () {
		Method[] m = Perturbations.class.getDeclaredMethods();
		List<String> list = new ArrayList<String>();
		list.add(""); // auto perturbation

		for (int i = 0; i < m.length; i++) {
			String name = m[i].getName().toString();

			if (m[i].getModifiers() == 1
					&& !name.equals("getList")
					&& !name.equals("getPerturbations")
					&& !name.equals("addPerturbation")) {
				list.add(name);
			}
		}

		return list.toArray(new String[list.size()]);
	}

	public HashMap<String, List<String>> getWidgetsPerturbations () {
		return WidgetsPerturbations;
	}
}