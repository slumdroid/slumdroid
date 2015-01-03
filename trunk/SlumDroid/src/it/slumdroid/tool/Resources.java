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

public class Resources {

	// Support Variables
	public final static String TAG = "slumdroid";	
	public static enum SchedulerAlgorithm {
		BREADTH_FIRST, DEPTH_FIRST, RANDOM
	}

	// Main Parameters
	public static String PACKAGE_NAME = "app.package";
	public static String CLASS_NAME = "app.package.class";

	public static long RANDOM_SEED = System.currentTimeMillis(); 
	public static boolean SCREENSHOT_ENABLED = true; // true -> perform an image capture of the screen after processing a task

	public static String SCHEDULER_ALGORITHM = "BREADTH_FIRST";

	// Automation Parameters
	public static int SLEEP_AFTER_EVENT = 1000;
	public static int SLEEP_AFTER_RESTART = 0;
	public static int SLEEP_AFTER_TASK = 0;
	public static int SLEEP_ON_THROBBER = 1000; // How long to wait on spinning wheels (in ms -- 0 = don't wait)

	// Comparator Parameters
	public static boolean COMPARE_LIST_COUNT = false;
	public static boolean COMPARE_CHECKBOX = false;
	public static boolean COMPARE_AVAILABLE = false;

	// Interactions Parameters
	public static String EVENTS[];
	public static String INPUTS[];

	public static boolean HASH_VALUES = true; // true -> use Hash Id to generate the text values

	public static String EXTRA_EVENTS[];
	public static String EXTRA_INPUTS[];

	public static int MAX_NUM_EVENTS_PER_SELECTOR = 3; // For ListView, Spinner and RadioGroup (0 = try all items in the list)
	public static boolean TAB_EVENTS_START_ONLY = false; // true -> click on tabs only on the start activity

	public static Class<?> theClass;

	private static void update () {	
		Prefs.updateNode(""); 				// Main Node
		Prefs.updateNode("automation");		// Automation Node
		Prefs.updateNode("comparator");		// Comparator Node
		Prefs.updateNode("interactions");	// Interactions Node
		Prefs.checkEvents();		
		Prefs.checkInputs();
	}

	static {
		update();
		try {
			theClass = Class.forName(CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}			
	}

}

// Log.d("nofatclips","Sono qui");
// Log.d("nofatclips","Sono qua");
// Log.d("nofatclips","Sono quo");
