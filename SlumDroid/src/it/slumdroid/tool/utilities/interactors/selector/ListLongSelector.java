/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.utilities.interactors.selector;

import static it.slumdroid.droidmodels.model.InteractionType.LIST_LONG_SELECT;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.tool.Resources.MAX_NUM_EVENTS_PER_SELECTOR;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class ListLongSelector.
 */
public class ListLongSelector extends IterativeInteractorAdapter {

	/**
	 * Instantiates a new list long selector.
	 */
	public ListLongSelector() {
		super(MAX_NUM_EVENTS_PER_SELECTOR, LIST_VIEW, EXPAND_LIST);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.interactors.selector.ListSelector#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean canUseWidget(WidgetState widget) {
		return widget.isLongClickable() && super.canUseWidget(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.interactors.selector.ListSelector#getInteractionType()
	 */
	public String getInteractionType() {
		return LIST_LONG_SELECT;
	}

}
