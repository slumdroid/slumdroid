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

package it.slumdroid.androidtest.graphviz;

import java.util.Locale;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.Session;
import it.slumdroid.droidmodels.model.UserEvent;

public class DotUtilities {

	final static String EOL = System.getProperty("line.separator");
	final static String TAB = "\t";

	public static String getCaption (UserEvent event) {
		String type = event.getType();
		String target = event.getWidgetName();
		boolean special = event.getWidgetType().equals("null");
		if (target.equals(""))
			target = event.getDescription();
		if (target.equals(""))
			target = event.getValue();
		if (target.equals("")) {
			target = event.getWidgetType();
			if (!(event.getWidgetId().equals(""))) {
				target = target + " #" + event.getWidgetId();
			}
		}
		String nodeDesc = special?type:(type + " '" + escapeDot(target) + "'");
		return nodeDesc;

	}

	public static String escapeDot (String str) {
		if (str == null) {
			return null;
		}
		int sz = str.length();
		StringBuffer out = new StringBuffer(sz * 2);
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			// handle unicode
			if (ch < 32) {
				switch (ch) {
				case '\b' :
					out.append('\\');
					out.append('b');
					break;
				case '\n' :
					out.append('\\');
					out.append('n');
					break;
				case '\t' :
					out.append('\\');
					out.append('t');
					break;
				case '\f' :
					out.append('\\');
					out.append('f');
					break;
				case '\r' :
					out.append('\\');
					out.append('r');
					break;
				default :
					if (ch > 0xf) {
						out.append("\\u00").append(hex(ch));
					} else {
						out.append("\\u000").append(hex(ch));
					}
					break;
				}
			} else {
				switch (ch) {
				case '"' :
					out.append('\\');
					out.append('"');
					break;
				case '\\' :
					out.append('\\');
					out.append('\\');
					break;
				default :
					out.append(ch);
					break;
				}
			}
		}
		return out.toString();
	}

	public static String hex(char ch) {
		return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
	}

	public static String exportToDot (Session xml) {
		if (xml instanceof GuiTree) {
			GuiTreeToDot g = new GuiTreeToDot ((GuiTree)xml);
			return g.getDot();
		}
		return "";
	}

	public static String exportToFsm (Session xml){
		if (xml instanceof GuiTree) {
			GuiTreeToFSM g = new GuiTreeToFSM ((GuiTree)xml);
			return g.getDot();
		}
		return "";
	}	   	

}