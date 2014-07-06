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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MergeGuiTree {

	public void mergeG(String path, String folder) {
		String closedTxt = path.concat("/closed.txt");
		String guitreeXml = path.concat("/guitree.xml");
		int count = 0;
		try{
			File dir = new File(folder);
			if (dir.exists() && dir.isDirectory()) {
				File fl[] = new Tools().dirListByAscendingDate(new File(folder));
				if (fl.length > 0){
					FileWriter outFile = new FileWriter(guitreeXml, false);
					PrintWriter out = new PrintWriter(outFile);
					for (File f : fl){
						FileReader inFile = new FileReader(f); 
						BufferedReader in = new BufferedReader(inFile);
						String s = null; 
						while((s = in.readLine()) != null) {
							if (!s.equals("")) {
								count++;
								out.println(s);
							}
						}
						inFile.close(); 
					}
					if (!new File(closedTxt).exists()) out.println("</SESSION>");
					out.close();
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		if (count==0) new File(guitreeXml).delete();
	}
}
