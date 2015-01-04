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

package it.slumdroid.tool.utilities.interactors;

import static it.slumdroid.droidmodels.model.InteractionType.SWAP_TAB;
import static it.slumdroid.tool.Resources.TAB_EVENTS_START_ONLY;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class TabSwapper.
 */
public class TabSwapper extends IterativeInteractorAdapter {

	/** The first. */
	private boolean first = true;

	/**
	 * Instantiates a new tab swapper.
	 *
	 * @param simpleTypes the simple types
	 */
	public TabSwapper (String ... simpleTypes) {
		super (simpleTypes);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInteractionType()
	 */
	public String getInteractionType () {
		this.first = false;
		return SWAP_TAB;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public boolean canUseWidget(WidgetState widget) {
		return super.canUseWidget(widget) && (isFirst() || !TAB_EVENTS_START_ONLY);
	}

	/**
	 * Checks if is first.
	 *
	 * @return true, if is first
	 */
	private boolean isFirst() {
		return this.first;
	}

}