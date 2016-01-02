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

package it.slumdroid.tool.utilities.interactors;

import static it.slumdroid.droidmodels.model.InteractionType.LONG_CLICK;
import static it.slumdroid.droidmodels.model.SimpleType.IMAGE_VIEW;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class LongClicker.
 */
public class LongClicker extends SimpleInteractorAdapter {

	/**
	 * Instantiates a new long clicker.
	 */
	public LongClicker() {
		super(IMAGE_VIEW);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean canUseWidget(WidgetState widget) {
		return widget.isLongClickable() && super.canUseWidget(widget);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInteractionType()
	 */
	public String getInteractionType() {
		return LONG_CLICK;
	}

}
