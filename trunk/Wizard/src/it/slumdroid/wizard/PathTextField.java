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

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public abstract class PathTextField extends JTextField {

	public PathTextField() {
		super();
		this.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAndroidPath (arg0);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAndroidPath (arg0);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateAndroidPath (arg0);
			}

			public void updateAndroidPath (DocumentEvent arg0) {
				if (arg0.getLength()==0) return;
				try {
					onUpdate(arg0.getDocument().getText(0, arg0.getDocument().getLength()));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public abstract void onUpdate(String path);
	public abstract JButton getChangeButton();

	public String getPath() {
		return getText();
	}

	public void setPath(String p) {
		setText(p);
	}

	private static final long serialVersionUID = 1L;

}