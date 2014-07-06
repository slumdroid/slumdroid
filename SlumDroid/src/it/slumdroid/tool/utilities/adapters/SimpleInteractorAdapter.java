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

import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.Interactor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;

public abstract class SimpleInteractorAdapter implements Interactor {

	protected HashSet<String> widgetClasses = new HashSet<String>();
	private Abstractor theAbstractor;

	public SimpleInteractorAdapter (String ... simpleTypes) {
		for (String s: simpleTypes) {
			this.widgetClasses.add(s);
		}
	}

	public boolean canUseWidget (WidgetState w) {
		return (w.isAvailable() && matchClass(w.getSimpleType()));
	}

	public List<UserEvent> getEvents (WidgetState w) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			events.add(generateEvent(w));
		}
		return events;
	}

	public List<UserInput> getInputs (WidgetState w) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(w)) {
			inputs.add(generateInput(w));
		}
		return inputs;
	}

	public List<UserEvent> getEvents (WidgetState w, String ... values) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(w)) {
			for (String value: values) {
				events.add(generateEvent(w, value));
			}
		}
		return events;
	}

	public List<UserInput> getInputs (WidgetState w, String ... values) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(w)) {
			for (String value: values) {
				inputs.add(generateInput(w, value));    
			}
		}
		return inputs;
	}

	protected UserEvent createEvent (WidgetState w) {
		return getAbstractor().createEvent(w, getInteractionType());
	}

	protected UserEvent generateEvent (WidgetState w) {
		return createEvent(w);
	}

	protected UserInput generateInput (WidgetState w) {
		return generateInput (w,"");
	}

	protected UserEvent generateEvent (WidgetState w, String value) {
		UserEvent event = createEvent(w);
		event.setValue(value);
		return event;
	}

	protected UserInput generateInput (WidgetState w, String value) {
		return getAbstractor().createInput(w, value, getInteractionType());
	}

	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}

	public void setAbstractor (Abstractor a) {
		this.theAbstractor = a;
	}

	public abstract String getInteractionType ();

	protected boolean matchClass (String type) {
		for (String storedType: this.widgetClasses) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}

}