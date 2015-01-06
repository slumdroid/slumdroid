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

package it.slumdroid.tool.components.persistence;

import it.slumdroid.droidmodels.guitree.FinalActivity;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.tool.model.DispatchListener;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.StateDiscoveryListener;
import it.slumdroid.tool.utilities.SessionParams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContextWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class ResumingPersistence.
 */
public class ResumingPersistence extends StepDiskPersistence implements DispatchListener, StateDiscoveryListener {

	/** The task list. */
	private List<Task> taskList;

	/** The activity file. */
	private String activityFile;

	/** The task list file. */
	private String taskListFile;

	/** The parameters file. */
	private String parametersFile;

	/** The task file. */
	private FileOutputStream taskFile;

	/** The task stream. */
	private OutputStreamWriter taskStream;

	/** The state file. */
	private FileOutputStream stateFile;

	/** The state stream. */
	private OutputStreamWriter stateStream;

	/** The parameters. */
	private Map<String, SessionParams> parameters = new Hashtable<String, SessionParams>();

	/** The listeners. */
	private Hashtable<String,SaveStateListener> theListeners = new Hashtable<String,SaveStateListener>();

	/**
	 * Instantiates a new resuming persistence.
	 */
	public ResumingPersistence () {
		super(1);
	}

	/**
	 * Instantiates a new resuming persistence.
	 *
	 * @param theSession the the session
	 */
	public ResumingPersistence (Session theSession) {
		super(theSession, 1);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.StepDiskPersistence#addTask(it.slumdroid.droidmodels.model.Task)
	 */
	@Override 
	public void addTask (Task t) {
		t.setFailed(false);
		super.addTask(t);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.DispatchListener#onTaskDispatched(it.slumdroid.droidmodels.model.Task)
	 */
	public void onTaskDispatched(Task task) {
		task.setFailed(true);
		saveTaskList();
		saveParameters();
	}

	/**
	 * Save task list.
	 */
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
			for (Task task: this.taskList) {
				String xml = ((ElementWrapper)task).toXml() + System.getProperty("line.separator");
				writeOnTaskFile(xml);
			}
			closeTaskFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (exists(backup(this.taskListFile))) {
			delete (backup(this.taskListFile));
		}
		if (exists(backup(this.activityFile))) {
			delete (backup(this.activityFile));
		}
	}

	/**
	 * Can has resume.
	 *
	 * @return true, if successful
	 */
	public boolean canHasResume () {
		if (!exists(getFileName())) {
			return false; 
		}
		if (!exists(getActivityFileName())) {
			throw new Error("Cannot resume previous session: state list not found.");
		}
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

	/**
	 * Backup file.
	 *
	 * @param fileName the file name
	 */
	public void backupFile (String fileName) {
		copy(fileName,backup(fileName));
	}

	/**
	 * Restore file.
	 *
	 * @param fileName the file name
	 */
	public void restoreFile (String fileName) {
		copy(backup(fileName),fileName);
	}	

	/**
	 * Backup.
	 *
	 * @param original the original
	 * @return the string
	 */
	public String backup (String original) {
		return original + ".bak";
	}

	/**
	 * Save parameters.
	 */
	public void saveParameters() {
		parameters.clear();
		FileOutputStream theFile = null;
		ObjectOutputStream theStream = null;
		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			parameters.put(listener.getKey(), listener.getValue().onSavingState());
		}
		try {
			theFile = wrapper.openFileOutput(getParametersFileName(), ContextWrapper.MODE_PRIVATE);
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

	/**
	 * Load parameters.
	 */
	@SuppressWarnings("unchecked")
	public void loadParameters() {
		FileInputStream theFile = null;
		ObjectInputStream theStream = null;
		try {
			theFile = wrapper.openFileInput(getParametersFileName());
			theStream = new ObjectInputStream(theFile);
			this.parameters = (Map<String, SessionParams>) theStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theFile.close();
				theStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			listener.getValue().onLoadingState(this.parameters.get(listener.getKey()));
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.StateDiscoveryListener#onNewState(it.slumdroid.droidmodels.model.ActivityState)
	 */
	public void onNewState(ActivityState newState) {
		if (exists(getActivityFileName())) {
			backupFile (getActivityFileName());
		}
		try {
			openStateFile(newState instanceof FinalActivity); 
			String xml = ((ElementWrapper)newState).toXml() + System.getProperty("line.separator");
			writeOnStateFile(xml);
			closeStateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.StepDiskPersistence#save()
	 */
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
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.StepDiskPersistence#isLast()
	 */
	@Override
	public boolean isLast() {
		return super.isLast() && noTasks();
	}

	/**
	 * On terminate.
	 */
	public void onTerminate() {
		this.taskList.clear();
	}

	/**
	 * Read file.
	 *
	 * @param input the input
	 * @return the list
	 */
	private List<String> readFile (String input) {
		FileInputStream theFile = null;
		BufferedReader theStream = null;
		String line = new String();
		List<String> output = new ArrayList<String>();
		try{
			theFile = wrapper.openFileInput (input);
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			while ( (line = theStream.readLine()) != null) {
				output.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theFile.close();
				theStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output;
	} 

	/**
	 * Read task file.
	 *
	 * @return the list
	 */
	public List<String> readTaskFile () {
		return readFile(getTaskListFileName());
	}

	/**
	 * Read state file.
	 *
	 * @return the list
	 */
	public List<String> readStateFile () {
		return readFile(getActivityFileName());
	}

	/**
	 * Open task file.
	 */
	public void openTaskFile () {
		try{
			this.taskFile = wrapper.openFileOutput(getTaskListFileName(), ContextWrapper.MODE_PRIVATE);
			this.taskStream = new OutputStreamWriter(this.taskFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open state file.
	 */
	public void openStateFile () {
		openStateFile (true);
	}

	/**
	 * Open state file.
	 *
	 * @param append the append
	 */
	public void openStateFile (boolean append) {
		try{
			this.stateFile = wrapper.openFileOutput(getActivityFileName(), (append)?ContextWrapper.MODE_APPEND:ContextWrapper.MODE_PRIVATE);
			this.stateStream = new OutputStreamWriter(this.stateFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write on task file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOnTaskFile (String graph) throws IOException {
		writeOnFile (this.taskStream, graph);
	}

	/**
	 * Write on state file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOnStateFile (String graph) throws IOException {
		writeOnFile (this.stateStream, graph);
	}

	/**
	 * Close task file.
	 */
	public void closeTaskFile () {
		closeFile(this.taskFile, this.taskStream);
	}

	/**
	 * Close state file.
	 */
	public void closeStateFile () {
		closeFile(this.stateFile, this.stateStream);
	}

	/**
	 * No tasks.
	 *
	 * @return true, if successful
	 */
	public boolean noTasks() {
		return this.taskList.size() == 0;
	}

	/**
	 * Sets the task list.
	 *
	 * @param taskList the new task list
	 */
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	/**
	 * Gets the activity file name.
	 *
	 * @return the activity file name
	 */
	public String getActivityFileName() {
		return activityFile;
	}

	/**
	 * Sets the activity file.
	 *
	 * @param activityFile the new activity file
	 */
	public void setActivityFile(String activityFile) {
		this.activityFile = activityFile;
	}

	/**
	 * Gets the parameters file name.
	 *
	 * @return the parameters file name
	 */
	public String getParametersFileName() {
		return parametersFile;
	}

	/**
	 * Sets the parameters file.
	 *
	 * @param name the new parameters file
	 */
	public void setParametersFile(String name) {
		this.parametersFile = name;
	}

	/**
	 * Gets the task list file name.
	 *
	 * @return the task list file name
	 */
	public String getTaskListFileName() {
		return taskListFile;
	}

	/**
	 * Sets the task list file.
	 *
	 * @param taskListFile the new task list file
	 */
	public void setTaskListFile(String taskListFile) {
		this.taskListFile = taskListFile;
	}

	/**
	 * Register listener.
	 *
	 * @param listener the listener
	 */
	public void registerListener (SaveStateListener listener) {
		theListeners.put(listener.getListenerName(), listener);
	}

}
