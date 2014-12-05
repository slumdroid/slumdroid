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

package it.slumdroid.tool.utilities;

import android.app.Activity;
import android.view.View;

public class ExtractorUtilities {

	private static Activity theActivity; // Current Activity

	public static Activity getActivity() {
		return theActivity;
	}

	public static void setActivity(Activity activity) {
		theActivity = activity;
	}

	public static View findViewById (int id) {
		return getActivity().findViewById(id);
	}

}
