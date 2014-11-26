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

package it.slumdroid.tool.model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class SessionParams implements Serializable {

	private static final long serialVersionUID = 2346125751510656206L;
	protected Map<String,String> values;

	public SessionParams () {
		this.values = new Hashtable<String,String>();
	}

	public SessionParams (String key, String value) {
		this();
		store (key,value);
	}

	public SessionParams (String key, int number) {
		this();
		store (key, number);
	}

	public SessionParams (String key, long number) {
		this();
		store (key, number);
	}

	public String get (String key) {
		return this.values.get(key);
	}

	public int getInt (String key) {
		return Integer.parseInt(this.values.get(key));
	}

	public long getLong (String key) {
		return Long.parseLong(this.values.get(key));
	}

	public boolean has (String key) {
		return this.values.containsKey(key);
	}

	public void store (String key, String value) {
		this.values.put(key, value);
	}

	public void store (String key, int number) {
		this.values.put(key, String.valueOf(number));
	}

	public void store (String key, long number) {
		this.values.put(key, String.valueOf(number));
	}

	public void store (SessionParams p) {
		this.values.putAll(p.values);
	}

}
