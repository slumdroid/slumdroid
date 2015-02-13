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

package it.slumdroid.utilities.module.androidtest.graphviz;

import it.slumdroid.droidmodels.guitree.GuiTree;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.utilities.module.androidtest.efg.EventFlowGraph;

import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class DotUtilities.
 */
public class DotUtilities {

	/**
	 * Gets the caption.
	 *
	 * @param event the event
	 * @return the caption
	 */
	public static String getCaption (UserEvent event) {
		String type = event.getType();
		String target = event.getWidgetName();
		boolean special = event.getWidgetType().equals("");
		if (target.equals("")) {
			target = event.getValue();
		}
		if (target.equals("")) {
			if (event.getWidget().getSimpleType() != null) {
				target = event.getWidget().getSimpleType();
			} else {
				target = event.getWidgetType();	
			}
			if (!event.getWidgetId().equals("")) {
				target = target + " #" + event.getWidgetId();
			}
		}
		String nodeDesc = special?type:(type + " '" + escapeDot(target) + "'");
		return nodeDesc;
	}

	/**
	 * Escape dot.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String escapeDot (String string) {
		if (string == null) {
			return null;
		}
		int size = string.length();
		StringBuffer out = new StringBuffer(size * 2);
		for (int i = 0; i < size; i++) {
			char character = string.charAt(i);
			// handle unicode
			if (character < 32) {
				switch (character) {
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
					if (character > 0xf) {
						out.append("\\u00").append(hex(character));
					} else {
						out.append("\\u000").append(hex(character));
					}
					break;
				}
			} else {
				switch (character) {
				case '"' :
					out.append('\\');
					out.append('"');
					break;
				case '\\' :
					out.append('\\');
					out.append('\\');
					break;
				default :
					out.append(character);
					break;
				}
			}
		}
		return out.toString();
	}

	/**
	 * Hex.
	 *
	 * @param character the character
	 * @return the string
	 */
	public static String hex(char character) {
		return Integer.toHexString(character).toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Export to dot.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	public static String exportToDot (GuiTree xml) {
		return new GuiTreeToDot (xml).getDot();
	}

	/**
	 * Export to fsm.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	public static String exportToFsm (GuiTree xml) {
		return new GuiTreeToFSM (xml).getDot();
	}

	/**
	 * Export to efg.
	 *
	 * @param efg the efg
	 * @return the string
	 */
	public static String exportToEfg (EventFlowGraph efg) {
		return new GuiTreeToEfgDot (efg).getDot();
	}

}