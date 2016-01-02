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

import static it.slumdroid.droidmodels.model.InteractionType.ENTER_TEXT;
import static it.slumdroid.droidmodels.model.SimpleType.SEARCH_BAR;

// TODO: Auto-generated Javadoc
/**
 * The Class HashEnterEditor.
 */
public class HashEnterEditor extends HashWriteEditor {

	/**
	 * Instantiates a new hash enter editor.
	 */
	public HashEnterEditor() {
		super(SEARCH_BAR);
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.tool.utilities.interactors.editor.HashWriteEditor#getInteractionType()
	 */
	public String getInteractionType() {
		return ENTER_TEXT;
	}

}
