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

package it.slumdroid.wizard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
			try {
				f=new FileReader(file);
				BufferedReader b=new BufferedReader(f);
				String s = b.readLine();
				if (s.contentEquals("completed")) {

					b.close();
					StartWindow.postExec(true);
					JOptionPane.showMessageDialog(null, "Deploy Completed Successfully.\n Define your state initial and close the AVD", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("failed")) {

					b.close();
					StartWindow.postExec(false);
					JOptionPane.showMessageDialog(null, "Deploy Failed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("done")) {

					b.close();
					StartWindow.postExec(false);
					StartWindow.disableDeploy();
					JOptionPane.showMessageDialog(null, "Ripper Executed", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("artifactdone")) {

					b.close();
					StartWindow.DownSide(true);
					StartWindow.Upside(true);
					JOptionPane.showMessageDialog(null, "Report Generated", "Information", JOptionPane.INFORMATION_MESSAGE);

				} else if (s.contentEquals("firstboot")) {

					b.close();
					StartWindow.postFirstBoot();
					JOptionPane.showMessageDialog(null, "Wait the Android OS Booting,\n then click on \"Deploy\"", "Information", JOptionPane.INFORMATION_MESSAGE);

				}

				fileNotFound=false;
				b.close();
			} catch (FileNotFoundException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}   
		}
		return null;
	}
}