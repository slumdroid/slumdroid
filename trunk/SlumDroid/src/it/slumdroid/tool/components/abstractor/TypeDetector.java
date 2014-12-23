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

package it.slumdroid.tool.components.abstractor;

import static it.slumdroid.droidmodels.model.SimpleType.ACTION_HOME;
import static it.slumdroid.droidmodels.model.SimpleType.AUTOC_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKBOX;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKTEXT;
import static it.slumdroid.droidmodels.model.SimpleType.DATE_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.DIALOG_TITLE;
import static it.slumdroid.droidmodels.model.SimpleType.EDIT_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.EMPTY_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.EMPTY_SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.IMAGE_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.LINEAR_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.NOEDITABLE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.NUMBER_PICKER_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.POPUP_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.POPUP_WINDOW;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.PROGRESS_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO;
import static it.slumdroid.droidmodels.model.SimpleType.RADIO_GROUP;
import static it.slumdroid.droidmodels.model.SimpleType.RATING_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.RELATIVE_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.SCROLL_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SEEK_BAR;
import static it.slumdroid.droidmodels.model.SimpleType.SLIDING_DRAWER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER;
import static it.slumdroid.droidmodels.model.SimpleType.SPINNER_INPUT;
import static it.slumdroid.droidmodels.model.SimpleType.TABLE_LAYOUT;
import static it.slumdroid.droidmodels.model.SimpleType.TABLE_ROW;
import static it.slumdroid.droidmodels.model.SimpleType.TAB_HOST;
import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TIME_PICKER;
import static it.slumdroid.droidmodels.model.SimpleType.TOGGLE_BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.WEB_VIEW;
import android.view.View;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressWarnings("deprecation")
public class TypeDetector {
	
	private String type = new String(); 
	
	public String getSimpleType(View view) {
		type = view.getClass().getName();
		if (type.endsWith("TableRow")) {
			return TABLE_ROW;
		}
		if (type.endsWith("TwoLineListItem")) {
			return LIST_ITEM;
		}
		if (type.endsWith("DialogTitle")) {
			return DIALOG_TITLE;
		}
		if (type.endsWith("NumberPickerButton")) {
			return NUMBER_PICKER_BUTTON;
		}
		if (type.endsWith("Layout")){
			return detectLayout();
		}
		if (type.endsWith("View")){
			return detectView(view);
		}
		if (view instanceof TextView){
			return detectTextView(view);
		}
		if (view instanceof ImageButton 
				|| type.endsWith("Button")) {
			return BUTTON;
		}
		if (view instanceof Spinner){
			return detectSpinner(view);
		}
		if (type.endsWith("Picker")){
			return detectPicker();
		}
		if (type.endsWith("PopupMenu") 
				|| type.contains("PopupMenu")) {
			return POPUP_MENU;
		}
		if (type.endsWith("PopupWindow") 
				|| type.contains("PopupWindow") 
				|| type.endsWith("PopupViewContainer")) {
			return POPUP_WINDOW;
		}
		if (view instanceof RadioGroup) {
			return RADIO_GROUP;
		}
		if (view instanceof ProgressBar 
				|| type.endsWith("ProgressBar")) {
			return detectProgressBar(view);
		}
		if (view instanceof TabHost) {
			return TAB_HOST;
		}
		if (view instanceof SlidingDrawer) {
			return SLIDING_DRAWER; // Deprecated in API level 17
		}
		if (type.endsWith("Container")) {
			return "Container"; // Generic Container
		}
		return new String(); // unKnown Widget or Custom Widget
	}
	
	private String detectLayout() {
		if (type.endsWith("LinearLayout")) {
			return LINEAR_LAYOUT;
		}
		if (type.endsWith("RelativeLayout")) {
			return RELATIVE_LAYOUT;
		}
		if (type.endsWith("TableLayout")) {
			return TABLE_LAYOUT;
		}
		return "Layout"; // Generic Layout
	}

	private String detectView(View view) {
		if (type.endsWith("ExpandedMenuView")) {
			return EXPAND_MENU;
		}
		if (type.endsWith("HomeView")) {
			return ACTION_HOME;
		}
		if (type.endsWith("MenuView")) {
			return MENU_VIEW;
		}
		if (type.endsWith("MenuItemView")) {
			return MENU_ITEM;
		}
		if (view instanceof TextView || type.endsWith("TextView")){
			return detectTextView(view);
		}
		if (view instanceof ImageView 
				|| type.endsWith("ImageView")) {
			return IMAGE_VIEW;
		}
		if (view instanceof ListView 
				|| type.endsWith("ListView")) {
			return detectList(view);
		}
		if (view instanceof ScrollView){
			return SCROLL_VIEW;
		}
		if (view instanceof WebView 
				|| type.endsWith("WebView")) {
			return WEB_VIEW;
		}
		return "View"; // Generic View
	}
	
	private String detectTextView(View view) {
		if (view instanceof EditText){
			return detectEdit(view);
		}
		if (view instanceof Button){
			return detectButton(view);
		}
		if (view instanceof CheckedTextView) {
			return CHECKTEXT;
		}
		return TEXT_VIEW;
	}
	
	private String detectEdit(View view) {
		if (view instanceof AutoCompleteTextView){
			if (type.endsWith("SearchAutoComplete")) {
				return SEARCH_BAR;
			}
			return AUTOC_TEXT;
		}
		if (((EditText) view).getKeyListener() == null) {
			return NOEDITABLE_TEXT;
		}
		return EDIT_TEXT;
	}

	private String detectButton(View view) {
		if (view instanceof CheckBox) {
			return CHECKBOX;
		}
		if (view instanceof ToggleButton) {
			return TOGGLE_BUTTON;
		}
		if (view instanceof RadioButton) {
			return RADIO;	
		}
		return BUTTON;
	}
	
	private String detectList(View view) {
		if (type.endsWith("RecycleListView")) {
			return EXPAND_MENU;
		}
		ListView list = (ListView)view;
		if (list.getCount() == 0) {
			return EMPTY_LIST;
		}
		if (list.getAdapter().getClass().getName().endsWith("PreferenceGroupAdapter")) {
			return PREFERENCE_LIST;
		}
		return LIST_VIEW;
	}

	private String detectSpinner(View view) {
		if (((Spinner)view).getCount() == 0) {
			return EMPTY_SPINNER;
		}
		if (((Spinner)view).getOnItemClickListener() != null 
				|| ((Spinner)view).getOnItemLongClickListener() != null
				|| ((Spinner)view).getOnItemSelectedListener() != null) {
			return SPINNER;
		}
		return SPINNER_INPUT;
	}
	
	private String detectPicker() {
		if (type.endsWith("DatePicker")) {
			return DATE_PICKER;
		}
		if (type.endsWith("TimePicker")) {
			return TIME_PICKER;
		}
		if (type.endsWith("NumberPicker")) {
			return NUMBER_PICKER;
		}
		return "Picker"; // Generic Picker
	}
	
	private String detectProgressBar(View view) {
		if (view instanceof RatingBar 
				&& (!((RatingBar)view).isIndicator())) {
			return RATING_BAR;
		}
		if (view instanceof SeekBar) {
			return SEEK_BAR;
		}
		return PROGRESS_BAR;
	}

}