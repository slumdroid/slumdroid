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

package it.slumdroid.wizard.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandLine {

	private static Map<String, String> dosCommands = new HashMap<String, String>();
	private static Map<String, String> config = new HashMap<String, String>();
	private static Map<String, String> commandMap = dosCommands;
	
	public static void setDevice (String value) {
		config.put(DEVICE, value);
	}

	public static void setAutPath (String value) {
		config.put(AUT_PATH, value);
	}
	
	public static void setAutPackage (String value) {
		config.put(AUT_PACKAGE, value);
	}

	public static void setAutClass (String value) {
		config.put(AUT_CLASS, value);
	}
	
	public static void setResultsPath (String value) {
		config.put(RESULTS_PATH, value);
	}
	
	public static void setApkName (String path) {
		config.put(APK_NAME, path);
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

	public static String arg (String argument) {
		return "[[" + argument + "]]";
	}

	public static String path (String oath) {
		return "\"" + arg (oath) + "\"";
	}

	public static String path (String value1, String value2) {
		return "\"" + arg (value1) + value2 + "\"";
	}

	public final static String ANDROID_PATH = System.getenv("ANDROID_HOME");
	public final static String LOAD_AVD = "load avd";
	public final static String DEVICE = "device";
	
	public final static String RESULTS_PATH = "experimentPath";
	
	public final static String AUT_PATH = "appPath";
	public final static String AUT_PACKAGE = "package";
	public final static String AUT_CLASS = "class";
	public final static String APK_NAME = "apkName";
	public final static String DUMP_APK = "dump apk";
	
	public final static String DEFINE = "define";
	public final static String DEPLOY = "deploy";
	public final static String TEST = "test";
	public final static String POST_PROCESS = "postproc";
	
	public final static String CLOSE = "close";
	
	// DOS commands
	static String place = System.getProperty("user.dir");
	static {
		String parameters = arg(DEVICE) + " " + path(AUT_PATH) + " " + arg(AUT_PACKAGE) + " " + arg(AUT_CLASS) + " " + path(RESULTS_PATH) + " " + arg(APK_NAME);
		
		dosCommands.put(DUMP_APK, "aapt dump badging " + path(AUT_PATH));
		dosCommands.put(LOAD_AVD, (System.getenv("ANDROID_HOME") + "\\tools\\android.bat list avd"));
		
		dosCommands.put(DEFINE, place + "\\batch\\FirstBoot.bat " + parameters);
		dosCommands.put(DEPLOY, place + "\\batch\\Installer.bat " + parameters);
		dosCommands.put(TEST, place + "\\batch\\Ripper.bat " + parameters);
		dosCommands.put(POST_PROCESS, place + "\\batch\\PostProcess.bat " + parameters);
		
		dosCommands.put(CLOSE, place + "\\batch\\close.bat");
	}

}
