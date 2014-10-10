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

package it.slumdroid.wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandLine {

	private static Map<String, String> dosCommands = new HashMap<String, String>();
	private static Map<String, String> config = new HashMap<String, String>();
	private static Map<String, String> commandMap = dosCommands;

	public static void setJavaPath (String path) {
		config.put(JAVA_PATH, path);
	}

	public static void setAndroidPath (String path) {
		config.put(ANDROID_PATH, path);
	}

	public static void setResultsPath (String path) {
		config.put(RESULTS_PATH, path);
	}

	public static void setAppPath (String path) {
		config.put(APP_PATH, path);
	}

	public static void setDos () {
		commandMap = dosCommands;
	}

	public static String get (String command, String ... args) {
		String pattern = commandMap.get(command);
		for (Entry<String, String> e: config.entrySet()) {
			String find = arg(e.getKey());
			pattern = pattern.replace(find, e.getValue());
		}

		String key = null;
		for (String s: args) {
			if (key==null) {
				key = arg(s);
			} else {
				pattern = pattern.replace(key, s);
				key=null;
			}
		}

		return pattern;
	}

	public static String arg (String a) {
		return "[[" + a + "]]";
	}

	public static String path (String p) {
		return "\"" + arg (p) + "\"";
	}

	public static String path (String p1, String p2) {
		return "\"" + arg (p1) + p2 + "\"";
	}

	// Config strings
	public final static String ANDROID_PATH = "androidPath";
	public final static String RESULTS_PATH = "experimentPath";
	public final static String APP_PATH = "appPath";
	public final static String JAVA_PATH = "javaPath";

	// Argument strings
	public final static String DEVICE = "device";
	public final static String CLASS = "class";
	public final static String PACKAGE = "package";
	public final static String CYCLES = "randomCycles";

	// Command strings
	public final static String DUMP_APK = "dump apk";
	public final static String LOAD_AVD = "load avd";
	public final static String DEPLOY = "deploy";
	public final static String RANDOM_TEST = "test random";
	public final static String SYSTEMATIC_TEST = "test systematic";
	public final static String POST_PROCESS = "postproc";
	public final static String SDK_CHECK = "sdk manager";
	public final static String CLOSE = "close";
	public final static String JAVA_VERSION = "java version";
	public final static String FIRST_BOOT = "first boot";
	public final static String FIRST_BOOT_RANDOM = "first boot_random";

	// DOS commands
	static String place = System.getProperty("user.dir");
	static {
		dosCommands.put(SDK_CHECK, arg(ANDROID_PATH) + "\\SDK Manager.exe");
		dosCommands.put(JAVA_VERSION, path(JAVA_PATH, "\\bin\\java") + " -version");
		dosCommands.put(DUMP_APK, "aapt dump badging " + path(APP_PATH));
		dosCommands.put(LOAD_AVD, path(ANDROID_PATH, "\\tools\\android.bat") + " list avd");

		dosCommands.put(FIRST_BOOT, place + "\\batch\\FirstBoot.bat " + arg(DEVICE) + " " + path(RESULTS_PATH) + " 0" + arg(PACKAGE) + " " + arg(CLASS) );
		dosCommands.put(FIRST_BOOT_RANDOM, place + "\\batch\\FirstBoot.bat " + arg(DEVICE) + " " + path(RESULTS_PATH) + " 1" + arg(PACKAGE) + " " + arg(CLASS));
		
		dosCommands.put(DEPLOY, place + "\\batch\\Installer.bat " + arg(DEVICE) + " " + path(APP_PATH) + " " + arg(PACKAGE) + " " + arg(CLASS) + " " + path(RESULTS_PATH));

		dosCommands.put(RANDOM_TEST, place + "\\batch\\Random.bat " + arg(DEVICE) + " " + arg(PACKAGE) + " " + path(RESULTS_PATH) + " 10 " + arg(CYCLES));
		dosCommands.put(SYSTEMATIC_TEST, place + "\\batch\\Ripper.bat " + arg(DEVICE) + " " + arg(PACKAGE) + " " + path(RESULTS_PATH) + " 10");

		dosCommands.put(POST_PROCESS, place + "\\batch\\PostProcess.bat " + path(RESULTS_PATH) + " " + path(APP_PATH) + " " + arg(PACKAGE));
		dosCommands.put(CLOSE, place + "\\batch\\close.bat");
	}

}
