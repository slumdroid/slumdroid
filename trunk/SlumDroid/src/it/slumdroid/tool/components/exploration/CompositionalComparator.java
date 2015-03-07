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
import static it.slumdroid.droidmodels.model.SimpleType.EXPAND_MENU;
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

// TODO: Auto-generated Javadoc
/**
 * The Class CompositionalComparator.
 */
public class CompositionalComparator {

	/**
	 * Compare.
	 *
	 * @param currentActivity the current activity
	 * @param storedActivity the stored activity
	 * @return true, if successful
	 */
	public boolean compare(ActivityState currentActivity, ActivityState storedActivity) {
		if (!compareNameTitle(currentActivity, storedActivity)) {
			return false;
		}
		for (WidgetState field: currentActivity) {	
			if (!lookFor(field, storedActivity)) {
				return false;
			}
		}
		return true; 
	}

	/**
	 * Compare name title.
	 *
	 * @param currentActivity the current activity
	 * @param storedActivity the stored activity
	 * @return true, if successful
	 */
	private boolean compareNameTitle(ActivityState currentActivity, ActivityState storedActivity) {
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
		if (COMPARE_AVAILABLE) {
			boolean compareAvailable = field.isAvailable() == otherField.isAvailable();
			boolean compareIndex = field.getIndex() == otherField.getIndex();
			compareWidget = compareId && compareType && compareAvailable && compareIndex;
		}
		if (compareWidget) {
			if (field.getSimpleType().equals(TEXT_VIEW)) {
				return field.getValue().isEmpty() == otherField.getValue().isEmpty();   
			}
			if (field.getSimpleType().equals(MENU_ITEM)) {
				boolean compareIndex = field.getIndex() == otherField.getIndex();
				boolean compareValue = field.getValue().equals(otherField.getValue()); 
				return compareValue && compareIndex;
			}
			if (field.getSimpleType().equals(BUTTON) 
					|| (COMPARE_TITLE && field.getSimpleType().equals(DIALOG_TITLE))
					|| (COMPARE_CHECKBOX && field.getSimpleType().equals(CHECKBOX))
					|| (COMPARE_TOAST && field.getSimpleType().equals(TOAST))) {
				return field.getValue().equals(otherField.getValue());
			}
			if (field.getSimpleType().equals(MENU_VIEW) 
					|| field.getSimpleType().equals(EXPAND_MENU)
					|| field.getSimpleType().equals(PREFERENCE_LIST)
					|| (COMPARE_LIST_COUNT && field.getSimpleType().equals(LIST_VIEW))) {
				return field.getCount() == otherField.getCount();
			}
		}
		return compareWidget;
	}

}