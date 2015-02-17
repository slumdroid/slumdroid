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

// TODO: Auto-generated Javadoc
/**
 * The Class Resources.
 */
public class Resources {

	/** The Constant TAG. */
	public static final String TAG = "slumdroid";	

	/** The activity list file name. */
	public static final String ACTIVITY_LIST_FILE_NAME = "activities.xml"; 

	/** The file name. */
	public static final String GUI_TREE_FILE_NAME = "guitree.xml"; 

	/** The parameters file name. */
	public static final String PARAMETERS_FILE_NAME = "parameters.obj"; 

	/** The task list file name. */
	public static final String TASK_LIST_FILE_NAME = "tasklist.xml";

	/**
	 * The Enum SchedulerAlgorithm.
	 */
	public static enum SchedulerAlgorithm {

		/** The breadth first. */
		BREADTH_FIRST, 
		/** The depth first. */
		DEPTH_FIRST, 
		/** The random first. */
		RANDOM_FIRST

	}

	/** The package name. */
	public static String PACKAGE_NAME = "app.package";

	/** The class name. */
	public static String CLASS_NAME = "app.package.class";

	/** The random seed. */
	public static long RANDOM_SEED = System.currentTimeMillis(); 

	/** The screenshot enabled. */
	/** true -> perform an image capture of the screen after processing a task */
	public static boolean SCREENSHOT_ENABLED = true; 

	/** The scheduler algorithm. */
	public static String SCHEDULER_ALGORITHM = "BREADTH_FIRST";

	/** The sleep after event. */
	public static int SLEEP_AFTER_EVENT = 1000;

	/** The sleep after restart. */
	public static int SLEEP_AFTER_RESTART = 0;

	/** The sleep after task. */
	public static int SLEEP_AFTER_TASK = 0;

	/** The sleep on throbber. */
	/**  How long to wait on spinning wheels (in ms -- 0 = don't wait) */
	public static int SLEEP_ON_THROBBER = 1000; 

	/** The compare available. */
	public static boolean COMPARE_AVAILABLE = false;

	/** The compare checkbox. */
	public static boolean COMPARE_CHECKBOX = false;

	/** The compare list count. */
	public static boolean COMPARE_LIST_COUNT = false;

	/** The compare title. */
	public static boolean COMPARE_TITLE = true;

	/** The compare toast. */
	public static boolean COMPARE_TOAST = false;

	/** The events. */
	public static String EVENTS[];

	/** The inputs. */
	public static String INPUTS[];

	/** The hash values. */
	/** true -> use Hash Id to generate the text values */
	public static boolean HASH_VALUES = true; 

	/** The extra events. */
	public static String EXTRA_EVENTS[];

	/** The extra inputs. */
	public static String EXTRA_INPUTS[];

	/** The max num events per selector. */
	/** For ListView, Spinner and RadioGroup (0 = try all items in the list) */
	public static int MAX_NUM_EVENTS_PER_SELECTOR = 3; 

	/** The tab events start only. */
	/** true -> click on tabs only on the start activity */
	public static boolean TAB_EVENTS_START_ONLY = false; 

	static {
		Prefs.updateNodes();			
	}

}

// Log.d("nofatclips","Sono qui");
// Log.d("nofatclips","Sono qua");
// Log.d("nofatclips","Sono quo");
