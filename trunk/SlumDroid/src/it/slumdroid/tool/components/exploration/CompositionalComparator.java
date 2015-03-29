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

package it.slumdroid.tool.components.exploration;

import static it.slumdroid.droidmodels.model.SimpleType.BUTTON;
import static it.slumdroid.droidmodels.model.SimpleType.CHECKBOX;
import static it.slumdroid.droidmodels.model.SimpleType.DIALOG_TITLE;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.LIST_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_ITEM;
import static it.slumdroid.droidmodels.model.SimpleType.MENU_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.PREFERENCE_LIST;
import static it.slumdroid.droidmodels.model.SimpleType.TEXT_VIEW;
import static it.slumdroid.droidmodels.model.SimpleType.TOAST;
import static it.slumdroid.tool.Resources.COMPARE_AVAILABLE;
import static it.slumdroid.tool.Resources.COMPARE_CHECKBOX;
import static it.slumdroid.tool.Resources.COMPARE_LIST_COUNT;
import static it.slumdroid.tool.Resources.COMPARE_TITLE;
import static it.slumdroid.tool.Resources.COMPARE_TOAST;
import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.WidgetState;

import java.util.Calendar;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class CompositionalComparator.
 */
public class CompositionalComparator {
	
	private static boolean hasList = false;

	/**
	 * Compare.
	 *
	 * @param currentActivity the current activity
	 * @param storedActivity the stored activity
	 * @return true, if successful
	 */
	public boolean compare(ActivityState currentActivity, ActivityState storedActivity) {
		if (!compareNameAndTitle(currentActivity, storedActivity)) {
			return false;
		}
		hasList = false;
		for (WidgetState field: currentActivity) {	
			if (!lookFor(field, storedActivity)) {
				return false;
			}
		}
		return true; 
	}

	/**
	 * Compare name and title.
	 *
	 * @param currentActivity the current activity
	 * @param storedActivity the stored activity
	 * @return true, if successful
	 */
	private boolean compareNameAndTitle(ActivityState currentActivity, ActivityState storedActivity) {
		if (COMPARE_TITLE) {
			if (!currentActivity.getTitle().equals(storedActivity.getTitle())) {
				return false;
			}	
		}
		if (!currentActivity.getName().equals(storedActivity.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * Look for.
	 *
	 * @param field the field
	 * @param activity the activity
	 * @return true, if successful
	 */
	private boolean lookFor(WidgetState field, ActivityState activity) {
		for (WidgetState otherField: activity) {
			if (matchWidget(otherField, field)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Match widget.
	 *
	 * @param field the field
	 * @param otherField the other field
	 * @return true, if successful
	 */
	private boolean matchWidget(WidgetState field, WidgetState otherField) {       
		boolean compareId = field.getId().equals(otherField.getId());
		boolean compareType = field.getSimpleType().equals(otherField.getSimpleType());
		boolean compareWidget = compareId && compareType;
		boolean isList = field.getSimpleType().equals(LIST_VIEW) || field.getSimpleType().equals(EXPAND_LIST);
		if (isList || field.getSimpleType().equals(EXPAND_MENU)) {
			hasList = true;
		}
		if (COMPARE_AVAILABLE) {
			boolean compareAvailable = field.isAvailable() == otherField.isAvailable();
			if (!hasList) {
				boolean compareIndex = field.getIndex() == otherField.getIndex();
				compareWidget = compareId && compareType && compareAvailable && compareIndex;
			} else {
				compareWidget = compareId && compareType && compareAvailable;
			}
		}
		if (compareWidget) {
			if (field.getSimpleType().equals(TEXT_VIEW)) {
				return field.getValue().isEmpty() == otherField.getValue().isEmpty();   
			}
			boolean compareMenuItem = field.getSimpleType().equals(MENU_ITEM) || field.getSimpleType().equals(EXPAND_MENU_ITEM);
			if (compareMenuItem) {
				boolean compareIndex = field.getIndex() == otherField.getIndex();
				boolean compareValue = field.getValue().equals(otherField.getValue()); 
				return compareValue && compareIndex;
			}
			boolean compareCheck = COMPARE_CHECKBOX && field.getSimpleType().equals(CHECKBOX);
			if (field.getSimpleType().equals(BUTTON) || compareCheck) {
				return field.getValue().equals(otherField.getValue());
			}
			boolean compareCount = field.getSimpleType().equals(MENU_VIEW) || field.getSimpleType().equals(EXPAND_MENU) || field.getSimpleType().equals(PREFERENCE_LIST);
			boolean compareList = COMPARE_LIST_COUNT && isList;
			if (compareCount || compareList) {
				return field.getCount() == otherField.getCount();
			}
			boolean compareTitle = COMPARE_TITLE && field.getSimpleType().equals(DIALOG_TITLE);
			boolean compareToast = COMPARE_TOAST && field.getSimpleType().equals(TOAST);
			if (compareTitle || compareToast) {
				if (!field.getValue().contains(String.valueOf(new GregorianCalendar().get(Calendar.YEAR)))) {
					return field.getValue().equals(otherField.getValue());	
				} 
			}
		}
		return compareWidget;
	}

}