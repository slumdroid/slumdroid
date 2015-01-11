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

import static it.slumdroid.droidmodels.model.InteractionType.SPINNER_SELECT;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER_INPUT;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.RandomInteractorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class RandomSpinnerSelector.
 */
public class RandomSpinnerSelector extends RandomInteractorAdapter {

	/**
	 * Instantiates a new random spinner selector.
	 */
	public RandomSpinnerSelector () {
		super (SPINNER_INPUT);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInteractionType()
	 */
	public String getInteractionType() {
		return SPINNER_SELECT;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public boolean canUseWidget (WidgetState widget) {
		if (getMin(widget) > getMax(widget)) {
			return false;
		}
		return super.canUseWidget(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.RandomInteractorAdapter#getMin()
	 */
	@Override
	public int getMin () {
		return 1;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.RandomInteractorAdapter#getMax(it.slumdroid.droidmodels.model.WidgetState)
	 */
	@Override
	public int getMax(WidgetState widget) {
		return widget.getCount();
	}

}
