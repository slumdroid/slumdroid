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

package it.slumdroid.droidmodels;

import it.slumdroid.droidmodels.guitree.*;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.testcase.TestCaseActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

public class ActivityMap implements Iterable<ActivityState> {

	public Map<String,ActivityState> activities;
	public GuiTree doc;

	public ActivityMap (GuiTree theDoc) {
		this.doc = theDoc;
		this.activities = new LinkedHashMap<String, ActivityState>();
	}

	public void addActivity(ActivityState t) {
		addActivity(t.getId(), t);
	}

	public void addActivity(String id, ActivityState t) {
		this.activities.put(id, t);
	}

	public ActivityState getActivity(String id) {
		return this.activities.get(id);
	}

	public ActivityState getActivity(ActivityState t) {
		return getActivity(t.getDescriptionId());
	}

	public boolean hasActivity (ActivityState t) {
		return hasActivity(t.getDescriptionId());
	}

	public boolean hasActivity (String id) {
		return this.activities.containsKey(id);
	}

	public List<String> readStateFile (String stateFileName) {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		try{
			theFile = new FileInputStream(stateFileName);
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			boolean first = true;
			while ( (line = theStream.readLine()) != null) {
				String dtd = (first)?"start_activity.dtd":"final_activity.dtd";
				String tag = (first)?"START_STATE":"FINAL_STATE";
				String root = "<"+tag;
				String doctype = "<!DOCTYPE " + tag + " PUBLIC \"" + tag + "\" \"" + dtd + "\">";
				output.add(line.replaceFirst(root, doctype + root));
				first = false;
			}
			theFile.close();
			theStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public void loadActivities(String stateFile) {
		List<String> entries = readStateFile(stateFile); 
		GuiTree sandboxSession = getNewSession();
		TestCaseActivity s;
		Element e;
		for (String state: entries) {
			sandboxSession.parse(state);
			e = sandboxSession.getDom().getDocumentElement();
			s = sandboxSession.importState (e);
			addActivity(s);
		}
	}

	public GuiTree getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Iterator<ActivityState> iterator() {
		return activities.values().iterator();
	}

}
