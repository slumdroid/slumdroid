/* This file is part of SlumDroid <https://github.com/slumdroid/slumdroid>.
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
 * Copyright (C) 2012-2016 Gennaro Imparato
 */

package it.slumdroid.tool.utilities.interactors.editor;

import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;
import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class HashWriteEditor.
 */
public class HashWriteEditor extends SimpleInteractorAdapter {

	/**
	 * Instantiates a new hash write editor.
	 *
	 * @param simpleTypes the simple types
	 */
	public HashWriteEditor(String ... simpleTypes) {
		super(simpleTypes);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInteractionType()
	 */
	public String getInteractionType() {
		return WRITE_TEXT;
	}

	/**
	 * Gets the values.
	 *
	 * @param widget the widget
	 * @return the values
	 */
	public String[] getValues(WidgetState widget) {
		String[] values = new String[1];                
		values[0] = Integer.toString(widget.getId().hashCode() % 100);
		return values;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getEvents(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserEvent> getEvents(WidgetState widget) {
		return getEvents(widget, getValues(widget));
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter#getInputs(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserInput> getInputs(WidgetState widget) {
		return getInputs(widget, getValues(widget));
	}

}
