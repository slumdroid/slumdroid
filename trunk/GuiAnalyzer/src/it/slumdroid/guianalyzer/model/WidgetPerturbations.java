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

package it.slumdroid.guianalyzer.model;

import it.slumdroid.droidmodels.model.WidgetState;
import java.util.List;

public interface WidgetPerturbations {

	public void addPerturbation (String perturbation);
	public List<WidgetState> getList ();
	public List<String> getPerturbations ();
	public void standard ();
	public void dictionary (String inputFileName);
	public void manual (String perturbation);
	public void manualRegEx (String regEx);
	public void exclude ();

}
