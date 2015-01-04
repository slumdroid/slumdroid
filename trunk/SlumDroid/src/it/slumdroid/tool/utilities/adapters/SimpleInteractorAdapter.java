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

package it.slumdroid.tool.utilities.adapters;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.model.Abstractor;
import it.slumdroid.tool.model.Interactor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleInteractorAdapter.
 */
public abstract class SimpleInteractorAdapter implements Interactor {

	/** The widget classes. */
	protected HashSet<String> widgetClasses = new HashSet<String>();
	
	/** The abstractor. */
	private Abstractor theAbstractor;

	/**
	 * Instantiates a new simple interactor adapter.
	 *
	 * @param simpleTypes the simple types
	 */
	public SimpleInteractorAdapter (String ... simpleTypes) {
		for (String type: simpleTypes) {
			this.widgetClasses.add(type);
		}
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#canUseWidget(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public boolean canUseWidget (WidgetState widget) {
		return widget.isAvailable() && matchClass(widget.getSimpleType());
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getEvents(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserEvent> getEvents (WidgetState widget) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			events.add(generateEvent(widget));
		}
		return events;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getInputs(it.slumdroid.droidmodels.model.WidgetState)
	 */
	public List<UserInput> getInputs (WidgetState widget) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(widget)) {
			inputs.add(generateInput(widget));
		}
		return inputs;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getEvents(it.slumdroid.droidmodels.model.WidgetState, java.lang.String[])
	 */
	public List<UserEvent> getEvents (WidgetState widget, String ... values) {
		ArrayList<UserEvent> events = new ArrayList<UserEvent>();
		if (canUseWidget(widget)) {
			for (String value: values) {
				events.add(generateEvent(widget, value));
			}
		}
		return events;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getInputs(it.slumdroid.droidmodels.model.WidgetState, java.lang.String[])
	 */
	public List<UserInput> getInputs (WidgetState widget, String ... values) {
		ArrayList<UserInput> inputs = new ArrayList<UserInput>();
		if (canUseWidget(widget)) {
			for (String value: values) {
				inputs.add(generateInput(widget, value));    
			}
		}
		return inputs;
	}

	/**
	 * Creates the event.
	 *
	 * @param widget the widget
	 * @return the user event
	 */
	protected UserEvent createEvent (WidgetState widget) {
		return getAbstractor().createEvent(widget, getInteractionType());
	}

	/**
	 * Generate event.
	 *
	 * @param widget the widget
	 * @return the user event
	 */
	protected UserEvent generateEvent (WidgetState widget) {
		return createEvent(widget);
	}

	/**
	 * Generate input.
	 *
	 * @param widget the widget
	 * @return the user input
	 */
	protected UserInput generateInput (WidgetState widget) {
		return generateInput (widget, "");
	}

	/**
	 * Generate event.
	 *
	 * @param widget the widget
	 * @param value the value
	 * @return the user event
	 */
	protected UserEvent generateEvent (WidgetState widget, String value) {
		UserEvent event = createEvent(widget);
		event.setValue(value);
		return event;
	}

	/**
	 * Generate input.
	 *
	 * @param widget the widget
	 * @param value the value
	 * @return the user input
	 */
	protected UserInput generateInput (WidgetState widget, String value) {
		return getAbstractor().createInput(widget, value, getInteractionType());
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getAbstractor()
	 */
	public Abstractor getAbstractor() {
		return this.theAbstractor;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#setAbstractor(it.slumdroid.tool.model.Abstractor)
	 */
	public void setAbstractor (Abstractor abstractor) {
		this.theAbstractor = abstractor;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.model.Interactor#getInteractionType()
	 */
	public abstract String getInteractionType ();

	/**
	 * Match class.
	 *
	 * @param type the type
	 * @return true, if successful
	 */
	protected boolean matchClass (String type) {
		for (String storedType: this.widgetClasses) {
			if (storedType.equals(type)) {
				return true;
			}
		}
		return false;
	}

}