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

package it.slumdroid.wizard.tools;

import static it.slumdroid.wizard.tools.CommandLine.CLOSE;

import java.util.ArrayList;

public class ExternalProcess {

	private Process process;
	private String command;
	static private ArrayList<Process> runningProcs = new ArrayList<Process>();

	public ExternalProcess () {}

	public ExternalProcess (String c) {
		load(c);
	}

	public void load (String commandList) {
		this.command = commandList;
	}

	public void execute () {
		try {
			this.process = Runtime.getRuntime().exec(this.command);
			runningProcs.add(this.process);
			StreamGobbler.fromProcess(this.process);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ExternalProcess executeCommand (String command) {
		ExternalProcess theNewOne = new ExternalProcess(command);
		theNewOne.execute();
		return theNewOne;
	}

	public static ExternalProcess execute (String command, String ... params) {
		String commandLine = CommandLine.get(command, params);
		System.err.println(commandLine);
		return executeCommand (commandLine);
	}

	public void kill() {
		if (process != null) {
			process.destroy();
		}
	}

	public static void killAll() {
		for (Process prc: runningProcs) {
			prc.destroy();
		}
		String commandLine = CommandLine.get(CLOSE);
		ExternalProcess.executeCommand(commandLine);
	}

	public static void remove (Process prc) {
		runningProcs.remove(prc);
	}

}