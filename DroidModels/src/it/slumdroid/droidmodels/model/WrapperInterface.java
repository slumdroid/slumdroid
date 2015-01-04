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

package it.slumdroid.droidmodels.model;

import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Interface WrapperInterface.
 */
public interface WrapperInterface extends Cloneable {

	/**
	 * Sets the element.
	 *
	 * @param element the new element
	 */
	public void setElement (Element element);
	
	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public Element getElement ();
	
	/**
	 * Gets the wrapper.
	 *
	 * @param element the element
	 * @return the wrapper
	 */
	public WrapperInterface getWrapper (Element element);

}
