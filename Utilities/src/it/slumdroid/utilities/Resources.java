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

package it.slumdroid.utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Resources.
 */
public class Resources {

	/** The Constant COV_GENERATOR. */
	public static final String COV_GENERATOR = "%EMMA% report -r html -sp %APKPATH%\\src -Dreport.sort=+name -in ";

	/** The Constant DIET_DIR. */
	public static final String DIET_DIR = "./diet/";

	/** The Constant GUITREE. */
	public static final String GUITREE = "/guitree.xml";

	/** The Constant GUITREE_DIR. */
	public static final String GUITREE_DIR = "./diet/guitree/";

	/** The Constant GUITREE_SUB. */
	public static final String GUITREE_SUB = "guitree_"; 

	/** The Constant INCREMENTAL_COV. */
	public static final String INCREMENTAL_COV = "%EMMA% report -r txt -Dreport.sort=+name -in ";

	/** The Constant MAX_ES. */
	public static final int MAX_ES = 100;

	/** The Constant TOOL. */
	public static final String TOOL = "it.slumdroid.tool";

	/** The Constant TOOL_TARGET. */
	public static final String TOOL_TARGET = "19";

	/** The Constant NEW_LINE. */
	public final static String NEW_LINE = System.getProperty("line.separator");

	/** The Constant BREAK. */
	public final static String BREAK = NEW_LINE + NEW_LINE;

	/** The Constant TAB. */
	public final static String TAB = "\t";

}
