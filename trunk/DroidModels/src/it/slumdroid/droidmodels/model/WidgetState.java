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

// TODO: Auto-generated Javadoc
/**
 * The Interface WidgetState.
 */
public interface WidgetState extends WrapperInterface {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId();

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id);

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name);

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType();

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type);

	/**
	 * Gets the text type.
	 *
	 * @return the text type
	 */
	public String getTextType();

	/**
	 * Sets the text type.
	 *
	 * @param inputType the new text type
	 */
	public void setTextType(String inputType);

	/**
	 * Gets the simple type.
	 *
	 * @return the simple type
	 */
	public String getSimpleType();

	/**
	 * Sets the simple type.
	 *
	 * @param simpleType the new simple type
	 */
	public void setSimpleType(String simpleType);

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount();

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(int count);

	/**
	 * Checks if is available.
	 *
	 * @return true, if is available
	 */
	public boolean isAvailable();

	/**
	 * Sets the available.
	 *
	 * @param value the new available
	 */
	public void setAvailable(String value);

	/**
	 * Checks if is clickable.
	 *
	 * @return true, if is clickable
	 */
	public boolean isClickable();

	/**
	 * Sets the clickable.
	 *
	 * @param value the new clickable
	 */
	public void setClickable(String value);

	/**
	 * Checks if is long clickable.
	 *
	 * @return true, if is long clickable
	 */
	public boolean isLongClickable();

	/**
	 * Sets the long clickable.
	 *
	 * @param value the new long clickable
	 */
	public void setLongClickable(String value);

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex();

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(int index);

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue();

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value);

	/**
	 * Clone.
	 *
	 * @return the widget state
	 */
	public WidgetState clone();

}