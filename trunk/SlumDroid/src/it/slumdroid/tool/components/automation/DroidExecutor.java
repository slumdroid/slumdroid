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
 * Copyright (C) 2013-2015 Gennaro Imparato
 */

package it.slumdroid.tool.components.automation;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;

import com.robotium.solo.Solo;

// TODO: Auto-generated Javadoc
/**
 * The Class DroidExecutor.
 */
public class DroidExecutor {

	/** The robotium. */
	private static Solo robotium;

	/** The instrumentation. */
	private Instrumentation instrumentation;

	/**
	 * Instantiates a new droid executor.
	 *
	 * @param test the test
	 */
	public DroidExecutor (ActivityInstrumentationTestCase2<?> test) {
		instrumentation = test.getInstrumentation();
		robotium = new Solo (instrumentation, test.getActivity());
	}

	/**
	 * Click.
	 *
	 * @param view the view
	 */
	public void click (View view) {
		getRobotium().clickOnView(view);
	}

	/**
	 * Long click.
	 *
	 * @param view the view
	 */
	public void longClick (View view) {
		getRobotium().clickLongOnView(view);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param value the value
	 */
	public void selectListItem (final ListView list, String value) {
		getInstrumentation().runOnMainSync(new Runnable() {
			public void run() {
				list.setSelection(0);
			}
		});
		sync();
		int item = Integer.valueOf(value);
		if (list.getCount() != list.getChildCount()) {
			if (item < list.getChildCount()) {
				getRobotium().clickInList(item);
			} else {
				selectRow(list, item);
				click(list.getSelectedView());	
			}
		} else {
			getRobotium().clickInList(item);	
		}
	}

	/**
	 * Long Select list item.
	 *
	 * @param list the list
	 * @param value the value
	 */
	public void selectLongListItem (final ListView list, String value) {
		getInstrumentation().runOnMainSync(new Runnable() {
			public void run() {
				list.setSelection(0);
			}
		});
		sync();
		int item = Integer.valueOf(value);
		if (list.getCount() != list.getChildCount()) {
			if (item < list.getChildCount()) {
				getRobotium().clickLongInList(item);	
			} else {
				selectRow(list, item);
				longClick(list.getSelectedView());
			}
		} else {
			getRobotium().clickLongInList(item);	
		}
	}

	/**
	 * Select row.
	 *
	 * @param list the list
	 * @param item the item
	 */
	private void selectRow (ListView list, int item) {
		final int row = Math.min(list.getCount(), Math.max(1, item)) - 1;
		getRobotium().scrollListToLine(list, row - 1);
		getRobotium().sendKey(Solo.DOWN);
	}

	/**
	 * Select spinner item.
	 *
	 * @param spinner the spinner
	 * @param value the value
	 */
	public void selectSpinnerItem (Spinner spinner, String value) {
		click(spinner);
		sync();
		int item = Integer.valueOf(value);
		final ListView list = getRobotium().getCurrentViews(ListView.class).get(0);
		if (list.getCount() != list.getChildCount()) {
			getInstrumentation().runOnMainSync(new Runnable() {
				public void run() {
					list.setSelection(0);
				}
			});
			sync();
			for (int row = 0; row < item; row++) {
				getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
			}
			getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		} else {
			getRobotium().clickInList(item);
		}
	}

	/**
	 * Write text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void writeText (EditText editText, String value) {
		getRobotium().clearEditText(editText);
		if (!value.equals("")) {
			getRobotium().enterText(editText, value);
		}
	}

	/**
	 * Type text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void typeText (EditText editText, String value) {
		getRobotium().clearEditText(editText);
		if (!value.equals("")) {
			getRobotium().typeText(editText, value);
		}
	}

	/**
	 * Write text and enter.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void writeTextAndEnter (EditText editText, String value) {
		typeText (editText, value);
		getRobotium().sendKey(Solo.ENTER);
	}

	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param value the value
	 */
	public void selectRadioItem (RadioGroup radioGroup, String value) {
		int item = Integer.valueOf(value);
		click(radioGroup.getChildAt(item - 1));
	}

	/**
	 * Change orientation.
	 */
	public void changeOrientation() {
		Display display = ((WindowManager) getInstrumentation().getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int angle = display.getRotation();
		int newAngle = ((angle == ROTATION_0) || (angle == ROTATION_180))?Solo.LANDSCAPE:Solo.PORTRAIT;
		getRobotium().setActivityOrientation(newAngle);
	}

	/**
	 * Sets the progress bar.
	 *
	 * @param view the view
	 * @param value the value
	 */
	public void setProgressBar (View view, String value) {
		ProgressBar progressBar = (ProgressBar)view; 
		getRobotium().setProgressBar(progressBar, Integer.parseInt(value));
	}

	/**
	 * Swap tab.
	 *
	 * @param view the view
	 * @param value the value
	 */
	public void swapTab (View view, String value) {
		TabHost tabHost = (TabHost)view; 
		int item = Integer.valueOf(value);
		int count = tabHost.getTabWidget().getTabCount();
		int index = Math.min(count, Math.max(1, item)) - 1;
		click(tabHost.getTabWidget().getChildAt(index));
	}

	/**
	 * Wait.
	 *
	 * @param milli the milli
	 */
	public void wait (int milli) {
		getRobotium().sleep(milli);
	}

	/**
	 * Gets the instrumentation.
	 *
	 * @return the instrumentation
	 */
	public Instrumentation getInstrumentation() {
		return instrumentation;
	}

	/**
	 * Sync.
	 */
	public void sync() {
		getInstrumentation().waitForIdleSync();
	}

	/**
	 * Gets the robotium.
	 *
	 * @return the robotium
	 */
	public Solo getRobotium() {
		return robotium;
	}

	/**
	 * Gets the current activity.
	 *
	 * @return the current activity
	 */
	public Activity getCurrentActivity() {
		return getRobotium().getCurrentActivity();
	}

}
