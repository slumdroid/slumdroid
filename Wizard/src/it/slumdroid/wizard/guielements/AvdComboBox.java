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

package it.slumdroid.wizard.guielements;

import static it.slumdroid.wizard.tools.CommandLine.LOAD_AVD;
import it.slumdroid.wizard.tools.CommandLine;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class AvdComboBox extends JComboBox<Object> {
	
	private static final long serialVersionUID = 1L;

	public void loadDevices() {
		String CommandList = CommandLine.get(LOAD_AVD);
		try {
			Process proc = Runtime.getRuntime().exec(CommandList);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s;
			int i = 0;
			while ((s = stdInput.readLine()) != null) {
				if (i == 0) removeAllItems();
				if(s.contains("Name: ")) {
					insertItemAt(s.substring(s.indexOf("Name: ") + 6).trim(), i);
					i++;
				}

			}
			if (getItemCount() > 0) {
				setSelectedIndex(0);
			}
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Error generating AVDs List");
			e1.printStackTrace();
		}
	}

	public JButton getLoadButton () {
		JButton btnLoadAvds = new JButton("Load AVDs");
		btnLoadAvds.setFont(new Font("Tahoma", Font.BOLD, 9));
		btnLoadAvds.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadDevices();
			}
		});
		return btnLoadAvds;
	}

	public String getDevice () {
		return (String) getSelectedItem();
	}

}
