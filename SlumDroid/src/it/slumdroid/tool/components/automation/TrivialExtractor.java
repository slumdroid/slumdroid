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

// import static it.slumdroid.tool.Resources.TAG;
import static it.slumdroid.tool.components.automation.DroidExecutor.sync;
import it.slumdroid.tool.model.ActivityDescription;
import it.slumdroid.tool.model.Extractor;
import it.slumdroid.tool.model.ImageCaptor;
import it.slumdroid.tool.utilities.ExtractorUtilities;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Bitmap;
// import android.util.Log;
import android.util.SparseArray;
import android.view.View;
// import android.widget.TextView;


import com.robotium.solo.Solo;

public class TrivialExtractor implements Extractor, ImageCaptor {
	
	private SparseArray<View> theViews = new SparseArray<View> ();
	private ArrayList<View> allViews = new ArrayList<View>();
	
	public Solo solo;

	public void extractState() {
		retrieveWidgets();
	}

	public View getWidget (int key) {
		return getWidgets().get(key);
	}

	public Activity getActivity() {
		return ExtractorUtilities.getActivity();
	}

	public ActivityDescription describeActivity() {
		return new ActivityDescription() {

			public Iterator<View> iterator() {
				return getAllWidgets().iterator();
			}

			public int getWidgetIndex(View view) {
				return getAllWidgets().indexOf(view);
			}

			public String getActivityName() {
				return getActivity().getClass().getSimpleName();
			}

			public String getActivityTitle() {
				return getActivity().getTitle().toString();
			}

			public String toString() {
				return getActivityName();
			}

		};
	}

	public Bitmap captureImage() {
		ArrayList<View> views = this.solo.getViews();
		Bitmap bitmap = null;
		try{
			if (views != null && views.size() > 0) {
				final View view = views.get(0);
				final boolean flag = view.isDrawingCacheEnabled();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (!flag) {
							view.setDrawingCacheEnabled(true);
						}
						view.buildDrawingCache();
					}
				});
				sync();
				bitmap = view.getDrawingCache();
				bitmap = bitmap.copy(bitmap.getConfig(), false);
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (!flag) {
							view.setDrawingCacheEnabled(false);
						}
					}
				});
				return bitmap;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		bitmap.recycle();
		return null;
	}
	
	public SparseArray<View> getWidgets () {
		return this.theViews;
	}

	public ArrayList<View> getAllWidgets () {
		return this.allViews;
	}
	
	public void clearWidgetList() {
		this.theViews.clear();
		this.allViews.clear();		
	}
	
	public void retrieveWidgets () {
		DroidExecutor.home();
		clearWidgetList();
		ArrayList<View> viewList = this.solo.getViews();
		if (viewList.size() != 0){
			//Log.d(TAG, "Retrieving widgets");
			//Log.d(TAG, "Found widget:");
			for (View view: viewList) {
				this.allViews.add(view);
				//String text = (view instanceof TextView)?": " + ((TextView)view).getText().toString():"";
				//Log.d(TAG, "id=" + view.getId() + " (" + view.toString().split("@")[0] + ")" + text);
				if (view.getId() > 0) {
					this.theViews.put(view.getId(), view); // Add only if the widget has a valid ID
				}
			}	
		}
	}

}
