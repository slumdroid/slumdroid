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

package it.slumdroid.wizard.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandLine {

	private static Map<String, String> dosCommands = new HashMap<String, String>();
	private static Map<String, String> config = new HashMap<String, String>();
	private static Map<String, String> commandMap = dosCommands;

	public static void setResultsPath (String path) {
		config.put(RESULTS_PATH, path);
	}

	public static void setAutPath (String path) {
		config.put(AUT_PATH, path);
	}

	public static void setAutPackage (String path) {
		config.put(AUT_PACKAGE, path);
	}

	public static void setAutClass (String path) {
		config.put(AUT_CLASS, path);
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
			if (key == null) {
				key = arg(s);
			} else {
				pattern = pattern.replace(key, s);
				key = null;
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

	public final static String ANDROID_PATH = System.getenv("ANDROID_HOME");
	public final static String CLOSE = "close";

	private final static String RESULTS_PATH = "experimentPath";

	public final static  String AUT_PATH = "autSorceCode";
	private final static String AUT_CLASS = "autClass";
	private final static String AUT_PACKAGE = "autPackage";

	public final static  String DUMP_APK = "dumpApk";
	private static final String APK_NAME = "apkName";

	public final static String DEFINE = "define";
	public final static String DEPLOY = "deploy";
	public final static String RIPPING_PROCESS = "rippingProcess";
	public final static String POST_PROCESS = "postProcess";

	// DOS commands
	static String place = System.getProperty("user.dir");
	static {
		/*
		 *  External parameters:
		 *  %1 = A.U.T. Source Path
		 *  %2 = A.U.T. Package
		 *  %3 = A.U.T. Class
		 *  %4 = A.U.T. Apk Name
		 *  %5 = Output Results Path
		 */
		String parameters = path(AUT_PATH) + " " + arg(AUT_PACKAGE) + " " + 
				arg(AUT_CLASS) + " " + arg(APK_NAME) +" " + path(RESULTS_PATH);

		dosCommands.put(DUMP_APK, "aapt dump badging " + path(AUT_PATH));
		dosCommands.put(DEFINE, place + "\\batch\\Define.bat " + parameters);
		dosCommands.put(DEPLOY, place + "\\batch\\Installer.bat " + parameters);
		dosCommands.put(RIPPING_PROCESS, place + "\\batch\\Ripper.bat " + parameters);
		dosCommands.put(POST_PROCESS, place + "\\batch\\PostProcess.bat " + parameters);
		dosCommands.put(CLOSE, place + "\\batch\\close.bat");
	}

}