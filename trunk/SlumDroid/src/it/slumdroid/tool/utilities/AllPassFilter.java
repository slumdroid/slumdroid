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

package it.slumdroid.tool.utilities;

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.Filter;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Class AllPassFilter.
 */
public class AllPassFilter implements Filter {

	/** The filtered items. */
	private ArrayList<WidgetState> filteredItems = new ArrayList<WidgetState>();

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<WidgetState> iterator() {
		return this.filteredItems.iterator();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Filter#loadItem(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public void loadItem (WidgetState widget) {
		this.filteredItems.add(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Filter#clear()
	 */
	@Override
	public void clear () {
		this.filteredItems.clear();
	}

}
