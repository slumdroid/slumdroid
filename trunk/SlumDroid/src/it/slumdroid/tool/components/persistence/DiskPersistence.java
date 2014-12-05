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

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.xml.XmlGraph;
import it.slumdroid.tool.model.ImageStorage;
import it.slumdroid.tool.model.Persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

public class DiskPersistence implements Persistence, ImageStorage {

	FileOutputStream fOut = null; 
	OutputStreamWriter osw = null;
	ContextWrapper wrapper = null;
	private Session theSession;
	protected int mode = ContextWrapper.MODE_PRIVATE;
	
	private final String FILE_NAME = new String("guitree.xml"); 

	public DiskPersistence () {
		// do nothing
	}

	public DiskPersistence (Session theSession) {
		this();
		setSession(theSession);
	}

	public void setSession(Session session) {
		this.theSession = session;
	}

	public Session getSession() {
		return this.theSession;
	}

	public String getFileName () {
		return this.FILE_NAME;
	}

	public void setContext(Activity activity) {
		this.wrapper = new ContextWrapper(activity);
	}

	public void addTask(Task task) {
		this.theSession.addTask(task);
	}

	public void save() {
		save (this.FILE_NAME);
	}

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

	public void writeOnFile (String graph) throws IOException {
		writeOnFile (this.osw, graph);
	}

	public void writeOnFile (OutputStreamWriter output, String graph) throws IOException {
		output.write(graph);
	}

	public void openFile (String fileName) {
		try{
			this.fOut = this.wrapper.openFileOutput(fileName, this.mode);
			this.osw = new OutputStreamWriter(this.fOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean delete (String fileName) {
		return this.wrapper.deleteFile(fileName);
	}

	public boolean exists (String filename) {
		File file = this.wrapper.getFileStreamPath(filename);
		return file.exists();
	}

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

	public void closeFile () {
		closeFile (this.fOut, this.osw);
	}

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

	public void saveImage(Bitmap image, String name) throws IOException {
		FileOutputStream fileOutput = null;
		OutputStreamWriter streamWriter = null;
		try {
			fileOutput = this.wrapper.openFileOutput(name,ContextWrapper.MODE_PRIVATE);
			streamWriter = new OutputStreamWriter(fileOutput);
			if (fileOutput != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 90, fileOutput);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutput != null) {
				streamWriter.close();
				fileOutput.close();
			}
		}
	}

	public String imageFormat() {
		return "jpg";
	}

}