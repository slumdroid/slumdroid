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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class UnionTaskListDiet {

	static Preferences prefs;

	static final String FAKE_TASK_IDENTIFIER = "!_FAKE_TASK_!";
	static final String FAKE_TASK = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TASK date=\""+FAKE_TASK_IDENTIFIER+"\" id=\"_ID_\"/>";
	static final String TASKLIST_DIET_XML = "./diet/tasklist_diet.xml";
	static String TASKLIST_XML = new String();

	public void tasklistDiet(String filePath, String preferencesPath, String packageTool){
		TASKLIST_XML = filePath + "/tasklist.xml";
		if (!loadPreferences(preferencesPath, packageTool)) {
			try {
				mergeTasklistsDepth();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				mergeTasklistsBreadth();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean loadPreferences (String path, String packageTool) {
		prefs = Preferences.userRoot().node(packageTool);
		new Tools().cleanNode (prefs);
		prefs = Preferences.userRoot().node(packageTool);
		new Tools().loadNode(path);
		String BREADTH = "BREADTH_FIRST";
		return (prefs.get("SCHEDULER_ALGORITHM", BREADTH).equals(BREADTH));
	}

	public static void mergeTasklistsDepth() throws Exception {	
		ArrayList<String> tasklist_diet = readAndDeleteTasklistDietFile();        
		if (tasklist_diet == null) tasklist_diet = new ArrayList<String>();

		ArrayList<String> tasklist_xml = readAndDeleteTasklistFile();
		if (tasklist_xml == null) tasklist_xml = new ArrayList<String>();

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

				for ( String s: tasklist_out )
					out.println(s);            

				out.close();
			}
		}
	}

	private static void handleDepthOutTasklist(PrintWriter outTasklist, ArrayList<String> tasklist) throws Exception {
		Integer id_fake = Integer.parseInt( getID( tasklist.get(tasklist.size()-1) ) ) + 1;
		outTasklist.println(FAKE_TASK.replace("_ID_", id_fake.toString())); 
		String lastString = tasklist.remove(tasklist.size() - 1);
		if (lastString.contains("fail=\"true\"")) outTasklist.println(tasklist.remove(tasklist.size() - 1));
		outTasklist.println(lastString);
	}

	private static ArrayList<String> handleDepht(ArrayList<String> tasklist_diet, ArrayList<String> tasklist_xml) {
		ArrayList<String> ret = new ArrayList<String>();
		int fakeTracePosition = getFakeTracePosition(tasklist_xml);
		if (fakeTracePosition != -1) tasklist_xml.remove(fakeTracePosition);
		ret.addAll(tasklist_diet);
		ret.addAll(tasklist_xml);
		return ret;
	}

	private static ArrayList<String> readAndDeleteTasklistFile() throws Exception {
		ArrayList<String> tasklist_xml = new ArrayList<String>();
		if (new File(TASKLIST_XML).exists()) {            
			FileInputStream fstream = new FileInputStream(TASKLIST_XML);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null)
				if (strLine.length() > 1)
					tasklist_xml.add(strLine);

			in.close();
			new File(TASKLIST_XML).delete();

			return tasklist_xml;
		}
		else return null;

	}

	private static int getFakeTracePosition(ArrayList<String> tasklist) {
		for (int i = 0; i < tasklist.size(); i++)
			if (tasklist.get(i).contains(FAKE_TASK_IDENTIFIER))
				return i;
		return -1;
	}

	public static void mergeTasklistsBreadth() {
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
					if (strLine.equals("") == false )
						tasklist_diet.add(strLine);
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
						if (strLine.contains(FAKE_TASK_IDENTIFIER) == false) {
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
						tasklist_diet.add( new String( setID(task, lastID++) ) );
					}
					else {
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

					for ( String s: tasklist_diet )
						out.println(s);            

					out.close();
				}
			}           
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getID(String line) throws Exception {
		if (line != null && line.equals("") == false) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(line));

				Document doc = dBuilder.parse(is);

				doc.setTextContent(line);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("TRACE");

				Node firstNode = nList.item(0);
				Element firstNodeElement = (Element) firstNode;

				return firstNodeElement.getAttribute("id");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String setID(String line, int newID) throws Exception {
		if (line != null && line.equals("") == false) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(line));

				Document doc = dBuilder.parse(is);

				doc.setTextContent(line);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("TRACE");

				Node firstNode = nList.item(0);
				Element firstNodeElement = (Element) firstNode;

				firstNodeElement.setAttribute("id", Integer.toString(newID));

				return convertXMLFileToString(doc);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String convertXMLFileToString(Document doc) throws Exception {
		try {
			StringWriter stw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(new DOMSource(doc), new StreamResult(stw));
			return stw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String();
	}

	private static ArrayList<String> readAndDeleteTasklistDietFile() throws Exception {
		ArrayList<String> ret = new ArrayList<String>();

		if (new File(TASKLIST_DIET_XML).exists()) {
			FileInputStream fstream = new FileInputStream(TASKLIST_DIET_XML);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.equals("") == false )
					ret.add(strLine);
			}

			in.close();
			new File(TASKLIST_DIET_XML).delete();

			return ret;
		}
		else return null;
	}

}
