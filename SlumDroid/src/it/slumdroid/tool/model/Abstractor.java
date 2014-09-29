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

import java.util.Collection;

import org.w3c.dom.Element;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Task;
import it.slumdroid.droidmodels.model.Transition;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;

public interface Abstractor {

	public ActivityState createActivity (ActivityDescription desc);
	public boolean updateDescription (ActivityState theActivity, ActivityDescription theDescription);
	public void setBaseActivity (ActivityDescription desc);
	public ActivityState getBaseActivity ();
	public UserEvent createEvent (WidgetState target, String type);
	public UserInput createInput (WidgetState target, String value, String type);
	public Task createTrace (Task prototype, Transition appendix);
	public Transition createStep (ActivityState start, Collection<UserInput> inputs, UserEvent event);
	public Task importTask (Element e);
	public ActivityState importState (Element fromXml);
	public void setFinalActivity (Task theTask, ActivityState theActivity);

}
