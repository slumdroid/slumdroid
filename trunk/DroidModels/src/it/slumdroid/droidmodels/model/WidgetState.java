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

package it.slumdroid.droidmodels.model;

public interface WidgetState extends WrapperInterface {

	public String getId();
	public void setId(String id);
	public String getName();
	public void setName(String name);
	public String getType();
	public void setType(String type);
	public String getTextType();
	public void setTextType (String inputType);
	public WidgetState clone();
	public String getSimpleType();
	public void setSimpleType(String simpleType);
	public int getCount();
	public void setCount(int count);
	public boolean isAvailable();
	public void setAvailable (String a);
	public boolean isClickable();
	public void setClickable (String c);
	public boolean isLongClickable();
	public void setLongClickable (String lc);
	public int getIndex();
	public String getValue();
	public void setValue (String v);

}