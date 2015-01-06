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

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.xml.XmlGraph;
import it.slumdroid.tool.model.Persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.ContextWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class DiskPersistence.
 */
public class DiskPersistence implements Persistence {

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

	/** The file name. */
	private final String FILE_NAME = new String("guitree.xml"); 

	/**
	 * Instantiates a new disk persistence.
	 */
	public DiskPersistence () {
		// do Nothing
	}

	/**
	 * Instantiates a new disk persistence.
	 *
	 * @param theSession the the session
	 */
	public DiskPersistence (Session theSession) {
		this();
		setSession(theSession);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#setSession(it.slumdroid.droidmodels.model.Session)
	 */
	public void setSession(Session session) {
		this.theSession = session;
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
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName () {
		return this.FILE_NAME;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#setContext(android.app.Activity)
	 */
	public void setContext(Activity activity) {
		this.wrapper = new ContextWrapper(activity);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#addTask(it.slumdroid.droidmodels.model.Task)
	 */
	public void addTask(Task task) {
		this.theSession.addTask(task);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#save()
	 */
	public void save() {
		save (this.FILE_NAME);
	}

	/**
	 * Generate.
	 *
	 * @return the string
	 */
	protected String generate () {
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
		return graph;
	}

	/**
	 * Save.
	 *
	 * @param fileName the file name
	 */
	protected void save (String fileName) {
		String graph = generate();
		openFile(fileName);
		try {
			writeOnFile (graph);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}

	/**
	 * Write on file.
	 *
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOnFile (String graph) throws IOException {
		writeOnFile (this.osw, graph);
	}

	/**
	 * Write on file.
	 *
	 * @param output the output
	 * @param graph the graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOnFile (OutputStreamWriter output, String graph) throws IOException {
		output.write(graph);
	}

	/**
	 * Open file.
	 *
	 * @param fileName the file name
	 */
	public void openFile (String fileName) {
		try{
			this.fOut = this.wrapper.openFileOutput(fileName, this.mode);
			this.osw = new OutputStreamWriter(this.fOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public boolean delete (String fileName) {
		return this.wrapper.deleteFile(fileName);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Persistence#exists(java.lang.String)
	 */
	public boolean exists (String filename) {
		File file = this.wrapper.getFileStreamPath(filename);
		return file.exists();
	}

	/**
	 * Copy.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public void copy (String from, String to) {
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
	 * Close file.
	 */
	public void closeFile () {
		closeFile (this.fOut, this.osw);
	}

	/**
	 * Close file.
	 *
	 * @param theFile the the file
	 * @param theStream the the stream
	 */
	public void closeFile (FileOutputStream theFile, OutputStreamWriter theStream) {
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
	public void closeFile (OutputStream theFile, OutputStream theStream) {
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

}