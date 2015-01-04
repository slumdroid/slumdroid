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

public class WidgetAdapter implements WidgetState {

	@Override
	public void setElement(Element e) {}

	@Override
	public Element getElement() {
		return null;
	}

	@Override
	public WrapperInterface getWrapper(Element element) {
		return null;
	}

	@Override
	public String getId() {
		return new String();
	}

	public void setId(String id) {}

	@Override
	public String getName() {
		return new String();
	}

	public void setName(String name) {}

	@Override
	public String getType() {
		return new String();
	}

	public void setType(String type) {}

	public String getUniqueId() {
		return new String();
	}

	public void setUniqueId(String id) {}

	public String getSimpleType() {
		return null;
	}
	
	public void setSimpleType(String simpleType) {}

	public String getTextType() {
		return null;
	}

	public void setTextType(String inputType) {}

	public boolean isAvailable() {
		return true;
	}

	public void setAvailable(String value) {}

	public int getCount() {
		return 1;
	}

	public void setCount(int count) {}

	public int getIndex() {
		return 0;
	}

	public void setIndex(int index) {}

	public boolean isClickable() {
		return true;
	}

	public void setClickable(String value) {}
	
	public boolean isLongClickable() {
		return false;
	}

	public void setLongClickable(String value) {}

	public String getValue() {
		return new String();
	}

	public void setValue(String value) {}
	
	public WidgetState clone() {
		return null;
	}

}