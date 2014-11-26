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

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.WidgetState;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class AbstractorUtilities {

	public static String detectName (View v) {
		String name = new String();
		if (v instanceof TextView) {
			TextView text = (TextView)v;
			name = text.getText().toString();
			if (v instanceof EditText) {
				CharSequence hint = ((EditText)v).getHint();
				name = (hint == null)?new String():hint.toString();
			}
		} else if (v instanceof RadioGroup) {
			RadioGroup group = (RadioGroup)v;
			int max=group.getChildCount();
			String text = new String();
			for (int i=0; i < max; i++) {
				View child = group.getChildAt(i);
				text = detectName (child);
				if (!text.equals("")) {
					name = text;
					break;
				}
			}
		}
		return name;
	}

	public static void setCount (View v, WidgetState w) {
		// For lists, the count is set to the number of rows in the list (inactive rows - e.g. separators - count as well)
		if (v instanceof AdapterView) {
			w.setCount(((AdapterView<?>)v).getCount());
			return;
		}
		// For Spinners, the count is set to the number of options
		if (v instanceof AbsSpinner) {
			w.setCount(((AbsSpinner)v).getCount());
			return;
		}
		// For the tab layout host, the count is set to the number of tabs
		if (v instanceof TabHost) {
			w.setCount(((TabHost)v).getTabWidget().getTabCount());
			return;
		}
		// For grids, the count is set to the number of icons, for RadioGroups it's set to the number of RadioButtons
		if (v instanceof ViewGroup) {
			w.setCount(((ViewGroup)v).getChildCount());
			return;
		}
		// For progress bars, seek bars and rating bars, the count is set to the maximum value allowed
		if (v instanceof ProgressBar) {
			w.setCount(((ProgressBar)v).getMax());
			return;
		}
	}

	public static void setValue (View v, WidgetState w) {

		// CheckBoxes, Radio Buttons and Toggle Buttons -> the value is the checked state (true or false)
		if (v instanceof Checkable) {
			w.setValue(((Checkable) v).isChecked()?"true":"false");
			return;
		}
		// TextView, EditText et al. -> the value is the displayed text
		if (v instanceof TextView) {
			w.setValue(((TextView) v).getText().toString());
			return;
		}
		// ProgressBars, SeekBars and RatingBars -> the value is the current progress
		if (v instanceof ProgressBar) {
			w.setValue(String.valueOf(((ProgressBar) v).getProgress()));
			return;
		}
	}

	public static String getType (View v) {
		return v.getClass().getName();
	}

	// Event description methods, used by Automation - the description property is only used in graphs	
	public static boolean describeCurrentEvent (UserEvent e, View v) {
		if (e == null) return false; // This is probably an input, not an event
		// Get text from the target widget
		if (v instanceof TextView) {
			String text = ((TextView)v).getText().toString();
			e.setDescription(text);
			return true;
		} else if (v instanceof TabHost) {
			e.setDescription(((TabHost)v).getCurrentTabTag());
		} else if (v instanceof ViewGroup) {
			int childNum = ((ViewGroup)v).getChildCount();
			for (int i = 0; i < childNum; i++) {
				View child =  ((ViewGroup)v).getChildAt(i);
				if (describeCurrentEvent(e, child)) return true;
			}
		}
		return false;
	}

}
