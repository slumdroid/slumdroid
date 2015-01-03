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

package it.slumdroid.utilities.module.androidtest.stats;

import static it.slumdroid.utilities.Resources.NEW_LINE;
import static it.slumdroid.utilities.Resources.TAB;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class StatsReport {

	public abstract String getReport();

	public String toString () {
		return getReport();
	}

	public static int max (int value1, Integer value2) {
		if (value2 == null) return value1;
		return Math.max(value1, value2);
	}

	public static int sum (Map<String,Integer> map) {
		return sum (map.values());
	}

	public static int sum (Collection<Integer> collection) {
		int sum = 0;
		for (Integer i: collection) {
			sum+=i;
		}
		return sum;
	}

	public static void inc (Map<String,Integer> table, String key) {
		if (table.containsKey(key)) {
			table.put(key, table.get(key) + 1);
		} else {
			table.put(key, 1);
		}
	}

	public static String expandMap (Map<String,Integer> map) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String,Integer> entry: map.entrySet()) {
			builder.append(TAB + TAB + entry.getKey().substring(0,1).toUpperCase() 
					+ entry.getKey().substring(1,entry.getKey().length()) 
					+  ": " + entry.getValue() + NEW_LINE);
		}
		return builder.toString();
	}

	public static String expandList (List<String> list) {
		StringBuilder builder = new StringBuilder();
		String separator = new String();
		for (String item: list) {
			builder.append(separator + item);
			separator = ", ";
		}
		return builder.toString();		
	}

}
