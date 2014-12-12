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

import static it.slumdroid.wizard.tools.CommandLine.AUT_PATH;
import static it.slumdroid.wizard.tools.CommandLine.DUMP_APK;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class AppData {
	private String theClass;
	private String thePackage;

	public final static String MANIFEST_XPATH = "//manifest[1]/@package";
	public final static String CLASS_XPATH = "//activity[intent-filter/action/@name='android.intent.action.MAIN'][1]/@name";

	public AppData () {}

	public AppData (String theClass, String thePackage) {
		if (theClass.contains(thePackage)){
			String without = theClass.replace(thePackage, "");
			setClassName(thePackage.concat(without).replace("..", "."));
		}else{
			setClassName(theClass);
		}
		setPackage(thePackage);
	}

	public String getClassName() {
		return this.theClass;
	}

	public void setClassName(String theClass) {
		this.theClass = theClass;
	}

	public String getPackage() {
		return thePackage;
	}

	public void setPackage(String thePackage) {
		this.thePackage = thePackage;
	}

	public static AppData getFromApk (String apkPath) {
		String theClass = new String();
		String thePackage = new String();
		String command = CommandLine.get (DUMP_APK, AUT_PATH, apkPath);
		try {
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s = new String();
			while ((s = stdInput.readLine()) != null) {
				if(s.contains("package: name=")) {
					String s1=s.substring(15);
					thePackage = s1.substring(0, s1.indexOf("'"));
				}

				if(s.contains("launchable-activity: name=")) {
					String s1 = s.substring(27);
					theClass = s1.substring(0, s1.indexOf("'"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error generating apk description");
		}
		return new AppData (theClass, thePackage);
	}

	public static AppData getFromSource(String sourcePath) {
		String path = sourcePath + File.separator + "AndroidManifest.xml";
		SearchableManifest doc = new SearchableManifest (path);
		String thePackage = doc.parseXpath(MANIFEST_XPATH);
		String theClass = doc.parseXpath(CLASS_XPATH);
		return new AppData (getClassFullName (theClass, thePackage), thePackage);
	}

	public static String getClassFullName(String theClass, String thePackage) {
		if (theClass.equals("")) return "";
		String dot = (thePackage.endsWith(".") || theClass.startsWith("."))?"":".";
		return thePackage + dot + theClass;
	}

}