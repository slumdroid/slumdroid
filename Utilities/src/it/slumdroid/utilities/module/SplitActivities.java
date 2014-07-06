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

import java.io.File;

public class SplitActivities {

	public void splitA(String path, String folder) {
		if (!new File(folder).exists()) new File(folder).mkdir();
		String activityXml = path.concat("/activities.xml");
		File file = new File(activityXml);
		if (file.exists()) file.renameTo(new File(new File(folder), "activities_" + System.currentTimeMillis() + ".xml"));
		try {
			new File(activityXml).createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}    
	}

}
