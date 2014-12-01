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

package it.slumdroid.utilities;

import static it.slumdroid.utilities.Resources.ACTIVITY;
import static it.slumdroid.utilities.Resources.ACTIVITY_DIR;
import static it.slumdroid.utilities.Resources.ACTIVITY_SUB;
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

import javax.swing.JOptionPane;

public class Utilities {

	public static void main(String[] args) {
		try{
			if (args.length!=0){
				if (args[0].equals("androidTest")) {
					if (!args[1].equals("")) new AndroidTest(args[1]);
				}
				else if (args[0].equals("buildControl")) new Tools().buildControl(args[1]);
				else if (args[0].equals("countEvents")) new Tools().countEvents(args[1]);
				else if (args[0].contains("coverage")) {
					if (args[0].equals("coverageG")) new Tools().covGenerator(COV_GENERATOR);  	
					else if (args[0].equals("coverageI")) new Tools().covGenerator(INCREMENTAL_COV);
					else if (args[0].equals("coverageText")) new Tools().covTextParsing(args[1]); 
				} 	
				else if (args[0].equals("graphicalEditor")) { 
					final String expPath = args[1];
					final String appPackage = args[2];
					final String appClass = args[3];
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								final GraphicalEditor frame = new GraphicalEditor(expPath, appPackage, appClass);
								frame.addWindowListener(new WindowAdapter () {
									@Override
									public void windowClosing(WindowEvent e) {
										frame.resetDefaultValues();
										frame.saveXML(expPath);
										JOptionPane.showMessageDialog(null, "Preferences created with default values", "Information", JOptionPane.INFORMATION_MESSAGE);
									}
								});
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else if (args[0].equals("guiAnalyzer")) {
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
				else if (args[0].contains("merge")) {
					if (args[0].equals("mergeAct")) new Tools().mergeA(args[1]);
					else if (args[0].equals("mergeGui")) new Tools().mergeG(args[1]);
				}
				else if (args[0].equals("preferenceEditor")) new PreferenceEditor().preferenceEditor(args); 
				else if (args[0].equals("retarget")) new Tools().retarget(args[1], args[2]);
				else if (args[0].contains("split")) {
					if (!new File(DIET_DIR).exists()) new File(DIET_DIR).mkdir();
					if (args[0].equals("splitAct")) new Tools().split(args[1], ACTIVITY_DIR, ACTIVITY, ACTIVITY_SUB);
					else if (args[0].equals("splitGui")) new Tools().split(args[1], GUITREE_DIR, GUITREE, GUITREE_SUB); 			
				}
				else if (args[0].equals("tasklist")){
					if (!new File(DIET_DIR).exists()) new File(DIET_DIR).mkdir();
					new UnionTaskListDiet().tasklistDiet(args[1], args[2]);
				}
				else if (args[0].equals("traslate")) new Tools().traslate(args[1], args[2]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}