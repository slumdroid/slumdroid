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

package it.slumdroid.tool.utilities.interactors.editor;

import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.AUTOC_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.EDIT_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AdditionalWriteEditor extends SimpleInteractorAdapter {

	private Map<String,ArrayList<String>> idValuePairs = new Hashtable<String,ArrayList<String>>();

	public AdditionalWriteEditor () {
		this (EDIT_TEXT, AUTOC_TEXT, SEARCH_BAR);
	}

	public AdditionalWriteEditor (String ... simpleTypes) {
		super (simpleTypes);
	}

	@Override
	public boolean canUseWidget(WidgetState widget) {
		return super.canUseWidget(widget) && hasId(widget.getId());
	}

	public boolean hasId (String id) {
		for (Map.Entry<String,ArrayList<String>> entry: this.idValuePairs.entrySet()) {
			if (id.equals(entry.getKey())) return true;
		}
		return false;
	}

	@Override
	public List<UserEvent> getEvents (WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			ArrayList<String> values = this.idValuePairs.get(widget.getId());
			for (String value: values) {
				events.add(generateEvent(widget, value));
			}
		}
		return events;
	}

	@Override
	public List<UserInput> getInputs (WidgetState widget) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(widget)) {
			ArrayList<String> values = this.idValuePairs.get(widget.getId());
			for (String value: values) {
				inputs.add(generateInput(widget, value));
			}
		}
		return inputs;
	}

	public String getInteractionType() {
		return WRITE_TEXT;
	}

	public void setIdValuePairs (Map<String,ArrayList<String>> pairs) {
		this.idValuePairs = pairs;
	}

	public AdditionalWriteEditor addIdValuePair (String id, String ... values) {
		ArrayList<String> valuesForId;
		if (!hasId(id)) {
			valuesForId = new ArrayList<String>();
			this.idValuePairs.put (id, valuesForId);
		} else {
			valuesForId = this.idValuePairs.get (id);
		}
		for (String value: values) {
			valuesForId.add(value);			
		}
		return this;
	}

}
