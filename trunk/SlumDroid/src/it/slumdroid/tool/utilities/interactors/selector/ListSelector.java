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

package it.slumdroid.tool.utilities.interactors.selector;

import static it.slumdroid.droidmodels.model.InteractionType.LIST_SELECT;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.tool.Resources.MAX_NUM_EVENTS_PER_SELECTOR;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class ListSelector.
 */
public class ListSelector extends IterativeInteractorAdapter {

	/**
	 * Instantiates a new list selector.
	 */
	public ListSelector() {
		super(MAX_NUM_EVENTS_PER_SELECTOR, LIST_VIEW, PREFERENCE_LIST, EXPAND_MENU);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter#getToItem(it.slumdroid.droidmodels.model.WidgetState, int, int)
	 */
	@Override
	public int getToItem(WidgetState widget, int fromItem, int toItem) {
		if (widget.getSimpleType().equals(PREFERENCE_LIST) 
				|| widget.getSimpleType().equals(EXPAND_MENU)) {
			return toItem;
		}		
		return super.getToItem(widget,fromItem,toItem);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean canUseWidget(WidgetState widget) {
		return widget.isClickable() && super.canUseWidget(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInteractionType()
	 */
	public String getInteractionType() {
		return LIST_SELECT;
	}

}