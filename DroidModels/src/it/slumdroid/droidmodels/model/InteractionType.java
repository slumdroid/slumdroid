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

public interface InteractionType {

	public final static String PRESS_BACK   = "pressBack";
	public final static String PRESS_MENU   = "pressMenu";
	public final static String PRESS_ACTION = "pressActionBar";
	public final static String CHANGE_ORIENTATION = "changeOrientation";
	public final static String CLICK = "click";
	public final static String LONG_CLICK = "longClick";
	public final static String DRAG = "drag";
	public final static String ENTER_TEXT = "enterText";
	public final static String WRITE_TEXT = "writeText";
	public final static String LIST_LONG_SELECT = "longSelectListItem";
	public final static String LIST_SELECT = "selectListItem";
	public final static String RADIO_SELECT = "selectRadioItem";
	public final static String SPINNER_SELECT = "selectSpinnerItem";
	public final static String SET_BAR = "setBar";
	public final static String SWAP_TAB = "swapTab";

}
