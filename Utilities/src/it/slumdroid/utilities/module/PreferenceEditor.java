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

package it.slumdroid.utilities.module;

import static it.slumdroid.utilities.Resources.TOOL;

import java.util.prefs.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class PreferenceEditor.
 */
public class PreferenceEditor {

	/** The package. */
	private final String PACKAGE = "PACKAGE_NAME";

	/** The class. */
	private final String CLASS = "CLASS_NAME";

	/** The seed. */
	private final String SEED = "RANDOM_SEED";

	/**
	 * Preference editor.
	 *
	 * @param args the args
	 */
	public void preferenceEditor(String[] args) {
		Preferences prefs = Preferences.userRoot().node(TOOL);
		new Tools().cleanNode (prefs);
		prefs = Preferences.userRoot().node(TOOL);
		String keyWord = args[1]; 
		String preferencesPath = args[2];
		if (new Tools().loadNode (preferencesPath)) {
			if (keyWord.equals("retarget")) {
				prefs.put (PACKAGE, args[3]);
				prefs.put (CLASS, args[4]);
			}
			if (keyWord.equals("randomize")) {
				prefs.putLong(SEED, new Tools().newSeed(args[3]));
			} 
			if (keyWord.equals("update")) {
				prefs.put(args[3], args[4]);			
			}
			new Tools().saveNode (preferencesPath, prefs);
		}	
	}

}
