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

package it.slumdroid.tool.utilities;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class SessionParams.
 */
public class SessionParams implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2346125751510656206L;

	/** The values. */
	protected Map<String,String> values;

	/**
	 * Instantiates a new session params.
	 */
	public SessionParams() {
		this.values = new Hashtable<String,String>();
	}

	/**
	 * Instantiates a new session params.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public SessionParams(String key, String value) {
		this();
		store(key,value);
	}

	/**
	 * Instantiates a new session params.
	 *
	 * @param key the key
	 * @param number the number
	 */
	public SessionParams(String key, int number) {
		this();
		store(key, number);
	}

	/**
	 * Instantiates a new session params.
	 *
	 * @param key the key
	 * @param number the number
	 */
	public SessionParams(String key, long number) {
		this();
		store(key, number);
	}

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String get(String key) {
		return this.values.get(key);
	}

	/**
	 * Gets the int.
	 *
	 * @param key the key
	 * @return the int
	 */
	public int getInt(String key) {
		return Integer.parseInt(this.values.get(key));
	}

	/**
	 * Gets the long.
	 *
	 * @param key the key
	 * @return the long
	 */
	public long getLong(String key) {
		return Long.parseLong(this.values.get(key));
	}

	/**
	 * Checks for.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean has(String key) {
		return this.values.containsKey(key);
	}

	/**
	 * Store.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void store(String key, String value) {
		this.values.put(key, value);
	}

	/**
	 * Store.
	 *
	 * @param key the key
	 * @param number the number
	 */
	public void store(String key, int number) {
		this.values.put(key, String.valueOf(number));
	}

	/**
	 * Store.
	 *
	 * @param key the key
	 * @param number the number
	 */
	public void store(String key, long number) {
		this.values.put(key, String.valueOf(number));
	}

	/**
	 * Store.
	 *
	 * @param params the params
	 */
	public void store(SessionParams params) {
		this.values.putAll(params.values);
	}

}
