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

package it.slumdroid.guianalyzer.perturbation;

import it.slumdroid.droidmodels.model.WidgetState;

import it.slumdroid.guianalyzer.model.WidgetPerturbations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Perturbations on editText input
 * 
 * MO0: Starter Input and Special Values Input
 * 
 * Some valid rules:
 * 
 * MO1: remove the mandatory sets from a regular expression
 * MO2: disorder the sequence of sets in a regular expression
 * MO3: change the repetition time of selecting elements from a set
 * MO4: select elements from complementary sets of sets in a regular expression,
 *      especially characters next to the boundary of the input domain
 * MO5: insert invalid and dangerous characters, such as an empty string,
 *      strings with starting period, and extremely long strings, into a regular
 *      expression
 * 
 */
public class Perturbations implements WidgetPerturbations {

	private WidgetState widget;
	private List<WidgetState> WidgetsList = new ArrayList<WidgetState>();
	private List<String> Perturbations = new ArrayList<String>();
	private String valueLowerCase;
	private String type;
	private boolean bypass = false;

	/**
	 * Constructor
	 * 
	 * @param   widget  the widget to perturbate
	 * @param   bypass  if true you can use any perturbation on any widget
	 *                  bypassing the type control
	 */
	public Perturbations (WidgetState widget, boolean bypass) {

		this.widget = widget;
		this.bypass = bypass;
		valueLowerCase = this.widget.getValue().toLowerCase();
		type = this.widget.getTextType();

	}

	/**
	 * Number perturbation
	 *
	 * Decimal: [0-9]+\\.[0-9]+
	 * Signed: (\\-|+)[0-9]+
	 * Decimal+Signed: (\\-|+)[0-9]+\\.[0-9]+
	 * Number: [0-9]+
	 *
	 * @return boolean
	 */
	public boolean number () {

		WidgetState tmp;

		if (type.contains("Number")) {

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			if (type.contains("Decimal")) {

				// MO1
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("[0-9]+"));
				WidgetsList.add(tmp);

				// MO2
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("\\.[0-9]+"));
				WidgetsList.add(tmp);

				// "long" number
				// MO5
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("[0-9]{20,}\\.[0-9]{20,}"));
				WidgetsList.add(tmp);

			} else if (type.contains("Signed")) {

				// MO1
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("[0-9]+"));
				WidgetsList.add(tmp);

				// MO5
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("(\\-|+)[0-9]{30,}"));
				WidgetsList.add(tmp);

			} else if (type.contains("Decimal") && type.contains("Signed")) {

				// MO1
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("[0-9]+"));
				WidgetsList.add(tmp);

				// MO5
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("(\\-|+)[0-9]{20,}\\.[0-9]{20,}"));
				WidgetsList.add(tmp);

			} else {

				// MO5
				tmp = widget.clone();
				tmp.setValue(RegEx.getString("[0-9]{20,}"));
				WidgetsList.add(tmp);

			}

			return true;

		} else if (bypass) {

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			// MO4
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[A-Za-z+&\\@\\#/%=~_\\(\\)|]+"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\\r\\\n";
			}
			tmp.setValue(RegEx.getString(" [0-9]+[+\\-,:;\\.+&\\@\\#/%=~_\\(\\)|]+[0-9]+( \\\t" + newline + ")+"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	/**
	 * URI perturbation: specified in URL only
	 * 
	 * URL: https?://[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+
	 * 
	 * @return boolean
	 */
	public boolean url () {

		if (bypass || type.contains("URI")) {
			WidgetState tmp;

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			//MO0 - Special Input
			tmp = widget.clone();
			tmp.setValue("SlumDroid's Test");
			WidgetsList.add(tmp);

			//MO0 - Special Input
			// "long" word
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[A-Za-z0-9]{20,}"));
			WidgetsList.add(tmp);

			// MO1
			tmp = widget.clone();
			tmp.setValue("google.it");
			//tmp.setValue(RegEx.getString("[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+"));
			WidgetsList.add(tmp);

			// MO2
			tmp = widget.clone();
			tmp.setValue("\\http://www.google.it");
			//tmp.setValue(RegEx.getAutomatonString(" \\.[\\-a-zA-Z0-9]+(https://)+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+"));
			WidgetsList.add(tmp);

			// MO3
			tmp = widget.clone();
			tmp.setValue("http://http://www.google.it");
			//tmp.setValue(RegEx.getString("https?://https?://[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+)+(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+"));
			WidgetsList.add(tmp);

			// MO4-1 Special Character1
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("https?://[+&\\@\\#/%=~_^\\(\\)|]"));
			//tmp.setValue(RegEx.getString("https?://[+&\\@\\#/%=~_\\(\\)|]+(\\.[a-zA-Z0-9]+)+(:[A-Za-z]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+"));
			WidgetsList.add(tmp);

			// MO4-2 Special Character2 
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("https?://[{]"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\\r\\\n";
			}
			tmp.setValue("http:\\\\www.google.it"+ newline + "test");
			//tmp.setValue(RegEx.getString(" https?:(\\\\\\\\)[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|^]+( \\\t" + newline + ")+"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	/**
	 * Email perturbation
	 * 
	 * Email: [0-9A-Za-z-\\.]+\\@([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}
	 * 
	 * @return boolean
	 */
	public boolean email () {

		if (bypass || type.contains("EmailAddress")) {
			WidgetState tmp;

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Valid Input
			tmp = widget.clone();
			tmp.setValue("android.ripper@google.it");
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			//MO0 - Special Input
			tmp = widget.clone();
			tmp.setValue("SlumDroid's Test");
			WidgetsList.add(tmp);

			//MO0 - Special Input
			// "long" word
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[A-Za-z0-9]{20,}"));
			WidgetsList.add(tmp);

			// MO1
			tmp = widget.clone();
			tmp.setValue("google.it");
			//tmp.setValue(RegEx.getString("[0-9A-Za-z-\\.]+([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}"));
			WidgetsList.add(tmp);

			// MO2
			tmp = widget.clone();
			tmp.setValue("@google.it");
			//tmp.setValue(RegEx.getString("\\@[0-9A-Za-z-\\.]+([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}"));
			WidgetsList.add(tmp);

			// MO3
			tmp = widget.clone();
			tmp.setValue("@@google.it");
			//tmp.setValue(RegEx.getString("[0-9A-Za-z-\\.]+\\@\\@([0-9A-Za-z-]+\\.)+[A-Za-z-]+"));
			WidgetsList.add(tmp);

			// MO4
			tmp = widget.clone();
			tmp.setValue("slumdroid@@google.it");
			//tmp.setValue(RegEx.getString("[0-9]+\\@[+&\\@\\#/%=~_\\(\\)|]+[A-Za-z-]{2,4}"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\r\\n";
			}
			tmp.setValue("slumdroid@google.it" + newline + "test");
			//tmp.setValue(RegEx.getString(" (\\.[0-9A-Za-z-\\.]+\\@([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}( \\\t" + newline + ")+){10,}"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	/**
	 * ZIP perturbation
	 * 
	 * ZIP: [0-9]{5}([-]{1}[0-9]{4})?
	 * 
	 * @return boolean
	 */
	public boolean zip () {

		if (bypass || type.contains("PostalAddress")) {
			WidgetState tmp;

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Valid Input
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]{5}([-]{1}[0-9]{4})?"));
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			// MO1
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]{5}([0-9]{4})?"));
			WidgetsList.add(tmp);

			// MO2
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[-]{1}[0-9]{5}([0-9]{4})?}"));
			WidgetsList.add(tmp);

			// MO3
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]{6}([-]{2}[0-9]{5})+"));
			WidgetsList.add(tmp);

			// MO4
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[A-Za-z]{5}[-]{1}[A-Za-z]{4}"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\\r\\\n";
			}
			tmp.setValue(RegEx.getString(" \\.[+&\\@\\#/%=~_\\(\\)|0-9]{5}([-]{1}[0-9]{4})+( \\\t" + newline + ")+"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	/**
	 * ISBN perturbation
	 * 
	 * ISBN: [0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]
	 * 
	 * @return boolean
	 */
	public boolean isbn () {

		if (bypass || type.contains("ISBN")) {
			WidgetState tmp;

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Valid Input
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]"));
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			// MO1
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
			WidgetsList.add(tmp);

			// MO2
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[xX0-9][0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
			WidgetsList.add(tmp);

			// MO3
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]+[- ]+[xX0-9]+"));
			WidgetsList.add(tmp);

			// MO4
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("[A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]*[\\.\\@&~]*[xX0-9]"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\\r\\\n";
			}
			tmp.setValue(RegEx.getString(" \\.[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]( \\\t" + newline + ")+"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	/**
	 * Credit card perturbation
	 * 
	 * Credit card: ((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}
	 * 
	 * @return boolean
	 */
	public boolean creditcard () {

		if (bypass || type.contains("CreditCard")) {
			WidgetState tmp;

			// MO0 - Starter Input
			tmp = widget.clone();
			tmp.setValue(valueLowerCase);
			WidgetsList.add(tmp);

			// MO0 - Valid Input
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
			WidgetsList.add(tmp);

			// MO0 - Empty Input
			if (!valueLowerCase.equals("")){
				tmp = widget.clone();
				tmp.setValue(new String());
				WidgetsList.add(tmp);
			}

			// MO1
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("(([0-9]{3})|([1-5][0-9]{2})|([0-9]{1})|([0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
			WidgetsList.add(tmp);

			// MO3
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("((4[0-9]{4})|(5[1-5][0-9]{3})|(6011)|(34[0-9]{2})|(37[0-9]{3}))-?[0-9]{6}-?[0-9]{7}-?[0-9]{6}|3[4,7][0-9-]{20}"));
			WidgetsList.add(tmp);

			// MO4
			tmp = widget.clone();
			tmp.setValue(RegEx.getString("((4[A-Za-z]{3})|(5[1-5][A-Za-z]{2})|(6011)|(34[A-Za-z]{1})|(37[A-Za-z]{1}))-?[A-Za-z]{4}-?[A-Za-z]{4}-?[A-Za-z]{4}|3[4,7][A-Za-z ]{15}"));
			WidgetsList.add(tmp);

			// MO5
			tmp = widget.clone();
			String newline = "";
			if (type.contains("Multiline")) {
				newline = "\\\r\\\n";
			}
			tmp.setValue(RegEx.getString(" \\. ((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}( \\\t" + newline + ")+"));
			WidgetsList.add(tmp);

			return true;
		}

		return false;

	}

	@Override
	public void exclude () {
		//Do Nothing
	}

	/**
	 * Dictionary from file
	 * Input values are extracted from a file
	 * 
	 * @return boolean 
	 */
	@Override
	public void dictionary (String inputFileName) {

		try {
			WidgetState tmp;

			FileInputStream fstream = new FileInputStream(inputFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = br.readLine()) != null) {
				tmp = widget.clone();
				tmp.setValue(line);
				WidgetsList.add(tmp);
			}

			in.close();
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	@Override
	public void manual (String perturbation) {

		WidgetState tmp;

		tmp = widget.clone();
		tmp.setValue(perturbation);
		WidgetsList.add(tmp);

	}

	/**
	 * DEFAULT pertubation
	 * If there has been no pertubations, input value should be set to a default
	 * value (it can be set to the already existing one)
	 */
	@Override
	public void standard () {

		WidgetState tmp;

		// MO0 - Starter Input
		tmp = widget.clone();
		tmp.setValue(valueLowerCase);
		WidgetsList.add(tmp);

		// MO0 - Empty Input
		if (!valueLowerCase.equals("")){
			tmp = widget.clone();
			tmp.setValue(new String());
			WidgetsList.add(tmp);
		}

		//MO0 - Special Input
		tmp = widget.clone();
		tmp.setValue("SlumDroid's Test");
		WidgetsList.add(tmp);

		//MO0 - Special Input == Only 1 character
		tmp = widget.clone();
		tmp.setValue(RegEx.getString("[A-Za-z0-9]{1}"));
		WidgetsList.add(tmp);

		// "long" word
		// MO5
		tmp = widget.clone();
		tmp.setValue(RegEx.getString("[A-Za-z0-9]{20}"));
		WidgetsList.add(tmp);

		//tmp = widget.clone();
		//tmp.setValue(RegEx.getString("VALUE"));
		//WidgetsList.add(tmp);

	}

	@Override
	public void manualRegEx (String regEx) {

		WidgetState tmp;

		tmp = widget.clone();
		tmp.setValue(RegEx.getString(regEx));
		WidgetsList.add(tmp);

	}

	@Override
	public List<String> getPerturbations () {
		return Perturbations;
	}

	@Override
	public void addPerturbation (String perturbation) {
		Perturbations.add(perturbation);
	}

	@Override
	public List<WidgetState> getList () {
		return WidgetsList;
	}
}