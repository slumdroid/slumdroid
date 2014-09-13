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

package it.slumdroid.androidtest;

public class Resources {

	public static final int DEFAULT_WIDTH = 650;
	public static final int DEFAULT_HEIGHT = 500;
	public final static String GUI_TREE = "GUI Tree (XML)";
	public final static String GUI_TREE_DOT = "Gui Tree (DOT)";
	public final static String FSM_DOT = "FSM (DOT)";
	public final static String TEST_REPORT = "Report";
	public final static String GUI_TREE_FILE_TYPE = "XML Gui Tree";

	public static void getTimestamp (String label) {
		java.util.Date d = new java.util.Date();
		java.sql.Timestamp t = new java.sql.Timestamp (d.getTime());
		System.out.println(label + t.toString());
	}

}
