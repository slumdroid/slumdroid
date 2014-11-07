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

package it.slumdroid.tool.utilities;

import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import it.slumdroid.tool.model.TypeDetector;
import static it.slumdroid.droidmodels.model.SimpleType.*;

public class SimpleTypeDetector implements TypeDetector {

	@SuppressWarnings("deprecation")
	public String getSimpleType(View view) {
		String type = view.getClass().getName();
		if (type.endsWith("TableRow")) return TABLE_ROW;
		if (type.endsWith("TwoLineListItem")) return LIST_ITEM;
		if (type.endsWith("DialogTitle")) return DIALOG_TITLE;
		if (type.endsWith("NumberPickerButton")) return NUMBER_PICKER_BUTTON;
		if (type.endsWith("Layout")){
			if (type.endsWith("LinearLayout")) return LINEAR_LAYOUT;
			if (type.endsWith("RelativeLayout")) return RELATIVE_LAYOUT;
			if (type.endsWith("TableLayout")) return TABLE_LAYOUT;
			return "layout";
		}
		if (type.endsWith("View")){
			if (type.endsWith("ExpandedMenuView") || type.endsWith("RecycleListView")) return EXPAND_MENU;
			if (type.endsWith("HomeView")) return ACTION_HOME;
			if (type.endsWith("MenuView")) return MENU_VIEW;
			if (type.endsWith("MenuItemView")) return MENU_ITEM;
			if (view instanceof TextView || type.endsWith("TextView")){
				if (view instanceof AutoCompleteTextView || type.endsWith("AutoCompleteTextView")) return AUTOC_TEXT;
				if (view instanceof CheckedTextView 	 || type.endsWith("CheckedTextView")) return CHECKTEXT;
				return TEXT_VIEW;
			}
			if (view instanceof ImageView || type.endsWith("ImageView")) return IMAGE_VIEW;
			if (view instanceof ListView  || type.endsWith("ListView")) {
				ListView list = (ListView)view;
				if (list.getCount() == 0) return EMPTY_LIST;
				if (list.getAdapter().getClass().getName().endsWith("PreferenceGroupAdapter")) return PREFERENCE_LIST;
				return LIST_VIEW;
			}
			if (view instanceof WebView || type.endsWith("WebView")) return WEB_VIEW;
			return "view";
		}
		if (view instanceof TextView){
			if (view instanceof EditText){
				if (view instanceof AutoCompleteTextView){
					if (type.endsWith("SearchAutoComplete")) return SEARCH_BAR;
					return AUTOC_TEXT;
				}
				if (((EditText) view).getKeyListener() == null) return NOEDITABLE_TEXT;
				return EDIT_TEXT;
			}
			if (view instanceof Button){
				if (view instanceof CheckBox) return CHECKBOX;
				if (view instanceof ToggleButton) return TOGGLE_BUTTON;
				if (view instanceof RadioButton) return RADIO;	
				return BUTTON;
			}
			if (view instanceof CheckedTextView) return CHECKTEXT;
			return TEXT_VIEW;
		}
		if (view instanceof ImageButton || type.endsWith("Button")) return BUTTON;

		if (view instanceof Spinner){
			if (((Spinner)view).getCount() == 0) return EMPTY_SPINNER;
			if (((Spinner)view).getOnItemClickListener() != null 
					|| ((Spinner)view).getOnItemLongClickListener()!= null
					|| ((Spinner)view).getOnItemSelectedListener()!= null) return SPINNER;
			return SPINNER_INPUT;
		}
		if (type.endsWith("Picker")){
			if (type.endsWith("DatePicker")) return DATE_PICKER;
			if (type.endsWith("TimePicker")) return TIME_PICKER;
			if (type.endsWith("NumberPicker")) return NUMBER_PICKER;
			return "Picker";
		}
		if (type.endsWith("PopupMenu") || type.contains("PopupMenu")) return POPUP_MENU;
		if (type.endsWith("PopupWindow") || type.contains("PopupWindow") || type.endsWith("PopupViewContainer")) return POPUP_WINDOW;
		if (type.endsWith("Container")) return "Container";
		
		if (view instanceof RadioGroup) return RADIO_GROUP;
		
		if (view instanceof ProgressBar || type.endsWith("ProgressBar")) {
			if (view instanceof RatingBar && (!((RatingBar)view).isIndicator())) return RATING_BAR;
			if (view instanceof SeekBar) return SEEK_BAR;
			return PROGRESS_BAR;
		}
		if (view instanceof TabHost) return TAB_HOST;
		if (view instanceof SlidingDrawer) return SLIDING_DRAWER; // Deprecated in API level 17
		return "";
	}

}