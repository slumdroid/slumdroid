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

	public static String detectName (View view) {
		String name = new String();
		if (view instanceof TextView) {
			TextView text = (TextView)view;
			name = text.getText().toString();
			if (view instanceof EditText) {
				CharSequence hint = ((EditText)view).getHint();
				name = (hint == null)?new String():hint.toString();
			}
		} else if (view instanceof RadioGroup) {
			RadioGroup group = (RadioGroup)view;
			int max = group.getChildCount();
			String text = new String();
			for (int i = 0; i < max; i++) {
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

	public static void setCount (View view, WidgetState widget) {
		// For lists, the count is set to the number of rows in the list (inactive rows - e.g. separators - count as well)
		if (view instanceof AdapterView) {
			widget.setCount(((AdapterView<?>)view).getCount());
			return;
		}
		// For Spinners, the count is set to the number of options
		if (view instanceof AbsSpinner) {
			widget.setCount(((AbsSpinner)view).getCount());
			return;
		}
		// For the tab layout host, the count is set to the number of tabs
		if (view instanceof TabHost) {
			widget.setCount(((TabHost)view).getTabWidget().getTabCount());
			return;
		}
		// For grids, the count is set to the number of icons, for RadioGroups it's set to the number of RadioButtons
		if (view instanceof ViewGroup) {
			widget.setCount(((ViewGroup)view).getChildCount());
			return;
		}
		// For progress bars, seek bars and rating bars, the count is set to the maximum value allowed
		if (view instanceof ProgressBar) {
			widget.setCount(((ProgressBar)view).getMax());
			return;
		}
	}

	public static void setValue (View view, WidgetState widget) {

		// CheckBoxes, Radio Buttons and Toggle Buttons -> the value is the checked state (true or false)
		if (view instanceof Checkable) {
			widget.setValue(((Checkable) view).isChecked()?"true":"false");
			return;
		}
		// TextView, EditText et al. -> the value is the displayed text
		if (view instanceof TextView) {
			widget.setValue(((TextView) view).getText().toString());
			return;
		}
		// ProgressBars, SeekBars and RatingBars -> the value is the current progress
		if (view instanceof ProgressBar) {
			widget.setValue(String.valueOf(((ProgressBar) view).getProgress()));
			return;
		}
	}

	public static String getType (View view) {
		return view.getClass().getName();
	}

	// Event description methods, used by Automation - the description property is only used in graphs	
	public static boolean describeCurrentEvent (UserEvent event, View view) {
		if (event == null) return false; // This is probably an input, not an event
		// Get text from the target widget
		if (view instanceof TextView) {
			String text = ((TextView)view).getText().toString();
			event.setDescription(text);
			return true;
		} else if (view instanceof TabHost) {
			event.setDescription(((TabHost)view).getCurrentTabTag());
		} else if (view instanceof ViewGroup) {
			int childNum = ((ViewGroup)view).getChildCount();
			for (int i = 0; i < childNum; i++) {
				View child =  ((ViewGroup)view).getChildAt(i);
				if (describeCurrentEvent(event, child)) return true;
			}
		}
		return false;
	}

}
