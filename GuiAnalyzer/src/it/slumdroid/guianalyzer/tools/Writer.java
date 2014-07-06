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

package it.slumdroid.guianalyzer.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Writer {

	FileWriter fileOut;

	public Writer (String outputFileName) {

		try {
			fileOut = new FileWriter(outputFileName);
		} catch (IOException e) {
			System.err.println("Errore creating " + outputFileName + ".\n" + e.getMessage());
		}

	}

	public void write (String text) {

		try {
			fileOut.write(text);
			fileOut.flush();
		} catch (IOException e) {
			System.err.println("Error writing:\n" + text + "\n" + e.getMessage());
		}

	}

	public void close () {

		try {
			fileOut.close();
		} catch (IOException ex) {
			Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}