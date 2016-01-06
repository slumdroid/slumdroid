/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.components.abstractor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import it.slumdroid.droidmodels.model.WidgetState;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractorUtilities.
 */
public class AbstractorUtilities {

	/**
	 * Detect name.
	 *
	 * @param view the view
	 * @return the string
	 */
	public static String detectName(View view) {
		String name = new String();
		if (view instanceof TextView) {
			name = ((TextView)view).getText().toString();
			if (view instanceof EditText) {
				CharSequence hint = ((EditText)view).getHint();
				name = (hint == null)?new String():hint.toString();
			}
			return name;
		} 
		if (view instanceof RadioGroup) {
			RadioGroup group = (RadioGroup)view;
			int max = group.getChildCount();
			String text = new String();
			for (int item = 0; item < max; item++) {
				View child = group.getChildAt(item);
				text = detectName(child);
				if (!text.equals("")) {
					name = text;
					break;
				}
			}
		}
		return name;
	}

	/**
	 * Sets the count.
	 *
	 * @param view the view
	 * @param widget the widget
	 */
	public static void setCount(View view, WidgetState widget) {
		if (view instanceof AdapterView) {
			widget.setCount(((AdapterView<?>)view).getCount());
			return;
		}
		if (view instanceof ProgressBar) {
			widget.setCount(((ProgressBar)view).getMax());
			return;
		}
		if (view instanceof TabHost) {
			widget.setCount(((TabHost)view).getTabWidget().getTabCount());
			return;
		}
		if (view instanceof ViewGroup) {
			widget.setCount(((ViewGroup)view).getChildCount());
			return;
		}
	}

	/**
	 * Sets the value.
	 *
	 * @param view the view
	 * @param widget the widget
	 */
	public static void setValue(View view, WidgetState widget) {
		if (view instanceof Checkable) {
			widget.setValue(((Checkable)view).isChecked()?"true":"false");
			return;
		}
		if (view instanceof TextView) {
			widget.setValue(((TextView)view).getText().toString());
			return;
		}
		if (view instanceof ProgressBar) {
			widget.setValue(String.valueOf(((ProgressBar)view).getProgress()));
			return;
		}
	}

	/**
	 * Gets the type.
	 *
	 * @param view the view
	 * @return the type
	 */
	public static String getType(View view) {
		return view.getClass().getName();
	}

}
