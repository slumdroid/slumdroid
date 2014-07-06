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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;

import java.util.ArrayList;
import java.util.List;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetState;

public abstract class IterativeInteractorAdapter extends SimpleInteractorAdapter {

	private int maxEventsPerWidget = 0;

	public IterativeInteractorAdapter (String ... simpleTypes) {
		super (simpleTypes);
	}

	public IterativeInteractorAdapter (int maxItems, String ... simpleTypes) {
		this (simpleTypes);
		setMaxEventsPerWidget(maxItems);
	}

	@Override
	public List<UserEvent> getEvents (WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			final int fromItem = 1; 
			final int toItem = getToItem(w, fromItem, w.getCount()); 
			if (toItem<fromItem) return events;
			for (int i=fromItem; i<=toItem; i++) {
				events.add(generateEvent(w, String.valueOf(i)));
			}
		}
		return events;
	}

	public int getToItem(WidgetState w, int fromItem, int toItem) {
		return (getMaxEventsPerWidget(w)>0)?Math.min (fromItem + getMaxEventsPerWidget(w) - 1, toItem):toItem;
	}

	public int getMaxEventsPerWidget() {
		return this.maxEventsPerWidget;
	}

	public int getMaxEventsPerWidget(WidgetState w) {
		return getMaxEventsPerWidget();
	}

	public void setMaxEventsPerWidget(int maxEventsPerWidget) {
		this.maxEventsPerWidget = maxEventsPerWidget;
	}

}