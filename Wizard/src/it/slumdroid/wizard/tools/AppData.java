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

import static it.slumdroid.wizard.tools.CommandLine.APP_PATH;
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

	public AppData (String c, String p) {
		if (c.contains(p)){
			String without = c.replace(p, "");
			setClassName(p.concat(without).replace("..", "."));
		}else{
			setClassName(c);
		}
		setPackage(p);
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
		String c = new String();
		String p = new String();
		String command = CommandLine.get (DUMP_APK, APP_PATH, apkPath);
		try {
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s = new String();
			while ((s = stdInput.readLine()) != null) {
				if(s.contains("package: name=")) {
					String s1=s.substring(15);
					p = s1.substring(0, s1.indexOf("'"));
				}

				if(s.contains("launchable-activity: name=")) {
					String s1=s.substring(27);
					c = s1.substring(0, s1.indexOf("'"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error generating apk description");
		}
		return new AppData (c, p);
	}

	public static AppData getFromSource(String sourcePath) {
		String path = sourcePath + File.separator + "AndroidManifest.xml";
		SearchableManifest doc = new SearchableManifest (path);
		String thePackage = doc.parseXpath(MANIFEST_XPATH);
		String theClass = doc.parseXpath(CLASS_XPATH);
		return new AppData (getClassFullName (theClass, thePackage), thePackage);
	}

	public static String getClassFullName(String c, String p) {
		if (c.equals("")) return "";
		String dot = (p.endsWith(".") || c.startsWith("."))?"":".";
		return p + dot + c;
	}
	
}