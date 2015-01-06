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

import static it.slumdroid.utilities.Resources.TOOL;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class UnionTaskListDiet.
 */
public class UnionTaskListDiet {

	/** The prefs. */
	private static Preferences prefs;

	/** The Constant SCHEDULER. */
	private static final String SCHEDULER ="SCHEDULER_ALGORITHM";
	
	/** The Constant BREADTH. */
	private static final String BREADTH = "BREADTH_FIRST";
	
	/** The Constant DEPTH. */
	private static final String DEPTH = "DEPTH_FIRST";
	
	/** The Constant FAKE_TASK_IDENTIFIER. */
	private static final String FAKE_TASK_IDENTIFIER = "!_FAKE_TASK_!";
	
	/** The Constant FAKE_TASK. */
	private static final String FAKE_TASK = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TASK date=\"" + FAKE_TASK_IDENTIFIER + "\" id=\"_ID_\"/>";
	
	/** The Constant ID. */
	private static final String ID = "id";
	
	/** The Constant TAG. */
	private static final String TAG = "TASK";
	
	/** The Constant TASKLIST_DIET_XML. */
	private static final String TASKLIST_DIET_XML = "./diet/tasklist_diet.xml";
	
	/** The tasklist xml. */
	private static String TASKLIST_XML = new String();
	
	/**
	 * Tasklist diet.
	 *
	 * @param filePath the file path
	 * @param preferencesPath the preferences path
	 */
	public void tasklistDiet(String filePath, String preferencesPath) {
		TASKLIST_XML = filePath + "/tasklist.xml";
		if (returnAlgorithm(preferencesPath) == null 
				|| returnAlgorithm(preferencesPath).equals(BREADTH)) {
			mergeTasklistsBreadth();
		} else {
			if (returnAlgorithm(preferencesPath).equals(DEPTH)) {
				mergeTasklistsDepth();
			} else { // RANDOM
				extractRandomTask();
			}
		}
	}
	
	/**
	 * Return algorithm.
	 *
	 * @param path the path
	 * @return the string
	 */
	private static String returnAlgorithm (String path) {
		prefs = Preferences.userRoot().node(TOOL);
		new Tools().cleanNode (prefs);
		prefs = Preferences.userRoot().node(TOOL);
		new Tools().loadNode(path);
		return prefs.get(SCHEDULER, BREADTH);
	}

	/**
	 * Merge tasklists depth.
	 */
	private static void mergeTasklistsDepth() {
		try {
			ArrayList<String> tasklist_diet = readAndDeleteTasklistDietFile();        
			if (tasklist_diet == null) {
				tasklist_diet = new ArrayList<String>();
			}

			ArrayList<String> tasklist_xml = readAndDeleteTasklistFile();
			if (tasklist_xml == null) {
				tasklist_xml = new ArrayList<String>();
			}

			ArrayList<String> tasklist_out = null;
			tasklist_out = handleDepht(tasklist_diet, tasklist_xml);

			if (tasklist_out.size() > 0) {
				FileWriter outFile = new FileWriter(TASKLIST_XML, false);
				PrintWriter out = new PrintWriter(outFile);
				handleDepthOutTasklist(out, tasklist_out);
				out.close();
				if (tasklist_out.size() > 0) {
					outFile = new FileWriter(TASKLIST_DIET_XML, false);
					out = new PrintWriter(outFile);
					for ( String s: tasklist_out ){
						out.println(s);
					}
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle depth out tasklist.
	 *
	 * @param outTasklist the out tasklist
	 * @param tasklist the tasklist
	 */
	private static void handleDepthOutTasklist(PrintWriter outTasklist, ArrayList<String> tasklist) {
		Integer id_fake = Integer.parseInt( getID( tasklist.get(tasklist.size()-1) ) ) + 1;
		outTasklist.println(FAKE_TASK.replace("_ID_", id_fake.toString())); 
		String lastString = tasklist.remove(tasklist.size() - 1);
		if (lastString.contains("fail=\"true\"")) {
			outTasklist.println(tasklist.remove(tasklist.size() - 1));
		}
		outTasklist.println(lastString);
	}

	/**
	 * Handle depht.
	 *
	 * @param tasklist_diet the tasklist_diet
	 * @param tasklist_xml the tasklist_xml
	 * @return the array list
	 */
	private static ArrayList<String> handleDepht(ArrayList<String> tasklist_diet, ArrayList<String> tasklist_xml) {
		ArrayList<String> ret = new ArrayList<String>();
		int fakeTracePosition = getFakeTracePosition(tasklist_xml);
		if (fakeTracePosition != -1) {
			tasklist_xml.remove(fakeTracePosition);
		}
		ret.addAll(tasklist_diet);
		ret.addAll(tasklist_xml);
		return ret;
	}

	/**
	 * Read and delete tasklist file.
	 *
	 * @return the array list
	 */
	private static ArrayList<String> readAndDeleteTasklistFile() {
		ArrayList<String> tasklist_xml = new ArrayList<String>();
		if (new File(TASKLIST_XML).exists()) {            
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(TASKLIST_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 1) {
						tasklist_xml.add(strLine);
					}	
				}
				in.close();
				new File(TASKLIST_XML).delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tasklist_xml;
		}
		else return null;
	}

	/**
	 * Gets the fake trace position.
	 *
	 * @param tasklist the tasklist
	 * @return the fake trace position
	 */
	private static int getFakeTracePosition(ArrayList<String> tasklist) {
		for (int item = 0; item < tasklist.size(); item++){
			if (tasklist.get(item).contains(FAKE_TASK_IDENTIFIER)) {
				return item;
			}
		}
		return -1;
	}

	/**
	 * Merge tasklists breadth.
	 */
	private static void mergeTasklistsBreadth() {
		boolean tasklist_diet_xml_found = false;
		boolean previous_track_found = false;

		int lastID = 0;
		ArrayList<String> tasklist_diet = new ArrayList<String>();
		ArrayList<String> tasklist_xml = new ArrayList<String>();

		try {
			if (new File(TASKLIST_DIET_XML).exists()) {
				tasklist_diet_xml_found = true;
				FileInputStream fstream = new FileInputStream(TASKLIST_DIET_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (!strLine.equals("")) {
						tasklist_diet.add(strLine);
					}
				}
				in.close();
				new File(TASKLIST_DIET_XML).delete();
				lastID = Integer.valueOf( getID( tasklist_diet.get(tasklist_diet.size() - 1) ) ) + 1;                
			}

			if (new File(TASKLIST_XML).exists()) {            
				FileInputStream fstream = new FileInputStream(TASKLIST_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 1) {
						if (!strLine.contains(FAKE_TASK_IDENTIFIER)) {
							tasklist_xml.add(strLine);
						} else {
							if (tasklist_xml.size() > 0) {
								previous_track_found = true;
								tasklist_diet.addAll(0, tasklist_xml);
								tasklist_xml.clear();
							}
						}
					}		
				}   
				in.close();
				new File(TASKLIST_XML).delete();
				for (String task : tasklist_xml) {
					if (tasklist_diet_xml_found) {
						tasklist_diet.add( new String( setID(task, lastID++) ) );
					} else {
						tasklist_diet.add( task );
					}
				}
			}
			if (tasklist_diet.size() > 0) {
				FileWriter outFile = new FileWriter(TASKLIST_XML, false);
				PrintWriter out = new PrintWriter(outFile);
				int num = 0;
				String lastString = tasklist_diet.remove(num);
				out.println(lastString);
				if (previous_track_found) out.println(tasklist_diet.remove(num));
				Integer id_fake = Integer.parseInt( getID( lastString ) ) + 1;
				out.println(FAKE_TASK.replace("_ID_", id_fake.toString()));
				out.close();
				if (tasklist_diet.size() > 0) {
					outFile = new FileWriter(TASKLIST_DIET_XML, false);
					out = new PrintWriter(outFile);
					for ( String s: tasklist_diet ){
						out.println(s);
					}
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extract random task.
	 */
	private static void extractRandomTask() {
		boolean tasklist_diet_xml_found = false;
		boolean previous_track_found = false;

		int lastID = 0;
		ArrayList<String> tasklist_diet = new ArrayList<String>();
		ArrayList<String> tasklist_xml = new ArrayList<String>();

		try {
			if (new File(TASKLIST_DIET_XML).exists()) {
				tasklist_diet_xml_found = true;
				FileInputStream fstream = new FileInputStream(TASKLIST_DIET_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (!strLine.equals("")) {
						tasklist_diet.add(strLine);
					}
				}
				in.close();
				new File(TASKLIST_DIET_XML).delete();
				lastID = Integer.valueOf( getID( tasklist_diet.get(tasklist_diet.size() - 1) ) ) + 1;                
			}
			if (new File(TASKLIST_XML).exists()) {            
				FileInputStream fstream = new FileInputStream(TASKLIST_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 1) {
						if (!strLine.contains(FAKE_TASK_IDENTIFIER)) {
							tasklist_xml.add(strLine);
						}
						else {
							if (tasklist_xml.size() > 0) {
								previous_track_found = true;
								tasklist_diet.addAll(0, tasklist_xml);
								tasklist_xml.clear();
							}
						}
					}		
				}   
				in.close();
				new File(TASKLIST_XML).delete();
				for (String task : tasklist_xml) {
					if (tasklist_diet_xml_found) {
						tasklist_diet.add(new String(setID(task, lastID++)));
					} else {
						tasklist_diet.add(task);
					}
				}
			}
			if (tasklist_diet.size() > 0) {
				FileWriter outFile = new FileWriter(TASKLIST_XML, false);
				PrintWriter out = new PrintWriter(outFile);

				int max = tasklist_diet.size() - 1;
				int min = 0;
				int num = new Random(System.currentTimeMillis()).nextInt((max - min) + 1) + min;
				
				String lastString = tasklist_diet.remove(num);
				out.println(lastString);
				if (previous_track_found) {
					out.println(tasklist_diet.remove(num));
				}
				Integer id_fake = Integer.parseInt(getID(lastString)) + 1;
				out.println(FAKE_TASK.replace("_ID_", id_fake.toString()));
				out.close();
				if (tasklist_diet.size() > 0) {
					outFile = new FileWriter(TASKLIST_DIET_XML, false);
					out = new PrintWriter(outFile);
					for ( String s: tasklist_diet ){
						out.println(s);
					}
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the id.
	 *
	 * @param line the line
	 * @return the id
	 */
	private static String getID(String line) {
		if (!(line != null && line.equals(""))) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(line));
				Document doc = dBuilder.parse(is);
				doc.setTextContent(line);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(TAG);
				Node firstNode = nList.item(0);
				Element firstNodeElement = (Element) firstNode;
				return firstNodeElement.getAttribute(ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Sets the id.
	 *
	 * @param line the line
	 * @param newID the new id
	 * @return the string
	 */
	private static String setID(String line, int newID) {
		if (!(line != null && line.equals(""))) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(line));
				Document doc = dBuilder.parse(is);
				doc.setTextContent(line);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(TAG);
				Node firstNode = nList.item(0);
				Element firstNodeElement = (Element) firstNode;
				firstNodeElement.setAttribute(ID, Integer.toString(newID));
				return new Tools().xmlToString(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Read and delete tasklist diet file.
	 *
	 * @return the array list
	 */
	private static ArrayList<String> readAndDeleteTasklistDietFile() {
		ArrayList<String> ret = new ArrayList<String>();
		if (new File(TASKLIST_DIET_XML).exists()) {
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(TASKLIST_DIET_XML);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					if (!strLine.equals("")){
						ret.add(strLine);
					}
				}
				in.close();
				new File(TASKLIST_DIET_XML).delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		} else {
			return null;
		}
	}

}
