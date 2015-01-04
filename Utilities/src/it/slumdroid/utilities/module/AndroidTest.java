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

package it.slumdroid.utilities.module;

import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.exportToDot;
import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.exportToFsm;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.utilities.module.androidtest.stats.ReportGenerator;

import java.io.File;
import java.io.PrintWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class AndroidTest.
 */
public class AndroidTest  {

	/** The input file name. */
	private String inputFileName = new String();
	
	/** The report file name. */
	private String reportFileName = new String();
	
	/** The dot file name. */
	private String dotFileName = new String();
	
	/** The fsm file name. */
	private String fsmFileName = new String();

	/** The txt file name. */
	private static String txtFileName = new String();
	
	/** The state file name. */
	private static String stateFileName = new String();

	/** The gui tree. */
	private GuiTree guiTree;

	/**
	 * Instantiates a new android test.
	 *
	 * @param inputPath the input path
	 */
	public AndroidTest (String inputPath) {
		if (!inputPath.equals("")) {

			// Inputs
			this.inputFileName = inputPath + "\\files\\guitree.xml";
			AndroidTest.stateFileName = inputPath + "\\files\\activities.xml";
			AndroidTest.txtFileName = inputPath + "\\test.txt";
			
			// Outputs
			this.reportFileName = inputPath + "\\output\\report.txt";
			this.dotFileName = inputPath + "\\output\\guitree.dot";
			this.fsmFileName = inputPath + "\\output\\fsm.dot";

			try {
				this.guiTree = GuiTree.fromXml(new File (getInputFileName()));
				processFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Process file.
	 */
	private void processFile() {
		createArtifact(exportToFsm(this.guiTree), getFsmFileName()); // FsmDot
		createArtifact(exportToDot(this.guiTree), getDotFileName()); // GuiTreeDot
		String report = new ReportGenerator(this.guiTree).getReport(); 
		createArtifact(report, getReportFileName()); // ReportTxt 
	}

	/**
	 * Creates the artifact.
	 *
	 * @param inputString the input string
	 * @param outputFile the output file
	 */
	private void createArtifact(String inputString, String outputFile) {
		try {
			PrintWriter autput = new PrintWriter (outputFile);
			autput.println(inputString);
			autput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the input file name.
	 *
	 * @return the input file name
	 */
	private String getInputFileName() {
		return inputFileName;
	}

	/**
	 * Gets the report file name.
	 *
	 * @return the report file name
	 */
	private String getReportFileName() {
		return reportFileName;
	}

	/**
	 * Gets the dot file name.
	 *
	 * @return the dot file name
	 */
	private String getDotFileName() {
		return dotFileName;
	}

	/**
	 * Gets the fsm file name.
	 *
	 * @return the fsm file name
	 */
	private String getFsmFileName() {
		return fsmFileName;
	}

	/**
	 * Gets the txt file name.
	 *
	 * @return the txt file name
	 */
	public static String getTxtFileName() {
		return txtFileName;
	} 

	/**
	 * Gets the state file name.
	 *
	 * @return the state file name
	 */
	public static String getStateFileName() {
		return stateFileName;
	} 

}