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

import static it.slumdroid.droidmodels.model.InteractionType.LIST_LONG_SELECT;
import it.slumdroid.droidmodels.model.WidgetState;

// TODO: Auto-generated Javadoc
/**
 * The Class ListLongSelector.
 */
public class ListLongSelector extends ListSelector {

	/**
	 * Instantiates a new list long selector.
	 *
	 * @param maxItems the max items
	 * @param simpleTypes the simple types
	 */
	public ListLongSelector (int maxItems, String ... simpleTypes) {
		super (maxItems, simpleTypes);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.interactors.selector.ListSelector#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean canUseWidget (WidgetState widget) {
		return widget.isLongClickable() && super.canUseWidget(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.interactors.selector.ListSelector#getInteractionType()
	 */
	public String getInteractionType () {
		return LIST_LONG_SELECT;
	}

}
