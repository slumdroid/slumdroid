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

import java.awt.EventQueue;
import java.io.File;

import it.slumdroid.utilities.module.*;

public class Utilities {

	static final String DIET_DIR = "./diet/";
	static final String GUITREE_DIR = "./diet/guitree/";
	static final String ACTIVITY_DIR = "./diet/activity/";

	static final String TOOL = "it.slumdroid.tool"; 

	public static void main(String[] args) {
		if (args.length!=0){
			if (args[0].equals("tasklist")){
				if (!new File(DIET_DIR).exists()) new File(DIET_DIR).mkdir();
				new UnionTaskListDiet().tasklistDiet(args[1], args[2], TOOL);
			}
			else if (args[0].contains("split")) {
				if (!new File(DIET_DIR).exists()) new File(DIET_DIR).mkdir();
				if (args[0].equals("splitGui")) new SplitGuiTree().splitG(args[1], GUITREE_DIR); 
				else if (args[0].equals("splitAct")) new SplitActivities().splitA(args[1], ACTIVITY_DIR);
			}
			else if (args[0].contains("merge")) {
				if (args[0].equals("mergeGui")) new MergeGuiTree().mergeG(args[1], GUITREE_DIR);
				else if (args[0].equals("mergeAct")) new MergeActivity().mergeA(args[1], ACTIVITY_DIR);
			}
			else if (args[0].contains("coverage")) {
				if (args[0].equals("coverageText")) new CovText().covTextParsing(args[1]); 
				else if (args[0].equals("coverageG")) new CovGenerator().covGenerator();  	
				else if (args[0].equals("coverageI")) new IncrementalCov().incrementalCoverage(); 
			}
			else if (args[0].equals("countEvents")) new CountEvents().countEvents(args[1]); 	
			else if (args[0].equals("buildControl")) new BuildControl().buildControl(args[1]);
			else if (args[0].equals("graphicalEditor")) {
				final boolean random = args[1].equals("1"); 
				final String path = args[2];
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GraphicalEditor frame = new GraphicalEditor(random, path);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			else if (args[0].equals("preferenceEditor")) new PreferenceEditor().preferenceEditor(args, TOOL); 
			else if (args[0].equals("retarget")) new Retarget().retarget(args[1], args[2]);
			else if (args[0].equals("traslate")) new TrasformActivity().traslate(args[1], args[2]);
		}
	}

}