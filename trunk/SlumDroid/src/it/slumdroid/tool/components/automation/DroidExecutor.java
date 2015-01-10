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
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;

import com.robotium.solo.Solo;

// TODO: Auto-generated Javadoc
/**
 * The Class DroidExecutor.
 */
@SuppressWarnings("deprecation")
public class DroidExecutor {

	/** The solo. */
	static private Solo solo;

	/** The instrum. */
	static private Instrumentation instrum;

	/**
	 * Creates the robotium.
	 *
	 * @param test the test
	 * @return the solo
	 */
	public static Solo createRobotium (ActivityInstrumentationTestCase2<?> test) {
		instrum = test.getInstrumentation();
		solo = new Solo (instrum, test.getActivity());
		return solo;
	}

	/**
	 * Gets the instrumentation.
	 *
	 * @return the instrumentation
	 */
	public static Instrumentation getInstrumentation() {
		return instrum;
	}

	/**
	 * Un lock screen.
	 */
	public static void unLockScreen() {
		solo.unlockScreen();
	}

	// Click interactions
	/**
	 * Click.
	 *
	 * @param view the view
	 */
	public static void click (View view) {
		assertNotNull(view,"Cannot click: the widget does not exist");
		solo.clickOnView(view);
	}

	/**
	 * Long click.
	 *
	 * @param view the view
	 */
	public static void longClick (View view) {
		assertNotNull(view, "Cannot longClick: the widget does not exist");
		solo.clickLongOnView(view);
	}

	// List interactions
	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param item the item
	 */
	public static void selectListItem (ListView list, String item) {
		selectListItem (list, item, false);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param item the item
	 * @param longClick the long click
	 */
	public static void selectListItem (ListView list, String item, boolean longClick) {
		selectListItem (list, Integer.valueOf(item), longClick);
	}

	/**
	 * Select list item.
	 *
	 * @param list the list
	 * @param num the num
	 * @param longClick the long click
	 */
	public static void selectListItem (ListView list, int num, boolean longClick) {
		assertNotNull(list, "Cannon select list item: the list does not exist");
		requestFocus(list);
		if (longClick) {
			solo.clickLongInList(num);
		} else {
			solo.clickInList(num);
		}
	}

	// Spinner interactions
	/**
	 * Select spinner item.
	 *
	 * @param spinner the spinner
	 * @param item the item
	 */
	public static void selectSpinnerItem (Spinner spinner, String item) {
		selectSpinnerItem (spinner, Integer.valueOf(item));
	}

	/**
	 * Select spinner item.
	 *
	 * @param spinner the spinner
	 * @param num the num
	 */
	public static void selectSpinnerItem (final Spinner spinner, int num) {
		assertNotNull(spinner, "Cannon press spinner item: the spinner does not exist");
		click(spinner);
		selectListItem(solo.getCurrentViews(ListView.class).get(0), num, false);
	}

	// Text interactions
	/**
	 * Write text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public static void writeText (EditText editText, String value) {
		solo.clearEditText(editText);
		if (!value.equals("")) {
			//solo.enterText(editText, value);
			solo.typeText(editText, value);
		}
	}

	/**
	 * Enter text.
	 *
	 * @param editText the edit text
	 * @param value the value
	 */
	public static void enterText (EditText editText, String value) {
		writeText (editText, value);
		solo.sendKey(Solo.ENTER);
	}

	// Radio Interactions
	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param value the value
	 */
	public static void selectRadioItem (RadioGroup radioGroup, String value) {
		selectRadioItem (radioGroup, Integer.valueOf(value));
	}

	/**
	 * Select radio item.
	 *
	 * @param radioGroup the radio group
	 * @param num the num
	 */
	public static void selectRadioItem (final RadioGroup radioGroup, int num) {
		if (num < 1) {
			assertNotNull(null, "Cannot press radio group item: the index must be a positive number");
		}
		assertNotNull(radioGroup, "Cannon press radio group item: the radio group does not exist");
		click(radioGroup.getChildAt(num - 1));
	}

	//SlidingDrawer Interactions
	/**
	 * Drag.
	 *
	 * @param view the view
	 */
	public static void drag (View view) {
		if (view.isShown()) {
			solo.setSlidingDrawer((SlidingDrawer) view, Solo.CLOSED);
		}
		else {
			solo.setSlidingDrawer((SlidingDrawer) view, Solo.OPENED);
		}
	}

	// Special interactions
	/**
	 * Go back.
	 */
	public static void goBack() {
		solo.goBack();
	}

	/**
	 * Open menu.
	 */
	public static void openMenu() {
		solo.sendKey(Solo.MENU);
	}

	/**
	 * Change orientation.
	 */
	public static void changeOrientation() {
		Display display = ((WindowManager) getInstrumentation().getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int angle = display.getRotation();
		int newAngle = ((angle == ROTATION_0) || (angle == ROTATION_180))?Solo.LANDSCAPE:Solo.PORTRAIT;
		solo.setActivityOrientation(newAngle);
	}

	// ActionBar interactions
	/**
	 * Action bar home.
	 */
	public static void actionBarHome () {
		solo.clickOnActionBarHomeButton();
	}

	// Progress Bar interactions
	/**
	 * Sets the progress bar.
	 *
	 * @param view the view
	 * @param value the value
	 */
	public static void setProgressBar (View view, String value) {
		solo.setProgressBar((ProgressBar)view, Integer.parseInt(value));
	}

	// Tab interactions
	/**
	 * Swap tab.
	 *
	 * @param view the view
	 * @param tab the tab
	 */
	public static void swapTab (View view, String tab) {
		swapTab ((TabHost)view, Integer.valueOf(tab));
	}

	/**
	 * Swap tab.
	 *
	 * @param tabHost the tab host
	 * @param num the num
	 */
	public static void swapTab (final TabHost tabHost, int num) {
		assertNotNull(tabHost, "Cannon swap tab: the tab host does not exist");
		int count = tabHost.getTabWidget().getTabCount();
		ActivityInstrumentationTestCase2.assertTrue("Cannot swap tab: tab index out of bound", num<=count);
		final int index = Math.min(count, Math.max(1,num)) - 1;
		click (tabHost.getTabWidget().getChildAt(index));
	}

	/**
	 * Request view.
	 *
	 * @param view the view
	 */
	public static void requestView (final View view) {
		try {
			solo.sendKey(Solo.UP); // Solo.waitForView() requires a widget to be focused		
			solo.waitForView(view, 1000, true);
			requestFocus(view);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}		

	/**
	 * Request focus.
	 *
	 * @param view the view
	 */
	protected static void requestFocus (final View view) {
		runOnUiThread(new Runnable() {
			public void run() {
				view.requestFocus();		
			}
		});
		getInstrumentation().waitForIdleSync();
	}

	// Utility methods
	/**
	 * Run on ui thread.
	 *
	 * @param action the action
	 */
	protected static void runOnUiThread (Runnable action) {
		ExtractorUtilities.getActivity().runOnUiThread(action);		
	}

	/**
	 * Wait.
	 *
	 * @param milli the milli
	 */
	public static void wait (int milli) {
		solo.sleep(milli);
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

}
