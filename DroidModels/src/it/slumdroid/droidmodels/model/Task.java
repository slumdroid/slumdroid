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

package it.slumdroid.droidmodels.model;

import java.util.Iterator;

public interface Task extends Iterable<Transition>, WrapperInterface {

	public String getId ();
	public void setId (String id);
	public Iterator<Transition> transitions();
	public void addTransition(Transition tail);
	public void setFinalActivity(ActivityState theActivity);
	public Transition getFinalTransition ();
	public boolean isFailed();
	public void setFailed (boolean failure);

}
