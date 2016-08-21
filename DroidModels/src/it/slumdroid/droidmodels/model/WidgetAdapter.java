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
 * The Class WidgetAdapter.
 */
public class WidgetAdapter implements WidgetState {

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#setElement(org.w3c.dom.Element)
	 */
	@Override
	public void setElement(Element e) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getElement()
	 */
	@Override
	public Element getElement() {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WrapperInterface#getWrapper(org.w3c.dom.Element)
	 */
	@Override
	public WrapperInterface getWrapper(Element element) {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getId()
	 */
	@Override
	public String getId() {
		return new String();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setId(java.lang.String)
	 */
	public void setId(String id) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getName()
	 */
	@Override
	public String getName() {
		return new String();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setName(java.lang.String)
	 */
	public void setName(String name) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getType()
	 */
	@Override
	public String getType() {
		return new String();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setType(java.lang.String)
	 */
	public void setType(String type) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getSimpleType()
	 */
	public String getSimpleType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setSimpleType(java.lang.String)
	 */
	public void setSimpleType(String simpleType) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getTextType()
	 */
	public String getTextType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setTextType(java.lang.String)
	 */
	public void setTextType(String inputType) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isAvailable()
	 */
	public boolean isAvailable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setAvailable(java.lang.String)
	 */
	public void setAvailable(String value) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getCount()
	 */
	public int getCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setCount(int)
	 */
	public void setCount(int count) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getIndex()
	 */
	public int getIndex() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setIndex(int)
	 */
	public void setIndex(int index) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isClickable()
	 */
	public boolean isClickable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setClickable(java.lang.String)
	 */
	public void setClickable(String value) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#isLongClickable()
	 */
	public boolean isLongClickable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setLongClickable(java.lang.String)
	 */
	public void setLongClickable(String value) {}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#getValue()
	 */
	public String getValue() {
		return new String();
	}

	/* (non-Javadoc)
	 * @see it.slumdroid.droidmodels.model.WidgetState#setValue(java.lang.String)
	 */
	public void setValue(String value) {}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public WidgetState clone() {
		return null;
	}

}