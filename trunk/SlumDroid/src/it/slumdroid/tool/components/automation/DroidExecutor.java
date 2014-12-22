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

package it.slumdroid.tool.components.automation;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import it.slumdroid.tool.utilities.ExtractorUtilities;
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

@SuppressWarnings("deprecation")
public class DroidExecutor {

	static private Solo solo;
	static private Instrumentation instrum;

	public static Solo createRobotium (ActivityInstrumentationTestCase2<?> test) {
		instrum = test.getInstrumentation();
		solo = new Solo (instrum, test.getActivity());
		return solo;
	}

	public static Instrumentation getInstrumentation() {
		return instrum;
	}
	
	public static void unLockScreen() {
		solo.unlockScreen();
	}

	// Click interactions
	public static void click (View view) {
		assertNotNull(view,"Cannot click: the widget does not exist");
		solo.clickOnView(view);
	}

	public static void longClick (View view) {
		assertNotNull(view, "Cannot longClick: the widget does not exist");
		solo.clickLongOnView(view);
	}

	// List interactions
	public static void selectListItem (ListView list, String item) {
		selectListItem (list, item, false);
	}

	public static void selectListItem (ListView list, String item, boolean longClick) {
		selectListItem (list, Integer.valueOf(item), longClick);
	}

	public static void selectListItem (ListView list, int num, boolean longClick) {
		assertNotNull(list, "Cannon select list item: the list does not exist");
		requestFocus(list);
		solo.sendKey(Solo.DOWN);
		final ListView theList = list;
		final int index = Math.min(list.getCount(), Math.max(1,num)) - 1;
		runOnUiThread(new Runnable() { 
			public void run() {
				theList.setSelection(index);
			}
		});
		if (index < list.getCount()/2) {
			solo.sendKey(Solo.DOWN);
			solo.sendKey(Solo.UP);
		} else {
			solo.sendKey(Solo.UP);                  
			solo.sendKey(Solo.DOWN);
		}
		View view = list.getSelectedView();
		if (longClick) longClick(view);
		else click (view);
	}

	// Spinner interactions
	public static void selectSpinnerItem (Spinner spinner, String item) {
		selectSpinnerItem (spinner, Integer.valueOf(item));
	}

	public static void selectSpinnerItem (final Spinner spinner, int num) {
		assertNotNull(spinner, "Cannon press spinner item: the spinner does not exist");
		click(spinner);
		selectListItem(solo.getCurrentViews(ListView.class).get(0), num, false);
	}

	// Text interactions
	public static void writeText (EditText editText, String value) {
		solo.clearEditText(editText);
		if (!value.equals("")) solo.enterText(editText, value);
	}

	public static void enterText (EditText editText, String value) {
		writeText (editText, value);
		solo.sendKey(Solo.ENTER);
	}

	// Radio Interactions
	public static void selectRadioItem (RadioGroup radioGroup, String value) {
		selectRadioItem (radioGroup, Integer.valueOf(value));
	}

	public static void selectRadioItem (final RadioGroup radioGroup, int num) {
		if (num < 1) assertNotNull(null, "Cannot press radio group item: the index must be a positive number");
		assertNotNull(radioGroup, "Cannon press radio group item: the radio group does not exist");
		click(radioGroup.getChildAt(num - 1));
	}

	//SlidingDrawer Interactions
	public static void drag (View view){
		if (view.isShown()) solo.setSlidingDrawer((SlidingDrawer) view, Solo.CLOSED);
		else solo.setSlidingDrawer((SlidingDrawer) view, Solo.OPENED);
	}

	// Special interactions
	public static void goBack() {
		solo.goBack();
	}

	public static void openMenu() {
		solo.sendKey(Solo.MENU);
	}

	public static void changeOrientation() {
		Display display = ((WindowManager) getInstrumentation().getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int angle = display.getRotation();
		int newAngle = ((angle == ROTATION_0) || (angle == ROTATION_180))?Solo.LANDSCAPE:Solo.PORTRAIT;
		solo.setActivityOrientation(newAngle);
	}

	// ActionBar interactions
	public static void actionBarHome () {
		solo.clickOnActionBarHomeButton();
	}

	// Progress Bar interactions
	public static void setProgressBar (View view, String value) {
		solo.setProgressBar((ProgressBar)view, Integer.parseInt(value));
	}

	// Tab interactions
	public static void swapTab (View view, String tab) {
		swapTab ((TabHost)view, Integer.valueOf(tab));
	}

	public static void swapTab (final TabHost tabHost, int num) {
		assertNotNull(tabHost, "Cannon swap tab: the tab host does not exist");
		int count = tabHost.getTabWidget().getTabCount();
		ActivityInstrumentationTestCase2.assertTrue("Cannot swap tab: tab index out of bound", num<=count);
		final int index = Math.min(count, Math.max(1,num)) - 1;
		click (tabHost.getTabWidget().getChildAt(index));
	}

	public static void requestView (final View view) {
		try {
			solo.sendKey(Solo.UP); // Solo.waitForView() requires a widget to be focused		
			solo.waitForView(view, 1000, true);
			requestFocus(view);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}		

	protected static void requestFocus (final View view) {
		runOnUiThread(new Runnable() {
			public void run() {
				view.requestFocus();		
			}
		});
		getInstrumentation().waitForIdleSync();
	}

	// Utility methods
	protected static void runOnUiThread (Runnable action) {
		ExtractorUtilities.getActivity().runOnUiThread(action);		
	}

	public static void wait (int milli) {
		solo.sleep(milli);
	}

	protected static void assertNotNull (final View view, String errorMessage) {
		ActivityInstrumentationTestCase2.assertNotNull(errorMessage, view);
	}

}
