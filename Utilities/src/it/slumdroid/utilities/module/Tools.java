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

package it.slumdroid.utilities.module;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.prefs.Preferences;

public class Tools {

	static final int maxPerEs = 100;
	static String metadata = new String();

	public void cleanNode (Preferences prefs) {
		try {
			prefs.removeNode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean loadNode (String path) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(path));
			Preferences.importPreferences(is);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void saveNode(String args, Preferences prefs) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(args);
			prefs.exportSubtree(fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long newSeed (String seed) {
		Random Rs = new Random(Long.parseLong(seed));
		Long generatedSeed = Rs.nextLong();
		return generatedSeed;
	}

	public String coverage(PrintWriter out){
		int esCounter = 0;
		String[] theFiles = showFiles();	
		String esFiles = new String(); 
		String name = new String();
		String comma = new String(); 
		out.println("set EMMA=java -cp %EMULATORPATH%\\lib\\emma_device.jar emma"); 	 
		for (String ecFiles: theFiles) {
			name = "coverage" + esCounter + ".es";
			out.println("%EMMA% merge -in " + metadata + ecFiles + " -out " + name);
			esFiles += comma + name;
			comma = ",";
			esCounter++;
		}
		return esFiles;
	}

	private static String[] showFiles() {
		ArrayList<String> fileList = new ArrayList<String>();
		int counter = 0;
		String theFiles = "";
		try{
			File[] candidates = new File("../coverage/").listFiles();
			for (File file : candidates) {
				if (file.isDirectory()) continue;
				if (file.getName().endsWith(".em")) {
					metadata = file.getName();
				} else if (file.getName().endsWith(".ec")) {
					theFiles+=","+file.getName();
					counter++;
				}
				if (counter>=maxPerEs) {
					fileList.add(theFiles);
					theFiles = "";
					counter = 0;
				}
			}
		} catch (Exception e) {}
		if (!theFiles.equals("")) {
			fileList.add(theFiles);
			theFiles = "";	    	
		}
		if (fileList.size()>0) {
			String[] output = new String [fileList.size()];
			output = fileList.toArray(output);
			return output;
		}
		return null;
	}

	public File[] dirListByAscendingDate(File folder) {
		if (!folder.isDirectory()) {
			return null;
		}
		File files[] = folder.listFiles();
		Arrays.sort(files, new Comparator<Object>() {
			public int compare(final Object o1, final Object o2) {
				return new Long(((File) o1).lastModified()).compareTo(new Long(
						((File) o2).lastModified()));
			}
		});
		return files;
	}
}
