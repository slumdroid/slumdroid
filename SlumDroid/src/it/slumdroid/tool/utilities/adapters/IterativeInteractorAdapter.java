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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.ArrayList;
import java.util.List;

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
	public List<UserEvent> getEvents (WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			final int fromItem = 1; 
			final int toItem = getToItem(widget, fromItem, widget.getCount()); 
			if (toItem < fromItem) return events;
			for (int item = fromItem; item <= toItem; item++) {
				events.add(generateEvent(widget, String.valueOf(item)));
			}
		}
		return events;
	}

	public int getToItem(WidgetState widget, int fromItem, int toItem) {
		return (getMaxEventsPerWidget(widget) > 0)?Math.min (fromItem + getMaxEventsPerWidget(widget) - 1, toItem):toItem;
	}

	public int getMaxEventsPerWidget() {
		return this.maxEventsPerWidget;
	}

	public int getMaxEventsPerWidget(WidgetState widget) {
		return getMaxEventsPerWidget();
	}

	public void setMaxEventsPerWidget(int maxEventsPerWidget) {
		this.maxEventsPerWidget = maxEventsPerWidget;
	}

}