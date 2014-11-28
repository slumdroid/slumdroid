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

package it.slumdroid.tool.utilities.interactors.selector;

import static it.slumdroid.droidmodels.model.InteractionType.SPINNER_SELECT;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.RandomInteractorAdapter;

public class RandomSpinnerSelector extends RandomInteractorAdapter {

	public RandomSpinnerSelector (String ... simpleTypes) {
		super (simpleTypes);
	}

	public String getInteractionType() {
		return SPINNER_SELECT;
	}

	@Override
	public boolean canUseWidget (WidgetState widget) {
		if (getMin(widget) > getMax(widget)) return false;
		return super.canUseWidget(widget);
	}

	@Override
	public int getMin () {
		return 1;
	}

	@Override
	public int getMax(WidgetState widget) {
		return widget.getCount();
	}

}
