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

import static it.slumdroid.utilities.Resources.GUITREE;
import static it.slumdroid.utilities.Resources.GUITREE_DIR;
import static it.slumdroid.utilities.Resources.MAX_ES;
import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TOOL_TARGET;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class Tools {

	private static String metadata = new String();

	// Preference Editor Utilities
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
	
	// XML Utilities
	public void xmlWriter(String path, StringBuilder builder){
		xmlWriter(path, builder, path);
	}
	
	public void xmlWriter(String path, StringBuilder builder, String output){
		Document doc = null;	
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder dombuilder = factory.newDocumentBuilder(); 
			try {
				doc = dombuilder.parse( new InputSource( new StringReader( builder.toString() ) ) );
			} catch (Exception e) {
				e.printStackTrace();
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			DocumentType doctype = doc.getDoctype();
	        if(doctype != null) {
	        	if (doctype.getPublicId() != null) transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
	        	if (doctype.getSystemId() != null) transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
	        }

			DOMSource domSource = new DOMSource(doc);
			StreamResult outputstream = new StreamResult(output);
			transformer.transform(domSource, outputstream);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
	}
	
	// Split Utilities
	public void split(String path, String folder, String output, String suboutput) {
		if (!new File(folder).exists()) new File(folder).mkdir();
		String guitreeXml = path.concat(output);
		File file = new File(guitreeXml);
		if (file.exists()) file.renameTo(new File(new File(folder), suboutput + System.currentTimeMillis() + ".xml"));
		try {
			new File(guitreeXml).createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Coverage Text Parsing Utilities
	public void covTextParsing(String path) {
		try {
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains("all classes")) {
					System.out.println("Actual Coverage is");
					System.out.println("[class, %]	[method, %]	[block, %]		[line, %]");
					System.out.println(line.replace("all classes", "").replace(",","."));
					inputStream1.close();
					return;
				}
			}
			inputStream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Coverage Generator Utilities
	public void covGenerator(String header){
		try {
			PrintWriter out = new PrintWriter ("../coverage/Copertura.bat");
			String esFiles = new Tools().coverage(out);
			out.print(header + esFiles);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
	}
	
	private String coverage(PrintWriter out){
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
		String theFiles = new String();
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
				if (counter >= MAX_ES) {
					fileList.add(theFiles);
					theFiles = "";
					counter = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
	// BuildControl Utilities
	public void buildControl(String path){
		try {
			boolean success = false;
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			int count = 0;
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains("[exec] Success")) {
					count++;
					if (count == 2) success = true;  
				}
			}
			inputStream1.close();
			if (success){
				PrintWriter outputStream1 = new PrintWriter ("build.txt");
				outputStream1.write("completed");
				outputStream1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ReTargeting Utilities
	public void retarget(String fileName, String targetPackage) {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element manifest = document.getDocumentElement();
		Element target = (Element)manifest.getElementsByTagName("instrumentation").item(0);
		target.setAttribute("android:targetPackage", targetPackage);

		String newManifest = new String();
		try {
			newManifest = toXml(manifest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter autput;
		try {
			autput = new PrintWriter (fileName);
			autput.println(newManifest);
			autput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private String toXml (Element dom) throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(dom);
		StringWriter autput = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.transform(theDom, new StreamResult(autput));
		return autput.toString();
	}
	
	public String xmlToString(Document doc) throws Exception {
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
	
	// TrasformActivity Utilities
	public void traslate(String path, String output) {

		String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		StringBuilder builder = new StringBuilder();
		builder.append(xmlHead + "<ACTIVITY_STATES>");
		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {				 
				builder.append(line.replace(xmlHead, "")
						.replace("START_STATE", "ACTIVITY_STATE")
						.replace("FINAL_STATE", "ACTIVITY_STATE"));	
			}
			builder.append("</ACTIVITY_STATES>");
			inputStream1.close();
			new Tools().xmlWriter(path, builder, output);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// Merge Utilities
	public void mergeG(String path) {
		String guitreeXml = path.concat(GUITREE);
		File dir = new File(GUITREE_DIR);
		if (dir.exists() && dir.isDirectory()) merge(guitreeXml, GUITREE_DIR);
	}
	
	private void merge(String xml, String dir){
		int count = 0;
		try{
		File fl[] = new Tools().dirListByAscendingDate( new File(dir) );
		if (fl.length > 0){
			FileWriter outFile = new FileWriter(xml, false);
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
			out.println("</SESSION>");
			out.close();
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		if (count == 0) new File(xml).delete();
	}

	// UpdateProperties Utilities 
	public void updateProperties(String autPath) {
		String path = new String(autPath.concat("/project.properties"));
		String target = new String("target=android-");
		StringBuilder builder = new StringBuilder();
		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains(target)) builder.append(target + TOOL_TARGET + NEW_LINE);	 
				else builder.append(line + NEW_LINE);	
			}
			inputStream1.close();
			PrintWriter outputStream1 = new PrintWriter (path);
			outputStream1.write(builder.toString());
			outputStream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
