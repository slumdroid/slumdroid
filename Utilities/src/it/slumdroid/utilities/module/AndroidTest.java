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

import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.exportToDot;
import static it.slumdroid.utilities.module.androidtest.graphviz.DotUtilities.exportToFsm;
import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.utilities.module.androidtest.stats.ReportGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class AndroidTest  {

	private String reportFileName = new String();
	private String dotFileName = new String();
	private String fsmFileName = new String();
	private GuiTree guiTree;

	public AndroidTest (String inputPath) {
		this.reportFileName = inputPath + "\\output\\report.txt";
		this.dotFileName = inputPath + "\\output\\guitree.dot";
		this.fsmFileName = inputPath + "\\output\\fsm.dot";
	}

	public void processFile(String filename) {
		try {
			this.guiTree = GuiTree.fromXml(new File (filename));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		createArtifact(exportToFsm(this.guiTree), fsmFileName); // FsmDot
		createArtifact(exportToDot(this.guiTree), dotFileName); // GuiTreeDot
		createArtifact(new ReportGenerator (this.guiTree).getReport(), reportFileName); // ReportTxt 
	}
		
	private void createArtifact(String inputString, String outputFile) {
		try {
			PrintWriter autput = new PrintWriter (inputString);
			autput.println(outputFile);
			autput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}