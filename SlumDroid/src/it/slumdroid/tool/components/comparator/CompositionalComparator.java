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

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.WidgetState;

import static it.slumdroid.tool.Resources.*;
import static it.slumdroid.droidmodels.model.SimpleType.*;

public class CompositionalComparator implements Comparator {

	@Override
	public boolean compare(ActivityState currentActivity, ActivityState storedActivity) {
		if (!compareNameTitle(currentActivity, storedActivity)) return false; 
		for (WidgetState field: currentActivity) {
			if (matchClass(field.getSimpleType())) {
				if (!lookFor(field, storedActivity)) return false;
			}
		}
		return true; 
	}
	
	private boolean compareNameTitle(ActivityState currentActivity, ActivityState storedActivity){
		if (!currentActivity.getTitle().equals(storedActivity.getTitle())) return false;
		if (!currentActivity.getName().equals(storedActivity.getName())) return false;
		return true;
	}
	
	private boolean matchClass (String type) {
		for (String storedType: WIDGET_TYPES) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

	private boolean lookFor (WidgetState field, ActivityState activity) {
		for (WidgetState otherField: activity) {
			if (matchWidget (otherField, field)) {
				return true;
			}
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

}