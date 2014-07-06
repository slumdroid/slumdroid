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

import java.io.BufferedReader;
import java.io.FileReader;

public class CountEvents {

	public void countEvents(String path) {
		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			int count = 0;
			for (;;) {
				String line = inputStream1.readLine();
				if (line==null){
					System.out.println("Performed " + count + " events");
					inputStream1.close();
					return;
				}
				else if (line.contains("Firing event:")) count++;	 			 	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
