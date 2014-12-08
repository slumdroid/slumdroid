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

package it.slumdroid.utilities;

public class Resources {
	
	public static final String COV_GENERATOR = "%EMMA% report -r html -sp %APKPATH%\\src -Dreport.sort=+name -in ";
	public static final String DIET_DIR = "./diet/";
	public static final String GUITREE = "/guitree.xml";
	public static final String GUITREE_DIR = "./diet/guitree/";
	public static final String GUITREE_SUB = "guitree_"; 
	public static final String INCREMENTAL_COV = "%EMMA% report -r txt -Dreport.sort=+name -in ";
	public static final int MAX_ES = 100;
	public static final String TOOL = "it.slumdroid.tool";
	public static final String TOOL_TARGET = "19";
	
	public final static String NEW_LINE = System.getProperty("line.separator");
	public final static String BREAK = NEW_LINE + NEW_LINE;
	public final static String TAB = "\t";
	
}
