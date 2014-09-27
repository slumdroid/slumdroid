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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.tool.model.*;
import static it.slumdroid.tool.Resources.ONLY_FINAL_TRANSITION;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import android.content.ContextWrapper;

import it.slumdroid.droidmodels.guitree.FinalActivity;
import it.slumdroid.droidmodels.model.*;
import it.slumdroid.droidmodels.xml.ElementWrapper;

public class ResumingPersistence extends StepDiskPersistence implements DispatchListener, StateDiscoveryListener, TerminationListener {

	private List<Trace> taskList;
	private String activityFile;
	private String taskListFile;
	private String parametersFile;
	private FileOutputStream taskFile;
	private OutputStreamWriter taskStream;
	private FileOutputStream stateFile;
	private OutputStreamWriter stateStream;
	private Map<String, SessionParams> parameters = new Hashtable<String, SessionParams>();
	private Hashtable<String,SaveStateListener> theListeners = new Hashtable<String,SaveStateListener>();

	public ResumingPersistence () {
		super(1);
	}

	public ResumingPersistence (Session theSession) {
		super(theSession, 1);
	}

	@Override 
	public void addTrace (Trace t) {
		t.setFailed(false);
		super.addTrace(t);
	}

	public void onNewTaskAdded (Trace t) { /* do nothing */ }

	public void onTaskDispatched(Trace t) {
		t.setFailed(true);
		saveTaskList();
		saveParameters();
	}

	public void saveTaskList() {
		if (noTasks()) {
			delete (getTaskListFileName());
			return;
		}

		if (exists(getTaskListFileName())) {
			backupFile (getTaskListFileName());
		}

		try {
			openTaskFile();
			for (Trace task: this.taskList) {
				String xml = new String();
				if (ONLY_FINAL_TRANSITION){
					try{
						Transition support = task.getFinalTransition();
						xml = ((ElementWrapper)support).toXml() + System.getProperty("line.separator");
					}catch (Exception e){
						xml = ((ElementWrapper)task).toXml() + System.getProperty("line.separator");
					}
				}
				else xml = ((ElementWrapper)task).toXml() + System.getProperty("line.separator");
				writeOnTaskFile(xml);
			}
			closeTaskFile();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (exists(backup(this.taskListFile))) {
			delete (backup(this.taskListFile));
		}
		if (exists(backup(this.activityFile))) {
			delete (backup(this.activityFile));
		}

	}

	public boolean canHasResume () {
		if (!exists(getFileName())) {
			return false; 
		}
		if (!exists(getActivityFileName())) throw new Error("Cannot resume previous session: state list not found.");
		if (exists(backup(getTaskListFileName()))) {
			restoreFile(getTaskListFileName());
			if (exists(backup(getActivityFileName()))) {
				restoreFile(getActivityFileName());
			}
		}
		if (!exists(getTaskListFileName())) {
			return false;
		}
		return true;
	}

	public void backupFile (String fileName) {
		copy(fileName,backup(fileName));
	}

	public void restoreFile (String fileName) {
		copy(backup(fileName),fileName);
	}	

	public String backup (String original) {
		return original + ".bak";
	}

	public void saveParameters() {
		parameters.clear();
		FileOutputStream theFile = null;
		ObjectOutputStream theStream = null;

		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			parameters.put(listener.getKey(), listener.getValue().onSavingState());
		}

		try {
			theFile = w.openFileOutput(getParametersFileName(), ContextWrapper.MODE_PRIVATE);
			theStream = new ObjectOutputStream(theFile);
			theStream.writeObject(this.parameters);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (theFile!=null && theStream!=null) {
				closeFile(theFile, theStream);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadParameters() {
		FileInputStream theFile = null;
		ObjectInputStream theStream = null;

		try {
			theFile = w.openFileInput(getParametersFileName());
			theStream = new ObjectInputStream(theFile);
			this.parameters = (Map<String, SessionParams>) theStream.readObject();
			theFile.close();
			theStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException ignore) {}

		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			listener.getValue().onLoadingState(this.parameters.get(listener.getKey()));
		}
	}

	public void onNewState(ActivityState newState) {
		if (exists(getActivityFileName())) {
			backupFile (getActivityFileName());
		}
		try {
			openStateFile(newState instanceof FinalActivity); 
			String xml = ((ElementWrapper)newState).toXml() + System.getProperty("line.separator");
			writeOnStateFile(xml);
			closeStateFile();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		super.save();
		saveParameters();
		saveTaskList();
		if (noTasks()) { 
			delete (backup(getActivityFileName()));
			delete (getParametersFileName());
			delete (backup(getParametersFileName()));
			delete (getTaskListFileName());
			delete (backup(getTaskListFileName()));

			FileOutputStream fOut;
			try {
				fOut = w.openFileOutput("closed.txt", ContextWrapper.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut); 
				osw.write("the end");
				osw.flush();
				osw.close();
				fOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}
	}

	@Override
	public boolean isLast() {
		return ( (super.isLast()) && noTasks() );
	}

	public void onTerminate() {
		this.taskList.clear();
	}

	public List<String> readTaskFile () {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		try{
			theFile = w.openFileInput (getTaskListFileName());
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
			theFile.close();
			theStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public List<String> readStateFile () {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		try{
			theFile = w.openFileInput (getActivityFileName());
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
			theFile.close();
			theStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public void openTaskFile () {
		try{
			this.taskFile = w.openFileOutput(getTaskListFileName(), ContextWrapper.MODE_PRIVATE);
			this.taskStream = new OutputStreamWriter(this.taskFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openStateFile () {
		openStateFile (true);
	}

	public void openStateFile (boolean append) {
		try{
			this.stateFile = w.openFileOutput(getActivityFileName(), (append)?ContextWrapper.MODE_APPEND:ContextWrapper.MODE_PRIVATE);
			this.stateStream = new OutputStreamWriter(this.stateFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeOnTaskFile (String graph) throws IOException {
		writeOnFile (this.taskStream, graph);
	}

	public void writeOnStateFile (String graph) throws IOException {
		writeOnFile (this.stateStream, graph);
	}

	public void closeTaskFile () {
		closeFile(this.taskFile, this.taskStream);
	}

	public void closeStateFile () {
		closeFile(this.stateFile, this.stateStream);
	}

	public boolean noTasks() {
		return (this.taskList.size()==0);
	}

	public void setTaskList(List<Trace> taskList) {
		this.taskList = taskList;
	}

	public String getActivityFileName() {
		return activityFile;
	}

	public void setActivityFile(String activityFile) {
		this.activityFile = activityFile;
	}

	public String getParametersFileName() {
		return parametersFile;
	}

	public void setParametersFile(String name) {
		this.parametersFile = name;
	}

	public String getTaskListFileName() {
		return taskListFile;
	}

	public void setTaskListFile(String taskListFile) {
		this.taskListFile = taskListFile;
	}

	public void registerListener (SaveStateListener listener) {
		theListeners.put(listener.getListenerName(), listener);
	}

}
