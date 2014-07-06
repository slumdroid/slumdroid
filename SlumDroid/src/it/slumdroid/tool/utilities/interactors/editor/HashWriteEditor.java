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

import java.util.List;

import it.slumdroid.droidmodels.model.UserEvent;
import it.slumdroid.droidmodels.model.UserInput;
import it.slumdroid.droidmodels.model.WidgetState;
import it.slumdroid.tool.utilities.adapters.SimpleInteractorAdapter;
import static it.slumdroid.droidmodels.model.InteractionType.WRITE_TEXT;

public class HashWriteEditor extends SimpleInteractorAdapter {

	public HashWriteEditor (String ... simpleTypes) {
		super (simpleTypes);
	}

	public String getInteractionType() {
		return WRITE_TEXT;
	}
	
	public String[] getValues(WidgetState w){
            String[] values = new String[1];                
            values[0] = Integer.toString(w.getId().hashCode() % 100);
            return values;
    }
    
    public List<UserEvent> getEvents (WidgetState w) {
            return getEvents (w, getValues(w));
    }

    public List<UserInput> getInputs (WidgetState w) {
            return getInputs (w, getValues(w));
    }

}
