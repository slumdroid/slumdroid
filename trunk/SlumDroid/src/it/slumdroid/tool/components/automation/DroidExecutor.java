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
	public void click (View view) {
		assertNotNull(view,"Cannot click: the widget does not exist");
		getRobotium().clickOnView(view);
	}

	/**
	 * Long click.
	 *
	 * @param view the view
	 */
	public void longClick (View view) {
		assertNotNull(view, "Cannot longClick: the widget does not exist");
		getRobotium().clickLongOnView(view);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param item the item
	 */
	public void selectListItem (ListView list, String item) {
		assertNotNull(list, "Cannon select list item: the list does not exist");
		getRobotium().sendKey(Solo.DOWN);
		final ListView theList = list;
		final int index = Math.min(list.getCount(), Math.max(1, Integer.valueOf(item))) - 1;
		runOnUiThread(new Runnable() { 
			public void run() {
				theList.setSelection(index);
			}
		});
		if (index < list.getCount()/2) {
			getRobotium().sendKey(Solo.DOWN);
			getRobotium().sendKey(Solo.UP);
		} else {
			getRobotium().sendKey(Solo.UP);                  
			getRobotium().sendKey(Solo.DOWN);
		}
		getRobotium().clickOnView(list.getSelectedView());
	}

	/**
	 * Long Select list item.
	 *
	 * @param list the list
	 * @param item the item
	 * @param longClick the long click
	 */
	public void selectLongListItem (ListView list, String item) {
		assertNotNull(list, "Cannon long select list item: the list does not exist");
		getRobotium().sendKey(Solo.DOWN);
		final ListView theList = list;
		final int index = Math.min(list.getCount(), Math.max(1, Integer.valueOf(item))) - 1;
		runOnUiThread(new Runnable() { 
			public void run() {
				theList.setSelection(index);
			}
		});
		if (index < list.getCount()/2) {
			getRobotium().sendKey(Solo.DOWN);
			getRobotium().sendKey(Solo.UP);
		} else {
			getRobotium().sendKey(Solo.UP);                  
			getRobotium().sendKey(Solo.DOWN);
		}
		getRobotium().clickLongOnView(list.getSelectedView());
	}

	/**
	 * Select spinner item.
	 *
	 * @param spinner the spinner
	 * @param item the item
	 */
	public void selectSpinnerItem (Spinner spinner, String item) {
		assertNotNull(spinner, "Cannon press spinner item: the spinner does not exist");
		click(spinner);
		selectListItem(getRobotium().getCurrentViews(ListView.class).get(0), item);
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
			getRobotium().typeText(editText, value);
		}
	}

	/**
	 * Enter text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public void enterText (EditText editText, String value) {
		writeText (editText, value);
		getRobotium().sendKey(Solo.ENTER);
	}

	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param item the item
	 */
	public void selectRadioItem (final RadioGroup radioGroup, String value) {
		int item = Integer.valueOf(value);
		if (item < 1) {
			assertNotNull(null, "Cannot press radio group item: the index must be a positive number");
		}
		assertNotNull(radioGroup, "Cannon press radio group item: the radio group does not exist");
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
		getRobotium().setProgressBar((ProgressBar)view, Integer.parseInt(value));
	}

	/**
	 * Swap tab.
	 *
	 * @param view the view
	 * @param tab the tab
	 */
	public void swapTab (View view, String tab) {
		TabHost tabHost = (TabHost)view; 
		int item = Integer.valueOf(tab);
		assertNotNull(tabHost, "Cannon swap tab: the tab host does not exist");
		int count = tabHost.getTabWidget().getTabCount();
		ActivityInstrumentationTestCase2.assertTrue("Cannot swap tab: tab index out of bound", item <= count);
		int index = Math.min(count, Math.max(1, item)) - 1;
		click (tabHost.getTabWidget().getChildAt(index));
	}

	/**
	 * Request view.
	 *
	 * @param view the view
	 */
	public void requestView (final View view) {
		try {
			getRobotium().sendKey(Solo.UP); // Solo.waitForView() requires a widget to be focused		
			getRobotium().waitForView(view, 1000, true);
			runOnUiThread(new Runnable() {
				public void run() {
					view.requestFocus();		
				}
			});
			sync();	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}		

	/**
	 * Run on ui thread.
	 *
	 * @param action the action
	 */
	protected void runOnUiThread (Runnable action) {
		getRobotium().getCurrentActivity().runOnUiThread(action);		
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
	protected static void assertNotNull (final View view, String errorMessage) {
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
	
}
