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

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CovGenerator {

	public void covGenerator() { 
		PrintWriter out = null;
		try {
			out = new PrintWriter ("../coverage/Copertura.bat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String esFiles = new Tools().coverage(out);
		out.print("%EMMA% report -r html -sp %APKPATH%\\src -Dreport.sort=+name -in " + esFiles);
		out.close();
	}

}
