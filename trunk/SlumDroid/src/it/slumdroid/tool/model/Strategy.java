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

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Trace;

public interface Strategy {

	public void addState (ActivityState newActivity);

	public Comparator getComparator ();
	public void setComparator (Comparator c);

	public boolean compareState (ActivityState theActivity);
	public boolean isLastComparationPositive ();

	public boolean checkForTermination ();
	public boolean checkForExploration ();
	public boolean checkForPause ();

	public void setTask(Trace theTask);
	public Trace getTask();

	public ActivityState getStateBeforeEvent();
	public ActivityState getStateAfterEvent ();

	public void registerTerminationListener(TerminationListener theListener);

}
