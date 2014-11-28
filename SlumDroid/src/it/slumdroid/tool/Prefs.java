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

package it.slumdroid.tool;

import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.DRAG;
import static it.slumdroid.droidmodels.model.InteractionType.ENTER_TEXT;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_LONG_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.LIST_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.LONG_CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.RADIO_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.SET_BAR;
import static it.slumdroid.droidmodels.model.InteractionType.SPINNER_SELECT;
import static it.slumdroid.droidmodels.model.InteractionType.SWAP_TAB;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKBOX;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKTEXT;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.IMAGE_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.LINEAR_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO_GROUP;
import static it.slumdroid.droidmodels.model.SimpleType.RATING_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEEK_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SLIDING_DRAWER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER_INPUT;
import static it.slumdroid.droidmodels.model.SimpleType.TAB_HOST;
import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TOGGLE_BUTTON;
import static it.slumdroid.tool.Resources.EVENTS;
import static it.slumdroid.tool.Resources.EXTRA_EVENTS;
import static it.slumdroid.tool.Resources.EXTRA_INPUTS;
import static it.slumdroid.tool.Resources.INPUTS;
import it.slumdroid.tool.utilities.UserFactory;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import it.slumdroid.tool.utilities.interactors.editor.AdditionalEnterEditor;
import it.slumdroid.tool.utilities.interactors.editor.AdditionalWriteEditor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import android.annotation.SuppressLint;

public class Prefs {

	private static Preferences prefs;
	private static boolean notFound = false;
	private Preferences localPrefs;
	private Class<?> resources;
	private static String mainNode = Prefs.class.getPackage().getName();
	
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_EVENTS = new ArrayList<SimpleInteractorAdapter>();
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_INPUTS = new ArrayList<SimpleInteractorAdapter>();

	public Prefs (String node) {
		this.localPrefs = loadNode(node);
		this.resources = Resources.class;
	}	

	public static Preferences getMainNode () {
		if (notFound) return null;
		if (prefs == null) {
			loadMainNode();
		}
		return prefs;
	}

	public static void loadMainNode() {
		loadMainNode (mainNode);
	}

	@SuppressLint("SdCardPath")
	public static void loadMainNode (String node) {
		String path = "/data/data/" + mainNode + "/files/preferences.xml";
		if (!(new File(path).exists())) {
			notFound = true;
			return;
		}
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(path));
			Preferences.importPreferences(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		prefs = Preferences.userRoot().node(node);
	}

	public static Preferences loadNode (String localNode) {
		if (getMainNode() == null) return null;
		return getMainNode().node(localNode);
	}

	public boolean hasPrefs() {
		return this.localPrefs != null;
	}

	public void updateResources() {
		if (!hasPrefs()) return;
		for (Field f: this.resources.getFields()) {			
			if (Modifier.isFinal(f.getModifiers())) continue;
			try {
				updateValue (f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getInt (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getInt(parameter.getName(), parameter.getInt(parameter));
	}

	public long getLong (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getLong(parameter.getName(), parameter.getLong(parameter));
	}

	public boolean getBoolean (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getBoolean(parameter.getName(), parameter.getBoolean(parameter));
	}

	public String getString (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.get(parameter.getName(), parameter.get("").toString());
	}

	public static String fromArray (Field parameter, int index) {
		return parameter.getName() + "[" + index + "]";
	}

	public String[] getStringArray (Field parameter) throws IllegalArgumentException {
		List<String> theList = new ArrayList<String>();
		int index = 0;
		String value = new String();
		boolean found = false;
		while ((value = this.localPrefs.get(fromArray(parameter, index), null)) != null) {
			found = true;
			theList.add(value);
			index++;
		}
		String tmp[] = new String [theList.size()];
		return (found)?theList.toArray(tmp):null;
	}

	public int[] getIntArray (Field parameter) throws IllegalArgumentException {
		String[] value = getStringArray (parameter);
		if (value == null) return null;
		int[] ret = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			ret[i] = Integer.parseInt(value[i]);
		}
		return ret;
	}

	protected void setArray (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		setArray(parameter, parameter.getType());		
	}

	protected void setArray (Field parameter, Class<?> type) throws IllegalArgumentException, IllegalAccessException {
		Class<?> component = type.getComponentType();
		if (component.equals(String.class)) {
			String[] strings = getStringArray(parameter);
			if (strings != null) {
				parameter.set (parameter, strings);					
			}
		} else if (component.equals(int.class)) {
			int[] numbers = getIntArray(parameter);
			if (numbers != null) {
				parameter.set (parameter, numbers);					
			}
		}
	}

	protected void updateValue (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		Class<?> type = parameter.getType();
		if (type.equals(int.class)) {
			parameter.setInt (parameter, getInt (parameter));
		} else if (type.equals(long.class)) {
			parameter.setLong (parameter, getLong (parameter));
		} else if (type.equals(String.class)) {
			parameter.set (parameter, getString (parameter));
		} else if (type.equals(boolean.class)) {
			parameter.setBoolean (parameter, getBoolean (parameter));
		} else if (type.isArray()) {
			setArray (parameter, type);
		} else {
			return;
		}
	}

	public static void updateNode (String node) {
		new Prefs (node).updateResources();
	}
	
	public static void checkInputs(){
		if (INPUTS != null) {
			if (!inputs()) {
				UserFactory.addInput(CLICK, RADIO, CHECKBOX, CHECKTEXT, TOGGLE_BUTTON, NUMBER_PICKER_BUTTON);
			}
		} else {
			UserFactory.addInput(CLICK, RADIO, CHECKBOX, CHECKTEXT, TOGGLE_BUTTON, NUMBER_PICKER_BUTTON);
		}
		UserFactory.addInput(SPINNER_SELECT, SPINNER_INPUT);
		UserFactory.addInput(SET_BAR, SEEK_BAR, RATING_BAR);
		if (EXTRA_INPUTS != null) {
			ADDITIONAL_INPUTS.clear();
			for (String s: EXTRA_INPUTS) {
				String[] widgets = s.split(",");
				if (widgets[0].equals(WRITE_TEXT)){
					SimpleInteractorAdapter interactor = new AdditionalWriteEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
					ADDITIONAL_INPUTS.add(interactor);
				}
			}
		}
	}

	private static boolean inputs() {
		boolean isClick = false;
		for (String s: INPUTS) {		
			String[] widgets = s.split("( )?,( )?");
			if (widgets[0].equals(CLICK)) isClick = true;
			UserFactory.addInput(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));		
		}
		return isClick;
	}
	
	public static void checkEvents(){
		if (EVENTS != null) {
			if (!events()) {
				UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW, TEXT_VIEW, LINEAR_LAYOUT);
			}
		} else {
			UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW, TEXT_VIEW, LINEAR_LAYOUT);
		}
		UserFactory.addEvent(LONG_CLICK, IMAGE_VIEW);
		UserFactory.addEvent(ENTER_TEXT, SEARCH_BAR);
		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, PREFERENCE_LIST, EXPAND_MENU);
		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW);
		UserFactory.addEvent(RADIO_SELECT, RADIO_GROUP); 
		UserFactory.addEvent(SPINNER_SELECT, SPINNER);
		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
		UserFactory.addEvent(DRAG, SLIDING_DRAWER);
		if (EXTRA_EVENTS != null) {
			ADDITIONAL_EVENTS.clear();
			for (String s: EXTRA_EVENTS) {
				String[] widgets = s.split(",");
				if (widgets[0].equals(WRITE_TEXT)){
					SimpleInteractorAdapter interactor = new AdditionalWriteEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
					ADDITIONAL_EVENTS.add(interactor);
				} else {
					if (widgets[0].equals(ENTER_TEXT)){
						SimpleInteractorAdapter interactor = new AdditionalEnterEditor().addIdValuePair(widgets[1], Arrays.copyOfRange(widgets, 2, widgets.length));
						ADDITIONAL_EVENTS.add(interactor);
					}
				}	
			}
		}
	}

	private static boolean events() {
		boolean isClick = false;
		for (String s: EVENTS) {
			String[] widgets = s.split("( )?,( )?");
			if (widgets[0].equals(CLICK)) isClick = true;
			UserFactory.addEvent(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));
		}
		return isClick;
	}

}
