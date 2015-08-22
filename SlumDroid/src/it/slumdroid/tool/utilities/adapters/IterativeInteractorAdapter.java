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

import java.util.ArrayList;
import java.util.List;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetState;

// TODO: Auto-generated Javadoc
/**
 * The Class IterativeInteractorAdapter.
 */
public abstract class IterativeInteractorAdapter extends SimpleInteractorAdapter {

	/** The max events per widget. */
	private int maxEventsPerWidget = 0;

	/**
	 * Instantiates a new iterative interactor adapter.
	 *
	 * @param simpleTypes the simple types
	 */
	public IterativeInteractorAdapter(String ... simpleTypes) {
		super(simpleTypes);
	}

	/**
	 * Instantiates a new iterative interactor adapter.
	 *
	 * @param maxItems the max items
	 * @param simpleTypes the simple types
	 */
	public IterativeInteractorAdapter(int maxItems, String ... simpleTypes) {
		this(simpleTypes);
		setMaxEventsPerWidget(maxItems);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getEvents(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public List<UserEvent> getEvents(WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			final int fromItem = 1; 
			final int toItem = getToItem(widget, fromItem, widget.getCount()); 
			if (toItem < fromItem) {
				return events;
			}
			for (int item = fromItem; item <= toItem; item++) {
				events.add(generateEvent(widget, String.valueOf(item)));
			}
		}
		return events;
	}

	/**
	 * Gets the to item.
	 *
	 * @param widget the widget
	 * @param fromItem the from item
	 * @param toItem the to item
	 * @return the to item
	 */
	public int getToItem(WidgetState widget, int fromItem, int toItem) {
		return (getMaxEventsPerWidget(widget) > 0)?Math.min (fromItem + getMaxEventsPerWidget(widget) - 1, toItem):toItem;
	}

	/**
	 * Gets the max events per widget.
	 *
	 * @return the max events per widget
	 */
	public int getMaxEventsPerWidget() {
		return this.maxEventsPerWidget;
	}

	/**
	 * Gets the max events per widget.
	 *
	 * @param widget the widget
	 * @return the max events per widget
	 */
	public int getMaxEventsPerWidget(WidgetState widget) {
		return getMaxEventsPerWidget();
	}

	/**
	 * Sets the max events per widget.
	 *
	 * @param maxEventsPerWidget the new max events per widget
	 */
	public void setMaxEventsPerWidget(int maxEventsPerWidget) {
		this.maxEventsPerWidget = maxEventsPerWidget;
	}

}