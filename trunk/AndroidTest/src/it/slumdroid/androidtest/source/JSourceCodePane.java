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

package it.slumdroid.androidtest.source;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class JSourceCodePane extends JTabbedPane implements Iterable<Component> {

	public JSourceCodePane () {
		this.areas = new HashMap<String, Component>();
	}

	public void addEnabled (String s, Icon i, Component c) {
		addTab (s,i,c,true);
	}

	public void addDisabled (String s, Icon i, Component c) {
		addTab (s,i,c,false);
	}

	public void addTab (String s, Icon i, Component c, boolean enabled) {
		JScrollPane scroll = new JScrollPane(c);
		addTab(s,i,scroll);
		areas.put(s,c);
		setEnabledAt(indexOfComponent(scroll), enabled);
	}

	public void enableTab (String s) {
		setEnabledAt(indexOfTab(s), true);
	}

	public void disableTab (String s) {
		setEnabledAt(indexOfTab(s), false);
	}

	public void showCode (String tab, String code) {
		enableTab(tab);
		Component c = areas.get(tab);
		if (c instanceof JSourceCodeArea)
			((JSourceCodeArea) c).initText(code);
	}

	public void setFileName (String tab, String name) {
		Component c = areas.get(tab);
		if (c instanceof JSourceCodeArea)
			((JSourceCodeArea) c).setDefaultFileName(name);
	}

	public Iterator<Component> iterator() {
		return new ArrayList<Component>(this.areas.values()).iterator();
	}

	private HashMap<String,Component> areas;

}
