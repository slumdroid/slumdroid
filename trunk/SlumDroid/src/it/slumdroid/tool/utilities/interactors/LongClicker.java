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

import static it.slumdroid.droidmodels.model.InteractionType.LONG_CLICK;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import it.slumdroid.droidmodels.model.WidgetState;

public class LongClicker extends SimpleInteractorAdapter {

	public LongClicker (String ... simpleTypes) {
		super (simpleTypes);
	}

	public boolean canUseWidget (WidgetState w) {
		return (w.isLongClickable() && super.canUseWidget(w));
	}

	public String getInteractionType () {
		return LONG_CLICK;
	}

}
