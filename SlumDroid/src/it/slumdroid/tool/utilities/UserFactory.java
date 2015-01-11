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

package it.slumdroid.tool.utilities;

import static it.slumdroid.droidmodels.model.InteractionType.CLICK;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.tool.Prefs.ADDITIONAL_EVENTS;
import static it.slumdroid.tool.Prefs.ADDITIONAL_INPUTS;
import static it.slumdroid.tool.Resources.HASH_VALUES;
import static it.slumdroid.tool.Resources.RANDOM_SEED;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.UserAdapter;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import it.slumdroid.tool.utilities.adapters.SimpleUserAdapter;
import it.slumdroid.tool.utilities.interactors.BarSlider;
import it.slumdroid.tool.utilities.interactors.Clicker;
import it.slumdroid.tool.utilities.interactors.LongClicker;
import it.slumdroid.tool.utilities.interactors.TabSwapper;
import it.slumdroid.tool.utilities.interactors.editor.HashEnterEditor;
import it.slumdroid.tool.utilities.interactors.editor.HashWriteEditor;
import it.slumdroid.tool.utilities.interactors.editor.RandomEnterEditor;
import it.slumdroid.tool.utilities.interactors.editor.RandomWriteEditor;
import it.slumdroid.tool.utilities.interactors.selector.ListLongSelector;
import it.slumdroid.tool.utilities.interactors.selector.ListSelector;
import it.slumdroid.tool.utilities.interactors.selector.RadioSelector;
import it.slumdroid.tool.utilities.interactors.selector.RandomSpinnerSelector;
import it.slumdroid.tool.utilities.interactors.selector.SpinnerSelector;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating User objects.
 */
public class UserFactory {

	/** The event to type map. */
	public static HashMap<String,String[]> eventToTypeMap = new HashMap<String,String[]>();

	/** The input to type map. */
	public static HashMap<String,String[]> inputToTypeMap = new HashMap<String,String[]>();

	/** The vetoes map. */
	public static Map<String,List<String>> vetoesMap = new Hashtable<String,List<String>>();

	/** The overrides map. */
	public static Map<String,List<String>> overridesMap = new Hashtable<String,List<String>>();

	/**
	 * Adds the event.
	 *
	 * @param eventType the event type
	 * @param widgetTypes the widget types
	 */
	public static void addEvent(String eventType, String ... widgetTypes) {
		eventToTypeMap.put(eventType, widgetTypes);
	}

	/**
	 * Adds the input.
	 *
	 * @param inputType the input type
	 * @param widgetTypes the widget types
	 */
	public static void addInput(String inputType, String ... widgetTypes) {
		inputToTypeMap.put(inputType, widgetTypes);
	}

	/**
	 * Types for event.
	 *
	 * @param interaction the interaction
	 * @return the string[]
	 */
	public static String[] typesForEvent(String interaction) {
		return eventToTypeMap.get(interaction);
	}

	/**
	 * Types for input.
	 *
	 * @param interaction the interaction
	 * @return the string[]
	 */
	public static String[] typesForInput(String interaction) {
		return inputToTypeMap.get(interaction);
	}

	/**
	 * Checks if is required event.
	 *
	 * @param interaction the interaction
	 * @return true, if is required event
	 */
	public static boolean isRequiredEvent(String interaction) {
		return UserFactory.eventToTypeMap.containsKey(interaction);
	}

	/**
	 * Checks if is required input.
	 *
	 * @param interaction the interaction
	 * @return true, if is required input
	 */
	public static boolean isRequiredInput(String interaction) {
		return UserFactory.inputToTypeMap.containsKey(interaction);
	}

	/**
	 * Gets the user.
	 *
	 * @param abstractor the abstractor
	 * @return the user
	 */	
	public static UserAdapter getUser (Abstractor abstractor) {
		UserAdapter userAdapter = new SimpleUserAdapter(abstractor, new Random(RANDOM_SEED));
		userAdapter.addEvent(new Clicker(typesForEvent(CLICK)));
		userAdapter.addEvent(new LongClicker());
		if (isRequiredEvent(WRITE_TEXT)) {
			if(HASH_VALUES) {
				userAdapter.addEvent(new HashWriteEditor(typesForEvent(WRITE_TEXT)));
			} else {
				userAdapter.addEvent(new RandomWriteEditor(typesForEvent(WRITE_TEXT)));
			}
		}
		if(HASH_VALUES)	{
			userAdapter.addEvent(new HashEnterEditor());
		} else {
			userAdapter.addEvent(new RandomEnterEditor());
		}
		userAdapter.addEvent(new ListSelector());
		userAdapter.addEvent(new ListLongSelector());
		userAdapter.addEvent(new SpinnerSelector());
		userAdapter.addEvent(new RadioSelector());
		userAdapter.addEvent(new TabSwapper());
		for (SimpleInteractorAdapter interactor: ADDITIONAL_EVENTS) {
			userAdapter.addEvent(interactor);			
		}
		userAdapter.addInput(new Clicker (typesForInput(CLICK)));	
		if (isRequiredInput(WRITE_TEXT)) {
			if (HASH_VALUES) {
				userAdapter.addInput(new HashWriteEditor(typesForInput(WRITE_TEXT)));
			} else {
				userAdapter.addInput(new RandomWriteEditor(typesForInput(WRITE_TEXT)));
			}
		}
		userAdapter.addInput(new RandomSpinnerSelector());
		userAdapter.addInput(new BarSlider());
		for (SimpleInteractorAdapter interactor: ADDITIONAL_INPUTS) {
			userAdapter.addInput(interactor);
		}
		return userAdapter;
	}	

}