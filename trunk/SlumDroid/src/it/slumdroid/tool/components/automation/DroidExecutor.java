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
import static it.slumdroid.tool.components.abstractor.AbstractorUtilities.getType;
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
	public DroidExecutor(ActivityInstrumentationTestCase2<?> test) {
		instrumentation = test.getInstrumentation();
		robotium = new Solo(instrumentation, test.getActivity());
	}

	/**
	 * Click.
	 *
	 * @param view the view
	 */
	public void click(View view) {
		getRobotium().clickOnView(view);
	}

	/**
	 * Long click.
	 *
	 * @param view the view
	 */
	public void longClick(View view) {
		getRobotium().clickLongOnView(view);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param value the value
	 */
	public void selectListItem(ListView list, String value) {
		if (list.getCount() != list.getChildCount()) {
			if (Integer.valueOf(value) < list.getChildCount()) {
				getRobotium().scrollListToLine(list, 0);
				getRobotium().clickInList(Integer.valueOf(value));
			} else {
				getRobotium().scrollListToLine(list, Integer.valueOf(value) - 1);
				getRobotium().sendKey(Solo.DOWN);
				click(list.getSelectedView());	
			}
		} else {
			getRobotium().clickInList(Integer.valueOf(value));	
		}
	}

	/**
	 * Long Select list item.
	 *
	 * @param list the list
	 * @param value the value
	 */
	public void selectLongListItem(ListView list, String value) {
		if (list.getCount() != list.getChildCount()) {
			if (Integer.valueOf(value) < list.getChildCount()) {
				getRobotium().scrollListToLine(list, 0);
				getRobotium().clickLongInList(Integer.valueOf(value));	
			} else {
				getRobotium().scrollListToLine(list, Integer.valueOf(value) - 1);
				getRobotium().sendKey(Solo.DOWN);
				longClick(list.getSelectedView());
			}
		} else {
			getRobotium().clickLongInList(Integer.valueOf(value));	
		}
	}

	/**
	 * Select spinner item.
	 *
	 * @param spinner the spinner
	 * @param value the value
	 */
	public void selectSpinnerItem(Spinner spinner, String value) {
		click(spinner);
		sync();
		final ListView list = getRobotium().getCurrentViews(ListView.class).get(0);
		if (getType(list).endsWith("DropDownListView")) {
			getRobotium().scrollListToLine(list, 0);
			sync();
			for (int row = 0; row < Integer.valueOf(value); row++) {
				getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
			}
			getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		} else {
			selectListItem(list, value);
		}
	}

	/**
	 * Write text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void writeText(EditText editText, String value) {
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
	public void typeText(EditText editText, String value) {
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
	public void writeTextAndEnter(EditText editText, String value) {
		typeText(editText, value);
		getRobotium().sendKey(Solo.ENTER);
	}

	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param value the value
	 */
	public void selectRadioItem(RadioGroup radioGroup, String value) {
		click(radioGroup.getChildAt(Integer.valueOf(value) - 1));
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
	 * @param progressBar the progress bar
	 * @param value the value
	 */
	public void setProgressBar(ProgressBar progressBar, String value) { 
		getRobotium().setProgressBar(progressBar, Integer.parseInt(value));
	}

	/**
	 * Swap tab.
	 *
	 * @param tabHost the tab host
	 * @param value the value
	 */
	public void swapTab(TabHost tabHost, String value) {
		click(tabHost.getTabWidget().getChildAt(Integer.valueOf(value) - 1));
	}

	/**
	 * Wait.
	 *
	 * @param milli the milli
	 */
	public void wait(int milli) {
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
