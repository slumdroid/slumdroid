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

package it.slumdroid.tool;

import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
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
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO_GROUP;
import static it.slumdroid.droidmodels.model.SimpleType.RATING_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEEK_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER_INPUT;
import static it.slumdroid.droidmodels.model.SimpleType.TAB_HOST;
import static it.slumdroid.droidmodels.model.SimpleType.TOGGLE_BUTTON;
import static it.slumdroid.tool.Resources.EVENTS;
import static it.slumdroid.tool.Resources.EXTRA_EVENTS;
import static it.slumdroid.tool.Resources.EXTRA_INPUTS;
import static it.slumdroid.tool.Resources.INPUTS;
import static it.slumdroid.tool.Resources.TAG;
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
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class Prefs.
 */
public class Prefs {

	/** The prefs. */
	private static Preferences prefs;
	
	/** The not found. */
	private static boolean notFound = false;
	
	/** The local prefs. */
	private Preferences localPrefs;
	
	/** The resources. */
	private Class<?> resources;
	
	/** The main node. */
	private static String mainNode = Prefs.class.getPackage().getName();

	/** The additional events. */
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_EVENTS = new ArrayList<SimpleInteractorAdapter>();
	
	/** The additional inputs. */
	public static ArrayList<SimpleInteractorAdapter> ADDITIONAL_INPUTS = new ArrayList<SimpleInteractorAdapter>();

	/**
	 * Instantiates a new prefs.
	 *
	 * @param node the node
	 */
	public Prefs (String node) {
		this.localPrefs = loadNode(node);
		this.resources = Resources.class;
	}	

	/**
	 * Gets the main node.
	 *
	 * @return the main node
	 */
	public static Preferences getMainNode () {
		if (notFound) return null;
		if (prefs == null) {
			loadMainNode();
		}
		return prefs;
	}

	/**
	 * Load main node.
	 */
	public static void loadMainNode() {
		loadMainNode (mainNode);
	}

	/**
	 * Load main node.
	 *
	 * @param node the node
	 */
	@SuppressLint("SdCardPath")
	public static void loadMainNode (String node) {
		String path = "/data/data/" + mainNode + "/files/preferences.xml";
		if (!(new File(path).exists())) {
			Log.i(TAG, "Preferences file not found.");
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

	/**
	 * Load node.
	 *
	 * @param localNode the local node
	 * @return the preferences
	 */
	public static Preferences loadNode (String localNode) {
		if (getMainNode() == null) return null;
		return getMainNode().node(localNode);
	}

	/**
	 * Checks for prefs.
	 *
	 * @return true, if successful
	 */
	public boolean hasPrefs() {
		return this.localPrefs != null;
	}

	/**
	 * Update resources.
	 */
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

	/**
	 * Gets the int.
	 *
	 * @param parameter the parameter
	 * @return the int
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public int getInt (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getInt(parameter.getName(), parameter.getInt(parameter));
	}

	/**
	 * Gets the long.
	 *
	 * @param parameter the parameter
	 * @return the long
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public long getLong (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getLong(parameter.getName(), parameter.getLong(parameter));
	}

	/**
	 * Gets the boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public boolean getBoolean (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.getBoolean(parameter.getName(), parameter.getBoolean(parameter));
	}

	/**
	 * Gets the string.
	 *
	 * @param parameter the parameter
	 * @return the string
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public String getString (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		return this.localPrefs.get(parameter.getName(), parameter.get("").toString());
	}

	/**
	 * From array.
	 *
	 * @param parameter the parameter
	 * @param index the index
	 * @return the string
	 */
	public static String fromArray (Field parameter, int index) {
		return parameter.getName() + "[" + index + "]";
	}

	/**
	 * Gets the string array.
	 *
	 * @param parameter the parameter
	 * @return the string array
	 * @throws IllegalArgumentException the illegal argument exception
	 */
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

	/**
	 * Gets the int array.
	 *
	 * @param parameter the parameter
	 * @return the int array
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public int[] getIntArray (Field parameter) throws IllegalArgumentException {
		String[] value = getStringArray (parameter);
		if (value == null) return null;
		int[] ret = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			ret[i] = Integer.parseInt(value[i]);
		}
		return ret;
	}

	/**
	 * Sets the array.
	 *
	 * @param parameter the new array
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	protected void setArray (Field parameter) throws IllegalArgumentException, IllegalAccessException {
		setArray(parameter, parameter.getType());		
	}

	/**
	 * Sets the array.
	 *
	 * @param parameter the parameter
	 * @param type the type
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
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

	/**
	 * Update value.
	 *
	 * @param parameter the parameter
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
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

	/**
	 * Update node.
	 *
	 * @param node the node
	 */
	public static void updateNode (String node) {
		new Prefs (node).updateResources();
	}

	/**
	 * Check inputs.
	 */
	public static void checkInputs(){
		if (INPUTS != null) {
			if (!hasClickAsInputs()) {
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

	/**
	 * Checks for click as inputs.
	 *
	 * @return true, if successful
	 */
	private static boolean hasClickAsInputs() {
		boolean isClick = false;
		for (String s: INPUTS) {		
			String[] widgets = s.split("( )?,( )?");
			if (widgets[0].equals(CLICK)) isClick = true;
			UserFactory.addInput(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));		
		}
		return isClick;
	}

	/**
	 * Check events.
	 */
	public static void checkEvents(){
		if (EVENTS != null) {
			if (!hasClickAsEvents()) {
				UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW);
			}
		} else {
			UserFactory.addEvent(CLICK, BUTTON, MENU_ITEM, IMAGE_VIEW);
		}
		UserFactory.addEvent(LONG_CLICK, IMAGE_VIEW);
		UserFactory.addEvent(ENTER_TEXT, SEARCH_BAR);
		UserFactory.addEvent(LIST_SELECT, LIST_VIEW, PREFERENCE_LIST, EXPAND_MENU);
		UserFactory.addEvent(LIST_LONG_SELECT, LIST_VIEW);
		UserFactory.addEvent(RADIO_SELECT, RADIO_GROUP); 
		UserFactory.addEvent(SPINNER_SELECT, SPINNER);
		UserFactory.addEvent(SWAP_TAB, TAB_HOST);
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

	/**
	 * Checks for click as events.
	 *
	 * @return true, if successful
	 */
	private static boolean hasClickAsEvents() {
		boolean isClick = false;
		for (String s: EVENTS) {
			String[] widgets = s.split("( )?,( )?");
			if (widgets[0].equals(CLICK)) isClick = true;
			UserFactory.addEvent(widgets[0], Arrays.copyOfRange(widgets, 1, widgets.length));
		}
		return isClick;
	}

}
