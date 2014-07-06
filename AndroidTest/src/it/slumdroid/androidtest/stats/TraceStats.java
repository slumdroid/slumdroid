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

package it.slumdroid.androidtest.stats;

import java.util.ArrayList;
import java.util.List;

import it.slumdroid.droidmodels.model.ActivityState;
import it.slumdroid.droidmodels.model.Trace;

public class TraceStats extends StatsReport {

	private int traces = 0;
	private int tracesSuccessful = 0;
	private int tracesFailed = 0;
	private int tracesCrashed = 0;
	private int tracesExit = 0;
	private List<String> crashes;
	private List<String> failures;
	private List<String> exits;

	public TraceStats() {
		crashes = new ArrayList<String>();
		failures = new ArrayList<String>();
		exits = new ArrayList<String>();
	}

	public void analyzeTrace (Trace theTrace) {
		this.traces++;
		ActivityState a = theTrace.getFinalTransition().getFinalActivity();
		String txt;
		if (a.isFailure()) {
			this.tracesFailed++;
			txt = theTrace.getId();
			this.failures.add(txt);
		} else if (a.isCrash()) {
			this.tracesCrashed++;
			txt = theTrace.getId();
			this.crashes.add(txt);
		} else if (a.isExit()) {
			this.tracesExit++;
			txt = theTrace.getId();
			this.exits.add(txt);
		} else {
			this.tracesSuccessful++;
		}
	}

	public int getTraces() {
		return traces;
	}

	public int getTracesSuccessful() {
		return tracesSuccessful;
	}

	public int getTracesFailed() {
		return tracesFailed;
	}

	public int getTracesExit() {
		return tracesExit;
	}

	public int getTracesCrashed() {
		return tracesCrashed;
	}

	public String printList (List<String> list) {
		return ((list.size()>0)?(TAB + TAB + "traces: " + expandList(list) + NEW_LINE):"");
	}

	public String getReport() {
		return "Traces processed: " + getTraces() + NEW_LINE + 
				TAB + "success: " + getTracesSuccessful() + NEW_LINE + 
				TAB + "fail: " + getTracesFailed() + NEW_LINE + 
				printList (this.failures) +
				TAB + "crash: " + getTracesCrashed() + NEW_LINE +
				printList (this.crashes) +
				TAB + "exit: " + getTracesExit() + NEW_LINE;
	}

}
