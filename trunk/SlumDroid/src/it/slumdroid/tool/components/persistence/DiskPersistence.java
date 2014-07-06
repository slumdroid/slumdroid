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

import static it.slumdroid.tool.Resources.TAG;
import it.slumdroid.tool.model.ImageStorage;
import it.slumdroid.tool.model.Persistence;
import it.slumdroid.tool.model.SaveStateListener;

import java.io.*;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.Trace;
import it.slumdroid.droidmodels.xml.XmlGraph;

public class DiskPersistence implements Persistence, ImageStorage {

	FileOutputStream fOut = null; 
	OutputStreamWriter osw = null;
	ContextWrapper w = null;
	private String fileName;
	private Session theSession;
	protected int mode = ContextWrapper.MODE_PRIVATE;

	public DiskPersistence () {}

	public DiskPersistence (Session theSession) {
		this();
		setSession(theSession);
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	public void setSession(Session s) {
		this.theSession = s;
	}

	public Session getSession() {
		return this.theSession;
	}

	public String getFileName () {
		return this.fileName;
	}

	public void setContext(Activity a) {
		this.w = new ContextWrapper(a);
	}

	public void addTrace(Trace t) {
		this.theSession.addTrace(t);
	}

	public void save() {
		save (this.fileName);
	}

	protected String generate () {
		String graph = new String();
		try {
			if (this.theSession instanceof XmlGraph) {
				graph = ((XmlGraph)this.theSession).toXml();
			} else {
				graph = this.theSession.toString();
			}
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
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
			this.fOut = w.openFileOutput(fileName, this.mode);
			this.osw = new OutputStreamWriter(fOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean delete (String fileName) {
		return w.deleteFile(fileName);
	}

	public boolean exists (String filename) {
		File file = w.getFileStreamPath(filename);
		return file.exists();
	}

	public void copy (String from, String to) {
		FileInputStream in;
		FileOutputStream out;
		byte[] buffer = new byte[4096];

		try {
			in = w.openFileInput(from);
			out = w.openFileOutput(to, ContextWrapper.MODE_PRIVATE);
			int n = 0;

			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			} catch (IOException e) {
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void registerListener(SaveStateListener listener) { /* do nothing */ }

	public void saveImage(Bitmap image, String name) throws IOException {
		FileOutputStream fileOutput = null;
		OutputStreamWriter streamWriter = null;
		try {
			fileOutput = w.openFileOutput(name,ContextWrapper.MODE_PRIVATE);
			streamWriter = new OutputStreamWriter(fileOutput);
			if (fileOutput != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 90, fileOutput);
				Log.i(TAG,"Saved image on disk: " + name);
			}
		} catch (FileNotFoundException e) {
			Log.i(TAG,"Image is not on disk: " + name);
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