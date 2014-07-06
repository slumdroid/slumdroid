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

package it.slumdroid.tool.model;

import java.util.List;

import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;

public interface Interactor {

	public boolean canUseWidget (WidgetState w);

	public List<UserEvent> getEvents (WidgetState w);
	public List<UserInput> getInputs (WidgetState w);
	public List<UserEvent> getEvents (WidgetState w, String ... values);
	public List<UserInput> getInputs (WidgetState w, String ... values);

	public Abstractor getAbstractor();
	public void setAbstractor (Abstractor a);

	public String getInteractionType ();

}
