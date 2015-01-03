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

import it.slumdroid.wizard.Wizard;
import it.slumdroid.wizard.tools.CommandLine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class ResultsPathTextField extends PathTextField {
	
	private static final long serialVersionUID = 1L;

	public ResultsPathTextField () {
		super();
	}

	@Override
	public void onUpdate (String path) {
		CommandLine.setResultsPath(path);
		if (Wizard.checkExp() != 0) {
			Wizard.enableOpenResultFolder();
			if (Wizard.checkPkg() != 0) {
				Wizard.enableStart();
				Wizard.enableReporting();
			}
		}
	}

	public JButton getChangeButton() {
		JButton button = new JButton("Select the Results Folder");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle("Select the Results Folder");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					setPath(fileChooser.getSelectedFile().toString());
				} 
			}
		});
		return button;
	}

}