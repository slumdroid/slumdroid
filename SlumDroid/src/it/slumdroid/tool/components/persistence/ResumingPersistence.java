/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.components.persistence;

import static it.slumdroid.tool.Resources.*;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.view.View;
import it.slumdroid.droidmodels.guitree.FinalActivity;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.xml.ElementWrapper;
import it.slumdroid.droidmodels.xml.XmlGraph;
import it.slumdroid.tool.components.automation.Automation;
import it.slumdroid.tool.model.DispatchListener;
import it.slumdroid.tool.model.SaveStateListener;
import it.slumdroid.tool.model.StateDiscoveryListener;
import it.slumdroid.tool.utilities.SessionParams;

// TODO: Auto-generated Javadoc
/**
 * The Class ResumingPersistence.
 */
public class ResumingPersistence implements SaveStateListener, DispatchListener, StateDiscoveryListener {

	/** The actor name. */
	public final String ACTOR_NAME = "ResumingPersistence";

	/** The param name. */
	public final String PARAM_NAME = "resumer";

	/** The xml body begin. */
	private final String XML_BODY_BEGIN = "    <TASK";

	/** The xml body end. */
	private final String XML_BODY_END = "/TASK>";

	/** The task file. */
	private FileOutputStream taskFile;

	/** The task list. */
	private List<Task> taskList;

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

	/** The footer. */
	private String footer = new String();

	/** The first. */
	private boolean first = true;

	/** The last. */
	private boolean last = false;

	/** The out. */
	FileOutputStream fOut = null; 

	/** The osw. */
	OutputStreamWriter osw = null;

	/** The wrapper. */
	ContextWrapper wrapper = null;

	/** The session. */
	private Session theSession;

	/** The mode. */
	protected int mode = ContextWrapper.MODE_PRIVATE;

	/**
	 * Instantiates a new resuming persistence.
	 *
	 * @param theSession the the session
	 */
	public ResumingPersistence(Session theSession) {
		setSession(theSession);
	}

	/**
	 * Adds the task.
	 *
	 * @param task the task
	 */
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.StepDiskPersistence#addTask(it.slumdroid.droidmodels.model.Task)
	 */ 
	public void addTask(Task task) {
		task.setFailed(false);
		getSession().addTask(task);
		saveStep();
	}

	/**
	 * Backup.
	 *
	 * @param original the original
	 * @return the string
	 */
	private String backup(String original) {
		return original + ".bak";
	}

	/**
	 * Backup file.
	 *
	 * @param fileName the file name
	 */
	private void backupFile(String fileName) {
		copy(fileName, backup(fileName));
	}

	/**
	 * Can has resume.
	 *
	 * @return true, if successful
	 */
	public boolean canHasResume() {
		if (!exists(GUI_TREE_FILE_NAME)) {
			return false; 
		}
		if (exists(backup(TASK_LIST_FILE_NAME))) {
			restoreFile(TASK_LIST_FILE_NAME);
			if (exists(backup(ACTIVITY_LIST_FILE_NAME))) {
				restoreFile(ACTIVITY_LIST_FILE_NAME);
			}
		}
		if (!exists(TASK_LIST_FILE_NAME)) {
			return false;
		}
		return true;
	}

	/**
	 * Capture image.
	 *
	 * @return the bitmap
	 */
	private Bitmap captureImage() {
		ArrayList<View> views = Automation.getRobotium().getViews();
		Bitmap source = null;
		Bitmap bitmap = null;
		try{
			if (views != null && views.size() > 0) {
				final View view = views.get(0);
				view.destroyDrawingCache();
				view.buildDrawingCache(false);
				source = view.getDrawingCache();
				if (source == null) {
					return null;
				}
				Bitmap.Config config = source.getConfig();
				if (config == null){
					config = Bitmap.Config.ARGB_8888;
				}
				bitmap = source.copy(config, false);
				source.recycle();
				view.destroyDrawingCache();
				return bitmap;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Close file.
	 */
	private void closeFile() {
		closeFile (this.fOut, this.osw);
	}

	/**
	 * Close file.
	 *
	 * @param theFile the the file
	 * @param theStream the the stream
	 */
	private void closeFile(FileOutputStream theFile, OutputStreamWriter theStream) {
		try {
			theStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theStream.close();
				theFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close file.
	 *
	 * @param theFile the the file
	 * @param theStream the the stream
	 */
	private void closeFile(OutputStream theFile, OutputStream theStream) {
		try {
			theStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				theStream.close();
				theFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close state file.
	 */
	private void closeStateFile() {
		closeFile(this.stateFile, this.stateStream);
	}

	/**
	 * Close task file.
	 */
	private void closeTaskFile() {
		closeFile(this.taskFile, this.taskStream);
	}

	/**
	 * Copy.
	 *
	 * @param from the from
	 * @param to the to
	 */
	private void copy(String from, String to) {
		FileInputStream in = null;
		FileOutputStream out = null;
		byte[] buffer = new byte[4096];
		try {
			in = this.wrapper.openFileInput(from);
			out = this.wrapper.openFileOutput(to, ContextWrapper.MODE_PRIVATE);
			int reader = 0;
			while ((reader = in.read(buffer)) != -1) {
				out.write(buffer, 0, reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Delete.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	private boolean delete(String fileName) {
		return this.wrapper.deleteFile(fileName);
	}

	/**
	 * Exists.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#exists(java.lang.String)
	 */
	public boolean exists(String filename) {
		return this.wrapper.getFileStreamPath(filename).exists();
	}

	/**
	 * Generate.
	 *
	 * @return the string
	 */
	private String generate() {
		String graph = new String();
		try {
			if (this.theSession instanceof XmlGraph) {
				graph = ((XmlGraph)this.theSession).toXml();
			} else {
				graph = this.theSession.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isFirst() && isLast()) {
			return graph;
		}
		int bodyBegin = graph.indexOf(XML_BODY_BEGIN);
		int bodyEnd = graph.lastIndexOf(XML_BODY_END) + XML_BODY_END.length();

		if (isFirst()) {
			this.footer = graph.substring(bodyEnd);
			return graph.substring(0, bodyEnd);
		}

		if (isLast()) {
			return (bodyBegin == -1)?(this.footer):graph.substring(bodyBegin);
		}
		if ((bodyBegin == -1) 
				|| (bodyEnd == -1)) { // Empty body
			return new String();
		}

		return graph.substring(bodyBegin, bodyEnd) + System.getProperty("line.separator");
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#getListenerName()
	 */
	public String getListenerName() {
		return ACTOR_NAME;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return this.theSession;
	}

	/**
	 * Checks if is first.
	 *
	 * @return true, if is first
	 */
	private boolean isFirst() {
		return this.first;
	}

	/**
	 * Checks if is last.
	 *
	 * @return true, if is last
	 */
	private boolean isLast() {
		return this.last && noTasks();
	}

	/**
	 * Load parameters.
	 */
	@SuppressWarnings("unchecked")
	public void loadParameters() {
		FileInputStream theFile = null;
		ObjectInputStream theStream = null;
		try {
			theFile = wrapper.openFileInput(PARAMETERS_FILE_NAME);
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

	/**
	 * No tasks.
	 *
	 * @return true, if successful
	 */
	private boolean noTasks() {
		return this.taskList.size() == 0;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.SaveStateListener#onLoadingState(it.slumdroid.tool.utilities.SessionParams)
	 */
	public void onLoadingState(SessionParams sessionParams) {
		this.footer = sessionParams.get(PARAM_NAME);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.StateDiscoveryListener#onNewState(it.slumdroid.droidmodels.model.ActivityState)
	 */
	public void onNewState(ActivityState newState) {
		if (exists(ACTIVITY_LIST_FILE_NAME)) {
			backupFile(ACTIVITY_LIST_FILE_NAME);
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
	 * @see it.slumdroid.tool.model.SaveStateListener#onSavingState()
	 */
	@Override
	public SessionParams onSavingState() {
		return new SessionParams(PARAM_NAME, this.footer);
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
	 * Open file.
	 *
	 * @param fileName the file name
	 */
	private void openFile(String fileName) {
		try{
			this.fOut = this.wrapper.openFileOutput(fileName, this.mode);
			this.osw = new OutputStreamWriter(this.fOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open state file.
	 *
	 * @param append the append
	 */
	private void openStateFile(boolean append) {
		try{
			this.stateFile = wrapper.openFileOutput(ACTIVITY_LIST_FILE_NAME, (append)?ContextWrapper.MODE_APPEND:ContextWrapper.MODE_PRIVATE);
			this.stateStream = new OutputStreamWriter(this.stateFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open task file.
	 */
	private void openTaskFile() {
		try{
			this.taskFile = wrapper.openFileOutput(TASK_LIST_FILE_NAME, ContextWrapper.MODE_PRIVATE);
			this.taskStream = new OutputStreamWriter(this.taskFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read file.
	 *
	 * @param input the input
	 * @return the list
	 */
	private List<String> readFile(String input) {
		FileInputStream theFile = null;
		BufferedReader theStream = null;
		String line = new String();
		List<String> output = new ArrayList<String>();
		try{
			theFile = wrapper.openFileInput(input);
			theStream = new BufferedReader(new FileReader(theFile.getFD()));
			while ((line = theStream.readLine()) != null) {
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
	 * Read state file.
	 *
	 * @return the list
	 */
	public List<String> readStateFile() {
		return readFile(ACTIVITY_LIST_FILE_NAME);
	}

	/**
	 * Read task file.
	 *
	 * @return the list
	 */
	public List<String> readTaskFile() {
		return readFile(TASK_LIST_FILE_NAME);
	}

	/**
	 * Register listener.
	 *
	 * @param listener the listener
	 */
	public void registerListener(SaveStateListener listener) {
		theListeners.put(listener.getListenerName(), listener);
	}

	/**
	 * Restore file.
	 *
	 * @param fileName the file name
	 */
	private void restoreFile(String fileName) {
		copy(backup(fileName), fileName);
	}

	/**
	 * Save.
	 */
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.components.persistence.StepDiskPersistence#save()
	 */
	public void save() {
		save(true);
		saveParameters();
		saveTaskList();
		if (noTasks()) { 
			delete(backup(ACTIVITY_LIST_FILE_NAME));
			delete(PARAMETERS_FILE_NAME);
			delete(backup(PARAMETERS_FILE_NAME));
			delete(TASK_LIST_FILE_NAME);
			delete(backup(TASK_LIST_FILE_NAME));
		}
	}

	/**
	 * Save.
	 *
	 * @param last the last
	 */
	private void save(boolean last) {
		if (!isFirst()) {
			this.mode = ContextWrapper.MODE_APPEND;
		}
		if (last) {
			setLast();		
		}
		save(GUI_TREE_FILE_NAME);
	}

	/**
	 * Save.
	 *
	 * @param fileName the file name
	 */
	protected void save(String fileName) {
		String graph = generate();
		openFile(fileName);
		try {
			writeOnFile(graph);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}

	/**
	 * Save image.
	 *
	 * @param image the image
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveImage(Bitmap image, String name) throws IOException {
		FileOutputStream fileOutput = null;
		OutputStreamWriter streamWriter = null;
		try {
			fileOutput = wrapper.openFileOutput(name, ContextWrapper.MODE_PRIVATE);
			streamWriter = new OutputStreamWriter(fileOutput);
			if (fileOutput != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 50, fileOutput);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fileOutput != null) {
				streamWriter.close();
				fileOutput.close();
			}
		}
	}

	/**
	 * Save parameters.
	 */
	private void saveParameters() {
		parameters.clear();
		FileOutputStream theFile = null;
		ObjectOutputStream theStream = null;
		for (Entry<String, SaveStateListener> listener: this.theListeners.entrySet()) {
			parameters.put(listener.getKey(), listener.getValue().onSavingState());
		}
		try {
			theFile = wrapper.openFileOutput(PARAMETERS_FILE_NAME, ContextWrapper.MODE_PRIVATE);
			theStream = new ObjectOutputStream(theFile);
			theStream.writeObject(this.parameters);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (theFile != null && theStream != null) {
				closeFile(theFile, theStream);
			}
		}
	}

	/**
	 * Save screenshot.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public boolean saveScreenshot(String fileName) {
		Bitmap bitmap = captureImage();
		if (bitmap == null) {
			return false;	
		}
		try {
			saveImage(bitmap, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;            
	}

	/**
	 * Save step.
	 */
	public void saveStep() {
		save(isLast());
		for (Task task: getSession()) {
			getSession().removeTask(task);
		}
		setNotFirst();
	}

	/**
	 * Save task list.
	 */
	private void saveTaskList() {
		if (noTasks()) {
			delete(TASK_LIST_FILE_NAME);
			return;
		}
		if (exists(TASK_LIST_FILE_NAME)) {
			backupFile(TASK_LIST_FILE_NAME);
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
		if (exists(backup(TASK_LIST_FILE_NAME))) {
			delete(backup(TASK_LIST_FILE_NAME));
		}
		if (exists(backup(ACTIVITY_LIST_FILE_NAME))) {
			delete(backup(ACTIVITY_LIST_FILE_NAME));
		}
	}

	/**
	 * Sets the context.
	 *
	 * @param activity the new context
	 */
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#setContext(android.app.Activity)
	 */
	public void setContext(Activity activity) {
		this.wrapper = new ContextWrapper(activity);
	}

	/**
	 * Sets the last.
	 */
	private void setLast() {
		this.last = true;
	}

	/**
	 * Sets the not first.
	 */
	public void setNotFirst() {
		this.first = false;
	}

	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#setSession(it.slumdroid.droidmodels.model.Session)
	 */
	public void setSession(Session session) {
		this.theSession = session;
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
	 * Write on file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeOnFile(String graph) throws IOException {
		writeOnFile(this.osw, graph);
	}

	/**
	 * Write on file.
	 *
	 * @param output the output
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeOnFile(OutputStreamWriter output, String graph) throws IOException {
		output.write(graph);
	}

	/**
	 * Write on state file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeOnStateFile(String graph) throws IOException {
		writeOnFile(this.stateStream, graph);
	}

	/**
	 * Write on task file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeOnTaskFile(String graph) throws IOException {
		writeOnFile(this.taskStream, graph);
	}

}
