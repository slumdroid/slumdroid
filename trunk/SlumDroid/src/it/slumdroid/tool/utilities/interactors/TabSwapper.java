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

package it.slumdroid.tool.utilities.interactors;

import static it.slumdroid.droidmodels.model.InteractionType.SWAP_TAB;
import static it.slumdroid.tool.Resources.TAB_EVENTS_START_ONLY;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.IterativeInteractorAdapter;

public class TabSwapper extends IterativeInteractorAdapter {

	private boolean first = true;

	public TabSwapper (String ... simpleTypes) {
		super (simpleTypes);
	}

	public String getInteractionType () {
		this.first = false;
		return SWAP_TAB;
	}

	@Override
	public boolean canUseWidget(WidgetState widget) {
		return super.canUseWidget(widget) && (isFirst() || !TAB_EVENTS_START_ONLY);
	}

	private boolean isFirst() {
		return this.first;
	}

}