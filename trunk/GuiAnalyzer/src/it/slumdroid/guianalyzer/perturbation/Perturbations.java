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

import nl.flotsam.xeger.Xeger;

/**
 * Perturbations on Text Input
 * 
 * Some valid rules:
 *
 * MO0: Starter input / Valid input
 * MO1: Remove the mandatory sets from a regular expression
 * MO2: Disorder the sequence of sets in a regular expression
 * MO3: Insert invalid and dangerous characters, such as an empty string,
 *      strings with starting period, and extremely long strings, into a regular
 *      expression
 * 
 */

public class Perturbations {

	private String valueLowerCase;
	private String type;

	public Perturbations (Object colValue, Object colType) {

		this.valueLowerCase = colType.toString().toLowerCase();
		this.type = colValue.toString();

	}

	// Decimal+Signed: (\\-|+)[0-9]+\\.[0-9]+
	// Decimal: [0-9]+\\.[0-9]+
	// Signed: (\\-|+)[0-9]+
	// Number: [0-9]+
	public String number () {
		String pertubedInputs =  new String();
		if (type.contains("Decimal") && type.contains("Signed")) {
			// MO3 - Special Input
			pertubedInputs = pertubedInputs.concat(createRegEx("(\\-|+)[0-9]{20,}\\.[0-9]{20,}"));
		} else if (type.contains("Decimal")) {
			// MO3 - Special Input
			pertubedInputs = pertubedInputs.concat(createRegEx("[0-9]{20,}\\.[0-9]{20,}"));
		} else if (type.contains("Signed")) {
			// MO3 - Special Input
			pertubedInputs = pertubedInputs.concat(createRegEx("(\\-|+)[0-9]{30,}"));
		} else {
			// MO3 - Special Input
			pertubedInputs = pertubedInputs.concat(createRegEx("[0-9]{20,}"));
		}
		return pertubedInputs;
	}

	// URL: https?://[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+
	public String url () {
		String pertubedInputs =  new String();
		// MO1 - Remove the Mandatory Sets
		String withoutHTTP = valueLowerCase.replace("http", "");
		pertubedInputs = pertubedInputs.concat("," + withoutHTTP);
		// MO3 - Special Inputs
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "%");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "[");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "]");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "{");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + " ");
		return pertubedInputs;
	}

	// Email: [0-9A-Za-z-\\.]+\\@([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}
	public String email () {
		String pertubedInputs =  new String();	
		// MO1 - Remove the Mandatory Sets
		String withoutET = valueLowerCase.replace("@", "");
		pertubedInputs = pertubedInputs.concat("," + withoutET);
		// MO3 - Special Inputs
		pertubedInputs = pertubedInputs.concat("," + "$" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + "=" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + "&" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + " " + valueLowerCase);	
		return pertubedInputs;
	}

	// ZIP: [0-9]{5}([-]{1}[0-9]{4})?
	public String zip () {

		String pertubedInputs =  new String();	
		// MO0 - Valid Input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{5}([-]{1}[0-9]{4})?"));
		// MO2 - Disorder Sets 
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{5}([0-9]{4})?"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[-]{1}[0-9]{5}([0-9]{4})?}"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{6}([-]{2}[0-9]{5})+"));
		// MO3 - Special Input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z]{5}[-]{1}[A-Za-z]{4}"));
		return pertubedInputs;
	}

	// ISBN: [0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]
	public String isbn () {

		String pertubedInputs =  new String();
		// MO0 - Valid input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]"));
		// MO1 - Remove the Mandatory Sets
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
		// MO2 - Disorder Sets 
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[xX0-9][0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
		// MO3 - Special Inputs
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]+[- ]+[xX0-9]+"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]*[\\.\\@&~]*[xX0-9]"));
		return pertubedInputs;	

	}

	// Credit card: ((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}
	public String creditcard () {

		String pertubedInputs =  new String();	
		// MO0 - Valid Input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
		// MO1 - Remove the Mandatory Sets
		pertubedInputs = pertubedInputs.concat("," + createRegEx("(([0-9]{3})|([1-5][0-9]{2})|([0-9]{1})|([0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
		// MO3 - Special Inputs
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[0-9]{4})|(5[1-5][0-9]{3})|(6011)|(34[0-9]{2})|(37[0-9]{3}))-?[0-9]{6}-?[0-9]{7}-?[0-9]{6}|3[4,7][0-9-]{20}"));		
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[A-Za-z]{3})|(5[1-5][A-Za-z]{2})|(6011)|(34[A-Za-z]{1})|(37[A-Za-z]{1}))-?[A-Za-z]{4}-?[A-Za-z]{4}-?[A-Za-z]{4}|3[4,7][A-Za-z ]{15}"));
		return pertubedInputs;
		
	}

	public String standard () {

		String pertubedInputs =  new String();
		// MO3 - Special Inputs
		pertubedInputs = pertubedInputs.concat("," + "SlumDroid's Test");
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z0-9]{20}"));
		return pertubedInputs;
		
	}
	
	public String createRegEx (String regex) {
		return new Xeger(regex).generate();
	}

	public String perturb(String type) {
		
		String pertubedInputs =  new String();
		// MO0 - Starter input 
		// M03 - Empty Input
		if (!valueLowerCase.equals("")) pertubedInputs = pertubedInputs.concat(valueLowerCase).concat(",,");
		else pertubedInputs = pertubedInputs.concat(valueLowerCase);
		
		if (type.equals("Number")) return pertubedInputs.concat(number());
		else if (type.equals("Generic")) return pertubedInputs.concat(standard());
		else if (type.equals("Url")) return pertubedInputs.concat(url());
		else if (type.equals("EMail")) return pertubedInputs.concat(email());
		else if (type.equals("Zip Code")) return pertubedInputs.concat(zip());
		else if (type.equals("ISBN")) return pertubedInputs.concat(isbn());
		else if (type.equals("Credit Card")) return pertubedInputs.concat(creditcard());
		else return new String();
	
	}
		
}