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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Tools.
 */
public class Tools {

	/** The metadata. */
	private static String metadata = new String();

	// Preference Editor Utilities
	/**
	 * Clean node.
	 *
	 * @param prefs the prefs
	 */
	public void cleanNode (Preferences prefs) {
		try {
			prefs.removeNode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load node.
	 *
	 * @param path the path
	 * @return true, if successful
	 */
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

	/**
	 * Save node.
	 *
	 * @param args the args
	 * @param prefs the prefs
	 */
	public void saveNode(String args, Preferences prefs) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(args);
			prefs.exportSubtree(fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * New seed.
	 *
	 * @param seed the seed
	 * @return the long
	 */
	public long newSeed (String seed) {
		Random Rs = new Random(Long.parseLong(seed));
		Long generatedSeed = Rs.nextLong();
		return generatedSeed;
	}

	/**
	 * Dir list by ascending date.
	 *
	 * @param folder the folder
	 * @return the file[]
	 */
	public File[] dirListByAscendingDate(File folder) {
		if (!folder.isDirectory()) {
			return null;
		}
		File files[] = folder.listFiles();
		Arrays.sort(files, new Comparator<Object>() {
			public int compare(final Object o1, final Object o2) {
				return new Long(((File) o1).lastModified()).compareTo(new Long(((File) o2).lastModified()));
			}
		});
		return files;
	}
	
	// XML Utilities
	/**
	 * Xml writer.
	 *
	 * @param path the path
	 * @param builder the builder
	 */
	public void xmlWriter(String path, StringBuilder builder){
		xmlWriter(path, builder, path);
	}
	
	/**
	 * Xml writer.
	 *
	 * @param path the path
	 * @param builder the builder
	 * @param output the output
	 */
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
	        	if (doctype.getPublicId() != null) {
	        		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
	        	}
	        	if (doctype.getSystemId() != null) {
	        		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
	        	}
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
	/**
	 * Split.
	 *
	 * @param path the path
	 * @param folder the folder
	 * @param output the output
	 * @param suboutput the suboutput
	 */
	public void split(String path, String folder, String output, String suboutput) {
		if (!new File(folder).exists()) {
			new File(folder).mkdir();
		}
		String guitreeXml = path.concat(output);
		File file = new File(guitreeXml);
		if (file.exists()) {
			file.renameTo(new File(new File(folder), suboutput + System.currentTimeMillis() + ".xml"));
		}
		try {
			new File(guitreeXml).createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Coverage Text Parsing Utilities
	/**
	 * Cov text parsing.
	 *
	 * @param path the path
	 */
	public void covTextParsing(String path) {
		BufferedReader inputStream1 = null;
		try {
			inputStream1 = new BufferedReader (new FileReader (path));
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Coverage Generator Utilities
	/**
	 * Cov generator.
	 *
	 * @param header the header
	 */
	public void covGenerator(String header){
		try {
			PrintWriter out = new PrintWriter ("../coverage/Copertura.bat");
			String esFiles = new Tools().coverage(out);
			out.print(header + esFiles);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}	
	}
	
	/**
	 * Coverage.
	 *
	 * @param out the out
	 * @return the string
	 */
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
	
	/**
	 * Show files.
	 *
	 * @return the string[]
	 */
	private static String[] showFiles() {
		ArrayList<String> fileList = new ArrayList<String>();
		int counter = 0;
		String theFiles = new String();
		try{
			File[] candidates = new File("../coverage/").listFiles();
			for (File file : candidates) {
				if (file.isDirectory()) {
					continue;
				}
				if (file.getName().endsWith(".em")) {
					metadata = file.getName();
				} else if (file.getName().endsWith(".ec")) {
					theFiles += "," + file.getName();
					counter++;
				}
				if (counter >= MAX_ES) {
					fileList.add(theFiles);
					theFiles = new String();
					counter = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!theFiles.equals("")) {
			fileList.add(theFiles);
			theFiles = new String();	    	
		}
		if (fileList.size() > 0) {
			String[] output = new String [fileList.size()];
			output = fileList.toArray(output);
			return output;
		}
		return null;
	}
		
	// BuildControl Utilities
	/**
	 * Control the Building.
	 *
	 * @param path the path
	 */
	public void buildControl(String path) {
		try {
			boolean success = false;
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			int count = 0;
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains("[exec] Success")) {
					count++;
					if (count == 2) {
						success = true;  
					}
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
	/**
	 * Retarget.
	 *
	 * @param fileName the file name
	 * @param targetPackage the target package
	 */
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

		PrintWriter output;
		try {
			output = new PrintWriter (fileName);
			output.println(newManifest);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * To xml.
	 *
	 * @param dom the dom
	 * @return the string
	 * @throws TransformerFactoryConfigurationError the transformer factory configuration error
	 * @throws TransformerException the transformer exception
	 */
	private String toXml (Element dom) throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(dom);
		StringWriter output = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.transform(theDom, new StreamResult(output));
		return output.toString();
	}
	
	/**
	 * Xml to string.
	 *
	 * @param doc the doc
	 * @return the string
	 * @throws Exception the exception
	 */
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
	/**
	 * Traslate.
	 *
	 * @param path the path
	 * @param output the output
	 */
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
	/**
	 * Merge g.
	 *
	 * @param path the path
	 */
	public void mergeG(String path) {
		String guitreeXml = path.concat(GUITREE);
		File dir = new File(GUITREE_DIR);
		if (dir.exists() && dir.isDirectory()) {
			merge(guitreeXml, GUITREE_DIR);
		}
	}
	
	/**
	 * Merge.
	 *
	 * @param xml the xml
	 * @param dir the dir
	 */
	private void merge(String xml, String dir) {
		int count = 0;
		boolean session_found = false;
		try{
		File fl[] = new Tools().dirListByAscendingDate( new File(dir) );
		if (fl.length > 0){
			FileWriter outFile = new FileWriter(xml, false);
			PrintWriter out = new PrintWriter(outFile);	
			for (File f : fl) {
				FileReader inFile = new FileReader(f); 
				BufferedReader in = new BufferedReader(inFile);
				String s = null; 
				while((s = in.readLine()) != null) {
					if (!s.equals("")) {
						count++;
						if (s.contains("</SESSION>")) {
							session_found = true;
						}
						out.println(s);
					}
				}
				inFile.close(); 
			}
			if (!session_found) {
				out.println("</SESSION>");
			}
			out.close();
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		if (count == 0) {
			new File(xml).delete();
		}
	}

	// UpdateProperties Utilities 
	/**
	 * Update properties.
	 *
	 * @param autPath the aut path
	 */
	public void updateProperties(String autPath) {
		String path = new String(autPath.concat("/project.properties"));
		String target = new String("target=android-");
		StringBuilder builder = new StringBuilder();
		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains(target)) {
					builder.append(target + TOOL_TARGET + NEW_LINE);	 
				} else {
					builder.append(line + NEW_LINE);	
				}
			}
			inputStream1.close();
			PrintWriter outputStream1 = new PrintWriter (path);
			outputStream1.write(builder.toString());
			outputStream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Trend Utilities
	/**
	 * Trend test.
	 *
	 * @param inputPath the input path
	 */
	public void trendTest(String inputPath) {
		if (inputPath.equals("")) {
			inputPath = new String(System.getProperty("user.dir"));
		}
		final String TASK = new String("Playing Task "); 
		final String TIME = new String("Time: ");
		final String ACTUAL = new String("Actual Coverage is");
		
		double seconds = 0;
		int task = 0;
		int time = 0;
		
		String coverage = new String();
		Stats item = new Stats(task, time, coverage);
		
		BufferedReader inputStream1 = null;
		ArrayList<Stats> list = new ArrayList<Stats>();
		boolean interesting = false;
		
		try {
			inputStream1 = new BufferedReader (new FileReader (inputPath + "\\test.txt"));
			String line = new String();
			while ((line = inputStream1.readLine()) != null ) {
				if (line.contains(TASK)) {
					task = Integer.valueOf(line.replace(TASK, "").replace(" ", "")) + 1;
				}
				if (line.contains(ACTUAL)) {
					for (int jump = 0; jump < 2; jump++) { 
						line = inputStream1.readLine();
					}
					String[] coverageLine = line.replace("\t","").replace("!", "").split(Pattern.quote(")"));
					String tempLoc = new String(coverageLine[coverageLine.length - 1]) + ")";
					if (!coverage.equals(tempLoc)) { 
						interesting = true;
						coverage = new String(tempLoc);
					}	
				}
				if (line.contains(TIME)) {
					seconds += Double.valueOf(line.replace(TIME, ""));
					String round = String.valueOf(seconds).replace("."," ").split(" ")[0];
					time = Integer.valueOf(round);
				}
				if (interesting) {
					item = new Stats(task, time, coverage);
					list.add(item);
					interesting = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (time != 0) {
			item = new Stats(task, time, coverage); // Last Task
			list.add(item);
			PrintWriter outputStream1 = null;
			if (!new File(inputPath + "\\output").exists()) {
				new File(inputPath + "\\output").mkdir();
			}
			try {
				String format = "%-6s %12s \t%-12s";
				outputStream1 = new PrintWriter (inputPath + "\\output\\trend.txt");
				outputStream1.write(String.format(format, "Task", "Coverage", "Time"));
				outputStream1.write(System.getProperty("line.separator"));
				for (int index = 0; index < list.size(); index++) {
					item = list.get(index);
					outputStream1.flush();
					format = "%-6s %-1s\t%-12s";
					String row = String.format(format, 
							String.format("%04d", item.getTaskID()), 
							item.getLoC().replace(".", ",").replace("% ", "%"), 
							String.format("%04d", item.getActualTime()));
					outputStream1.write(row);
					outputStream1.write(System.getProperty("line.separator"));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				outputStream1.close();
			}
		}		
	}
	
	/**
	 * The Class Stats.
	 */
	protected class Stats {

		/** The task id. */
		private int taskID;
		
		/** The actual time. */
		private int actualTime;
		
		/** The Lo c. */
		private String LoC;
		
		/**
		 * Instantiates a new stats.
		 *
		 * @param id the id
		 * @param actual the actual
		 * @param coverage the coverage
		 */
		public Stats(int id, int actual, String coverage){
			this.taskID = id;
			this.actualTime = actual;
			this.LoC = new String(coverage);
		}
		
		/**
		 * Gets the task id.
		 *
		 * @return the task id
		 */
		public int getTaskID() {
			return taskID;
		}
		
		/**
		 * Gets the actual time.
		 *
		 * @return the actual time
		 */
		public int getActualTime() {
			return actualTime;
		}
		
		/**
		 * Gets the Lines of Code Coverage.
		 *
		 * @return the Lines of Code Coverage
		 */
		public String getLoC() {
			return LoC;
		}
		
	}

}
