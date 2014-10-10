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

package it.slumdroid.androidtest;

import static it.slumdroid.androidtest.Resources.DEFAULT_HEIGHT;
import static it.slumdroid.androidtest.Resources.DEFAULT_WIDTH;
import static it.slumdroid.androidtest.Resources.FSM_DOT;
import static it.slumdroid.androidtest.Resources.GUI_TREE;
import static it.slumdroid.androidtest.Resources.GUI_TREE_DOT;
import static it.slumdroid.androidtest.Resources.GUI_TREE_FILE_TYPE;
import static it.slumdroid.androidtest.Resources.TEST_REPORT;
import static it.slumdroid.androidtest.graphviz.DotUtilities.exportToDot;
import static it.slumdroid.androidtest.graphviz.DotUtilities.exportToFsm;
import it.slumdroid.androidtest.source.JSourceCodeArea;
import it.slumdroid.androidtest.source.JSourceCodePane;
import it.slumdroid.androidtest.stats.ReportGenerator;
import it.slumdroid.droidmodels.guitree.GuiTree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import net.iharder.dnd.FileDrop;

import org.xml.sax.SAXException;

public class Xml2DotFrame extends JFrame  {

	private String inputFileName = new String();
	private String reportFileName = new String();
	private String dotFileName = new String();
	private String fsmFileName = new String();
	private boolean windowed;
	private GuiTree guiTree;
	private JSourceCodePane schermo;
	private static final long serialVersionUID = 1L;

	public Xml2DotFrame (String inputPath) {
		super();
		setTitle("AndroidTest");
		if (!inputPath.equals("")){
			this.inputFileName = inputPath + "\\files\\guitree.xml";
			this.reportFileName = inputPath + "\\output\\report.txt";
			this.dotFileName = inputPath + "\\output\\guitree.dot";
			this.fsmFileName = inputPath + "\\output\\fsm.dot";
		}
		this.windowed = (inputFileName.equals(""));
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		JSourceCodeArea xmlInput = new JSourceCodeArea();
		xmlInput.setEditable(false);

		JPopupMenu m = xmlInput.getComponentPopupMenu();
		m.add (new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				JFileChooser toLoad = new JFileChooser ();
				toLoad.setCurrentDirectory(new File ("."));
				toLoad.setFileFilter(new FileNameExtensionFilter(GUI_TREE_FILE_TYPE, "xml"));
				int code = toLoad.showOpenDialog (Xml2DotFrame.this.getParent());
				if (code == JFileChooser.APPROVE_OPTION) {
					File theFile = toLoad.getSelectedFile();
					processFile (theFile);
				}
			}
		});

		JSourceCodeArea dotGuiTree = new JSourceCodeArea();
		dotGuiTree.setDefaultExtension("dot");
		JSourceCodeArea dotFsm = new JSourceCodeArea();
		dotFsm.setDefaultExtension("dot");
		JSourceCodeArea testReport = new JSourceCodeArea();
		testReport.setDefaultExtension("txt");

		schermo = new JSourceCodePane();
		schermo.addEnabled(GUI_TREE, null, xmlInput);
		schermo.addDisabled(GUI_TREE_DOT, null, dotGuiTree);
		schermo.addDisabled(FSM_DOT, null, dotFsm);
		schermo.addDisabled(TEST_REPORT, null, testReport);

		new FileDrop (null, xmlInput, new FileDrop.Listener() {
			public void filesDropped(File[] files ) {
				if (files.length==0) return;
				processFile(files[0]);
			}
		});

		getContentPane().add(schermo);
		if (!inputFileName.equals("")) { 
			processFile(inputFileName);
		}
	}

	public void processFile(String filename) {
		processFile (new File (filename));
	}

	private void processFile(File file) {
		setFilename (file);	
		try {
			this.guiTree = GuiTree.fromXml(file);
			if (isWindowed()) this.showInputXml(); 
			this.showGuiTreeDot();
			this.showFsmDot();
			this.showReport();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isWindowed () {
		return this.windowed;
	}

	public String getFilename() {
		return this.inputFileName;
	}

	public void setFilename(String filename) {
		this.inputFileName = filename;
		this.setTitle(filename);
	}

	public void setFilename(File theFile) {
		setFilename (theFile.getAbsolutePath());
		for (Component c: this.schermo) {
			if (c instanceof JSourceCodeArea) {
				((JSourceCodeArea)c).setDefaultPath(theFile.getPath());
			}
		}
	}

	private void showInputXml () {
		StringBuffer input= new StringBuffer();
		try {
			FileInputStream fstream = new FileInputStream(getFilename());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				input.append(strLine + System.getProperty("line.separator"));
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		schermo.showCode(GUI_TREE, input.toString());
	}

	private void showFsmDot() {	
		String fsm = exportToFsm(this.guiTree);
		schermo.setFileName(FSM_DOT, this.fsmFileName.equals("")?"fsm.dot":this.fsmFileName);
		if (isWindowed()) {
			schermo.showCode(FSM_DOT, fsm);
		} else {
			PrintWriter autput;
			try {
				autput = new PrintWriter (this.fsmFileName);
				autput.println(fsm);
				autput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
	}

	private void showGuiTreeDot() {
		String dot = exportToDot(this.guiTree);
		schermo.setFileName(GUI_TREE_DOT, this.dotFileName.equals("")?"guitree.dot":this.dotFileName);
		if (isWindowed()) {
			schermo.showCode(GUI_TREE_DOT, dot);
		} else {
			PrintWriter autput;
			try {
				autput = new PrintWriter (this.dotFileName);
				autput.println(dot);
				autput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
	}

	private void showReport() {
		ReportGenerator r = new ReportGenerator (this.guiTree);
		String report = r.getReport();
		schermo.setFileName(TEST_REPORT, this.reportFileName.equals("")?"report.txt":this.reportFileName);
		if (isWindowed()) {
			schermo.showCode(TEST_REPORT, report);
		} else {
			PrintWriter autput;
			try {
				autput = new PrintWriter (this.reportFileName);
				autput.println(report);
				autput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
