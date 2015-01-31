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
		robotium = new Solo (instrumentation, test.getActivity());
	}

	/**
	 * Click.
	 *
	 * @param view the view
	 */
	public void click (final View view) {
		assertNotNull(view,"Cannot click: the widget does not exist");
		getRobotium().clickOnView(view);
	}

	/**
	 * Long click.
	 *
	 * @param view the view
	 */
	public void longClick (final View view) {
		assertNotNull(view, "Cannot longClick: the widget does not exist");
		getRobotium().clickLongOnView(view);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param value the value
	 */
	public void selectListItem (final ListView list, final String value) {
		assertNotNull(list, "Cannon select list item: the list does not exist");
		if (list.getCount() != list.getChildCount()) {
			requestView(list);
			final int item = Math.min(list.getCount(), Math.max(1, Integer.valueOf(value))) - 1;
			runOnUiThread(new Runnable() { 
				public void run() {
					list.setSelection(item);
				}
			});
			if (item < list.getCount()/2) {
				getRobotium().sendKey(Solo.DOWN);
				getRobotium().sendKey(Solo.UP);
			} else {
				getRobotium().sendKey(Solo.UP);                  
				getRobotium().sendKey(Solo.DOWN);
			}
			getRobotium().clickOnView(list.getSelectedView());	
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
	public void selectLongListItem (final ListView list, final String value) {
		assertNotNull(list, "Cannon long select list item: the list does not exist");
		if (list.getCount() != list.getChildCount()) {
			requestView(list);
			final int item = Math.min(list.getCount(), Math.max(1, Integer.valueOf(value))) - 1;
			runOnUiThread(new Runnable() { 
				public void run() {
					list.setSelection(item);
				}
			});
			if (item < list.getCount()/2) {
				getRobotium().sendKey(Solo.DOWN);
				getRobotium().sendKey(Solo.UP);
			} else {
				getRobotium().sendKey(Solo.UP);                  
				getRobotium().sendKey(Solo.DOWN);
			}
			getRobotium().clickLongOnView(list.getSelectedView());	
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
	public void selectSpinnerItem (final Spinner spinner, final String value) {
		assertNotNull(spinner, "Cannon press spinner item: the spinner does not exist");
		getRobotium().clickOnView(spinner);
		final ListView list = getRobotium().getCurrentViews(ListView.class).get(0);
		if (list.getCount() != list.getChildCount()) {
			requestView(list);
			final int item = Math.min(list.getCount(), Math.max(1, Integer.valueOf(value))) - 1;
			runOnUiThread(new Runnable() { 
				public void run() {
					list.setSelection(item);
				}
			});
			if (item < list.getCount()/2) {
				getRobotium().sendKey(Solo.DOWN);
				getRobotium().sendKey(Solo.UP);
			} else {
				getRobotium().sendKey(Solo.UP);                  
				getRobotium().sendKey(Solo.DOWN);
			}
			getRobotium().clickOnView(list.getSelectedView());	
		} else {
			getRobotium().clickInList(Integer.valueOf(value));	
		}		
	}

	/**
	 * Write text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void writeText (final EditText editText, final String value) {
		getRobotium().clearEditText(editText);
		if (!value.equals("")) {
			getRobotium().enterText(editText, value);
		}
	}

	public void writeTextAndEnter (final EditText editText, final String value) {
		writeText (editText, value);
		getRobotium().sendKey(Solo.ENTER);
	}

	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param value the value
	 */
	public void selectRadioItem (final RadioGroup radioGroup, final String value) {
		int item = Integer.valueOf(value);
		if (item < 1) {
			assertNotNull(null, "Cannot press radio group item: the index must be a positive number");
		}
		assertNotNull(radioGroup, "Cannon press radio group item: the radio group does not exist");
		getRobotium().clickOnView(radioGroup.getChildAt(item - 1));
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
	public void setProgressBar (final View view, final String value) {
		getRobotium().setProgressBar((ProgressBar)view, Integer.parseInt(value));
	}

	/**
	 * Swap tab.
	 *
	 * @param view the view
	 * @param value the value
	 */
	public void swapTab (final View view, final String value) {
		TabHost tabHost = (TabHost)view; 
		int item = Integer.valueOf(value);
		assertNotNull(tabHost, "Cannon swap tab: the tab host does not exist");
		int count = tabHost.getTabWidget().getTabCount();
		ActivityInstrumentationTestCase2.assertTrue("Cannot swap tab: tab index out of bound", item <= count);
		int index = Math.min(count, Math.max(1, item)) - 1;
		getRobotium().clickOnView(tabHost.getTabWidget().getChildAt(index));
	}

	/**
	 * Request view.
	 *
	 * @param view the view
	 */
	public void requestView (final View view) {
		getRobotium().sendKey(Solo.UP); // Solo.waitForView() requires a widget to be focused		
		getRobotium().waitForView(view, 1000, true);
		runOnUiThread(new Runnable() {
			public void run() {
				view.requestFocus();            
			}
		});
		sync();		
	}		

	/**
	 * Run on ui thread.
	 *
	 * @param action the action
	 */
	protected void runOnUiThread (Runnable action) {
		getCurrentActivity().runOnUiThread(action);		
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
	 * Assert not null.
	 *
	 * @param view the view
	 * @param errorMessage the error message
	 */
	protected static void assertNotNull (View view, String errorMessage) {
		ActivityInstrumentationTestCase2.assertNotNull(errorMessage, view);
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
