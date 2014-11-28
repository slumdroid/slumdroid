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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.Interactor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class SimpleInteractorAdapter implements Interactor {

	protected HashSet<String> widgetClasses = new HashSet<String>();
	private Abstractor theAbstractor;

	public SimpleInteractorAdapter (String ... simpleTypes) {
		for (String type: simpleTypes) {
			this.widgetClasses.add(type);
		}
	}

	public boolean canUseWidget (WidgetState widget) {
		return widget.isAvailable() && matchClass(widget.getSimpleType());
	}

	public List<UserEvent> getEvents (WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			events.add(generateEvent(widget));
		}
		return events;
	}

	public List<UserInput> getInputs (WidgetState widget) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(widget)) {
			inputs.add(generateInput(widget));
		}
		return inputs;
	}

	public List<UserEvent> getEvents (WidgetState widget, String ... values) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			for (String value: values) {
				events.add(generateEvent(widget, value));
			}
		}
		return events;
	}

	public List<UserInput> getInputs (WidgetState widget, String ... values) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(widget)) {
			for (String value: values) {
				inputs.add(generateInput(widget, value));    
			}
		}
		return inputs;
	}

	protected UserEvent createEvent (WidgetState widget) {
		return getAbstractor().createEvent(widget, getInteractionType());
	}

	protected UserEvent generateEvent (WidgetState widget) {
		return createEvent(widget);
	}

	protected UserInput generateInput (WidgetState widget) {
		return generateInput (widget,"");
	}

	protected UserEvent generateEvent (WidgetState widget, String value) {
		UserEvent event = createEvent(widget);
		event.setValue(value);
		return event;
	}

	protected UserInput generateInput (WidgetState widget, String value) {
		return getAbstractor().createInput(widget, value, getInteractionType());
	}

	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}

	public void setAbstractor (Abstractor abstractor) {
		this.theAbstractor = abstractor;
	}

	public abstract String getInteractionType ();

	protected boolean matchClass (String type) {
		for (String storedType: this.widgetClasses) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

}