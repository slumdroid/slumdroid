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

import it.slumdroid.wizard.Wizard;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class BackWorker extends SwingWorker<Integer, String> {
	private static String file;

	public void setFile(String file) {
		BackWorker.file = file;
	}

	@Override
	protected Integer doInBackground() {
		boolean fileNotFound=true;
		while(fileNotFound) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {	
				e.printStackTrace();
			}
			FileReader reader;
			BufferedReader buffer = null;
			try {
				reader = new FileReader(file);
				buffer = new BufferedReader(reader);
				String line = buffer.readLine();
				if (line.contentEquals("completed")) {

					buffer.close();
					Wizard.postExec(true);
					JOptionPane.showMessageDialog(null, "Deploy Completed Successfully", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (line.contentEquals("failed")) {

					buffer.close();
					Wizard.postExec(false);
					JOptionPane.showMessageDialog(null, "Deploy Failed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (line.contentEquals("done")) {

					buffer.close();
					Wizard.postExec(false);
					Wizard.disableDeploy();
					JOptionPane.showMessageDialog(null, "Ripping Process Executed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (line.contentEquals("artifactdone")) {

					buffer.close();
					Wizard.DownSide(true);
					Wizard.Upside(true);
					Wizard.postGenerate();
					JOptionPane.showMessageDialog(null, "Reports Generated", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (line.contentEquals("firstboot")) {

					buffer.close();
					Wizard.postFirstBoot();
					JOptionPane.showMessageDialog(null, "Click on \"Deploy\"", "Information", JOptionPane.INFORMATION_MESSAGE);

				}

				fileNotFound = false;
				buffer.close();
			} catch (Exception ignore) {
				// ignore.printStackTrace();
			}   finally {
				try {
					buffer.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		return null;
	}
}