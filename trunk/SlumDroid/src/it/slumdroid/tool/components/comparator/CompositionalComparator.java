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

package it.slumdroid.tool.components.comparator;

import it.slumdroid.tool.model.Comparator;

import java.util.HashSet;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.WidgetState;

import static it.slumdroid.tool.Resources.*;
import static it.slumdroid.droidmodels.model.SimpleType.*;

public class CompositionalComparator implements Comparator {

	private boolean matchClass (String type) {
		for (String storedType: WIDGET_TYPES) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

	private boolean matchWidget (WidgetState field, WidgetState otherField) {	
		boolean compareId = otherField.getId().equals(field.getId());
		boolean compareType = otherField.getSimpleType().equals(field.getSimpleType());		
		boolean compareWidget = compareId && compareType;
		if (compareWidget){ 
			boolean isDialog = field.getSimpleType().equals(DIALOG_TITLE);
			if (isDialog) return otherField.getName().equals(field.getName());
			boolean isList = field.getSimpleType().equals(LIST_VIEW) || field.getSimpleType().equals(PREFERENCE_LIST);	
			boolean isMenu = field.getSimpleType().equals(MENU_VIEW) || field.getSimpleType().equals(EXPAND_MENU);
			if (isMenu || (isList && COMPARE_LIST_COUNT)) return otherField.getCount() == field.getCount();
		}
		return compareWidget;
	}

	@Override
	public boolean compare(ActivityState currentActivity, ActivityState storedActivity) {
		if (!compareNameTitle(currentActivity, storedActivity)) return false; // Different name, different activity, early return
		HashSet<String> checkedAlready = new HashSet<String>();
		// Check if current has at least one widget that stored ain't got
		for (WidgetState field: currentActivity) {
			int id = Integer.valueOf(field.getId());
			if (matchClass(field.getSimpleType()) && (id>0) ) {
				if (!lookFor(field, storedActivity)) return false;
				checkedAlready.add(field.getId()); // store widgets checked in this step to skip them in the next step
			}
		}
		// Check if stored has at least one widget that current ain't got. Skip if already checked.
		for (WidgetState field: storedActivity) {
			int id = Integer.valueOf(field.getId());
			if ( matchClass(field.getSimpleType()) && (id>0) && (!checkedAlready.contains(field.getId())) ) {
				if (!lookFor(field, currentActivity)) return false;
			}
		}
		return true; // All tests failed, can't found a difference between current and stored!
	}

	private boolean lookFor (WidgetState field, ActivityState activity) {
		for (WidgetState otherField: activity) {
			if (matchWidget (otherField, field)) {
				return true;
			}
		}
		return false;
	}

	private boolean compareNameTitle(ActivityState currentActivity, ActivityState storedActivity){
		if (!currentActivity.getTitle().equals(storedActivity.getTitle())) return false;
		if (!currentActivity.getName().equals(storedActivity.getName())) return false;
		return true;
	}

}