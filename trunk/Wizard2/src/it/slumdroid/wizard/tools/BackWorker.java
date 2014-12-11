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
			FileReader f;
			BufferedReader b = null;
			try {
				f = new FileReader(file);
				b = new BufferedReader(f);
				String s = b.readLine();
				if (s.contentEquals("completed")) {

					b.close();
					Wizard.postExec(true);
					JOptionPane.showMessageDialog(null, "Deploy Completed Successfully", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("failed")) {

					b.close();
					Wizard.postExec(false);
					JOptionPane.showMessageDialog(null, "Deploy Failed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("done")) {

					b.close();
					Wizard.postExec(false);
					Wizard.disableDeploy();
					JOptionPane.showMessageDialog(null, "Ripping Process Executed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("artifactdone")) {

					b.close();
					Wizard.DownSide(true);
					Wizard.Upside(true);
					Wizard.postGenerate();
					JOptionPane.showMessageDialog(null, "Reports Generated", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("firstboot")) {

					b.close();
					Wizard.postFirstBoot();
					JOptionPane.showMessageDialog(null, "Click on \"Deploy\"", "Information", JOptionPane.INFORMATION_MESSAGE);

				}

				fileNotFound = false;
				b.close();
			} catch (Exception ignore) {
				// ignore.printStackTrace();
			}   finally {
				try {
					b.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		return null;
	}
}