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

package it.slumdroid.tool.utilities;

import it.slumdroid.tool.model.Filter;

import java.util.ArrayList;
import java.util.Iterator;

import it.slumdroid.droidmodels.model.WidgetState;

public class AllPassFilter implements Filter {

	private ArrayList<WidgetState> filteredItems = new ArrayList<WidgetState>();

	@Override
	public Iterator<WidgetState> iterator() {
		return this.filteredItems.iterator();
	}

	@Override
	public void loadItem(WidgetState w) {
		this.filteredItems.add(w);
	}

	@Override
	public void clear () {
		this.filteredItems.clear();
	}

}
