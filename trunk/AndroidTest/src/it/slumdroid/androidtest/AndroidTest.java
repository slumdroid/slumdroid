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

package it.slumdroid.androidtest;

import java.awt.EventQueue;
import javax.swing.*;

public class AndroidTest {

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String inputPath  = (args.length == 0) ? "" : args[0];
				Xml2DotFrame frame = new Xml2DotFrame(inputPath);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (frame.isWindowed()) {
					frame.setVisible(true);
				} else {
					frame.processFile(inputPath + "\\files\\guitree.xml");
				}
			}
		});
	}

}