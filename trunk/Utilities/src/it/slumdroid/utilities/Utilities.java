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

package it.slumdroid.utilities;

import static it.slumdroid.utilities.Resources.COV_GENERATOR;
import static it.slumdroid.utilities.Resources.DIET_DIR;
import static it.slumdroid.utilities.Resources.GUITREE;
import static it.slumdroid.utilities.Resources.GUITREE_DIR;
import static it.slumdroid.utilities.Resources.GUITREE_SUB;
import static it.slumdroid.utilities.Resources.INCREMENTAL_COV;
import it.slumdroid.utilities.module.AndroidTest;
import it.slumdroid.utilities.module.GraphicalEditor;
import it.slumdroid.utilities.module.GuiAnalyzer;
import it.slumdroid.utilities.module.PreferenceEditor;
import it.slumdroid.utilities.module.Tools;
import it.slumdroid.utilities.module.UnionTaskListDiet;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Utilities {

	public static void main(String[] args) {
		try{
			if (args.length != 0) {
				String keyWord = args[0];
				if (keyWord.equals("androidTest")) new AndroidTest(args[1]);
				else if (keyWord.equals("buildControl")) new Tools().buildControl(args[1]);
				else if (keyWord.contains("coverage")) {
					if (keyWord.equals("coverageG")) new Tools().covGenerator(COV_GENERATOR);  	
					else if (keyWord.equals("coverageI")) new Tools().covGenerator(INCREMENTAL_COV);
					else if (keyWord.equals("coverageText")) new Tools().covTextParsing(args[1]); 
				} 	
				else if (keyWord.equals("graphicalEditor")) {
					final String expPath = args[1];
					final String appPackage = args[2];
					final String appClass = args[3];
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							final GraphicalEditor frame = new GraphicalEditor(expPath, appPackage, appClass);
							frame.addWindowListener(new WindowAdapter () {
								@Override
								public void windowClosing(WindowEvent e) {
									frame.resetDefaultValues();
									frame.saveXML();
								}
							});
							frame.setVisible(true);
						}
					});
				}
				else if (keyWord.equals("guiAnalyzer")) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								new GuiAnalyzer().setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else if (keyWord.equals("mergeGui")) new Tools().mergeG(args[1]); 
				else if (keyWord.equals("retarget")) new Tools().retarget(args[1], args[2]);
				else if (keyWord.equals("splitGui") || keyWord.equals("tasklist")) {
					if (!new File(DIET_DIR).exists()) new File(DIET_DIR).mkdir();
					if (keyWord.equals("splitGui")) new Tools().split(args[1], GUITREE_DIR, GUITREE, GUITREE_SUB);
					if (keyWord.equals("tasklist")) new UnionTaskListDiet().tasklistDiet(args[1], args[2]); 
				}
				else if (keyWord.equals("properties")) new Tools().updateProperties(args[1]);
				else if (keyWord.equals("traslate")) new Tools().traslate(args[1], args[2]);
				else if (keyWord.equals("trend")) {
					String path = new String();
					if (args.length != 1) {
						path = new String(args[1]);
					}
					new Tools().trendTest(path);
				}
				else if (keyWord.equals("preferenceEditor")) new PreferenceEditor().preferenceEditor(args);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}