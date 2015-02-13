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

package it.slumdroid.utilities.module.guianalyzer;

import nl.flotsam.xeger.Xeger;

// TODO: Auto-generated Javadoc
/**
 * Perturbations on Text Input
 * Perturbations rules:
 * MO0: Starter input / Valid input
 * MO1: Remove the mandatory sets from a regular expression
 * MO2: Disorder the sequence of sets in a regular expression
 * MO3: Insert invalid and dangerous characters, such as an empty string,
 *      strings with starting period, and extremely long strings, into a regular
 *      expression.
 */

public class Perturbations {

	/** The value lower case. */
	private String valueLowerCase;

	/** The type. */
	private String type;

	/**
	 * Instantiates a new perturbations.
	 *
	 * @param colValue the col value
	 * @param colType the col type
	 */
	public Perturbations (Object colValue, Object colType) {
		this.valueLowerCase = colType.toString().toLowerCase();
		this.type = colValue.toString();
	}

	/**
	 * Perturbe.
	 *
	 * @param type the type
	 * @return the string
	 */
	public String perturbe(String type) {
		String pertubedInputs =  new String();
		if (type.equals("") || type.equals("Exclude")) {
			// do Nothing
		} else {
			pertubedInputs = pertubedInputs.concat(valueLowerCase); // MO0 - Starter/Valid Inputs 
			if (!valueLowerCase.equals("")) {
				pertubedInputs = pertubedInputs.concat(","); // M03 - Empty Input for all Types
			}

			if (type.equals("Generic")) {
				return pertubedInputs.concat(generic());
			}
			if (type.equals("Number")) {
				return pertubedInputs.concat(number());
			}
			if (type.equals("Url")) {
				return pertubedInputs.concat(url());
			}
			if (type.equals("EMail")) {
				return pertubedInputs.concat(email());
			}
			if (type.equals("Zip Code")) {
				return pertubedInputs.concat(zip());
			}
			if (type.equals("ISBN")) {
				return pertubedInputs.concat(isbn());
			}
			if (type.equals("Credit Card")) {
				return pertubedInputs.concat(creditcard());			
			}
		}
		return pertubedInputs;
	}

	// Generic Strings
	/**
	 * Generic.
	 *
	 * @return the string
	 */
	private String generic () {
		String pertubedInputs =  new String();
		// MO3 - Dangerous Inputs
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat(",SlumDroid's Test"); // Single Quote
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z0-9]{20}")); // Long Random String
		return pertubedInputs;
	}

	// Decimal+Signed: (\\-|+)[0-9]+\\.[0-9]+
	// Decimal: [0-9]+\\.[0-9]+
	// Signed: (\\-|+)[0-9]+
	// Number: [0-9]+
	/**
	 * Number.
	 *
	 * @return the string
	 */
	private String number () {
		String pertubedInputs =  new String();
		pertubedInputs = pertubedInputs.concat(",0"); // MO3 - Dangerous Input == 0 
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{20,}")); // MO3 - Dangerous Inputs
		if (type.contains("Decimal")) {
			pertubedInputs = pertubedInputs.concat(",."); // MO1 - Remove the Mandatory Sets
			pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{20,}\\.[0-9]{20,}")); // MO3 - Dangerous Inputs
		}
		if (type.contains("Signed")) {
			pertubedInputs = pertubedInputs.concat(",-"); // MO1 - Remove the Mandatory Sets
			pertubedInputs = pertubedInputs.concat("," + createRegEx("(\\-|+)[0-9]{30,}")); // MO3 - Dangerous Inputs
		}
		return pertubedInputs;
	}

	// URL: https?://[\\-a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(:[0-9]+){1}(/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)+\\?[0-9A-Za-z]+=[0-9A-Za-z+&\\@\\#/%=~_\\(\\)|]+
	/**
	 * Url.
	 *
	 * @return the string
	 */
	private String url () {
		String pertubedInputs =  new String();
		// MO1 - Remove the Mandatory Sets
		String withoutHTTP = valueLowerCase.replace("http", "");
		pertubedInputs = pertubedInputs.concat("," + withoutHTTP);
		// MO3 - Dangerous Inputs
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "%");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "[");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "]");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + "{");
		pertubedInputs = pertubedInputs.concat("," + valueLowerCase + " ");
		return pertubedInputs;
	}

	// Email: [0-9A-Za-z-\\.]+\\@([0-9A-Za-z-]+\\.)+[A-Za-z-]{2,4}
	/**
	 * Email.
	 *
	 * @return the string
	 */
	private String email () {
		String pertubedInputs =  new String();	
		// MO1 - Remove the Mandatory Sets
		String withoutET = valueLowerCase.replace("@", "");
		pertubedInputs = pertubedInputs.concat("," + withoutET);
		// MO3 - Dangerous Inputs
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat("," + "$" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + "=" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + "&" + valueLowerCase);
		pertubedInputs = pertubedInputs.concat("," + " " + valueLowerCase);	
		return pertubedInputs;
	}

	// ZIP: [0-9]{5}([-]{1}[0-9]{4})?
	/**
	 * Zip.
	 *
	 * @return the string
	 */
	private String zip () {
		String pertubedInputs =  new String();	
		// MO0 - Valid Input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{5}([-]{1}[0-9]{4})?"));
		// MO2 - Disorder Sets 
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{5}([0-9]{4})?"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[-]{1}[0-9]{5}([0-9]{4})?}"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]{6}([-]{2}[0-9]{5})+"));
		// MO3 - Dangerous Input
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z]{5}[-]{1}[A-Za-z]{4}"));
		return pertubedInputs;
	}

	// ISBN: [0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]
	/**
	 * Isbn.
	 *
	 * @return the string
	 */
	private String isbn () {
		String pertubedInputs =  new String();
		// MO0 - Valid input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*[xX0-9]"));
		// MO1 - Remove the Mandatory Sets
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
		// MO2 - Disorder Sets 
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[xX0-9][0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]*[- ]*"));
		// MO3 - Dangerous Inputs
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[0-9]+[- ][0-9]+[- ][0-9]+[- ][0-9]+[- ]+[xX0-9]+"));
		pertubedInputs = pertubedInputs.concat("," + createRegEx("[A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]+[\\.\\@&~][A-Za-z]*[\\.\\@&~]*[xX0-9]"));
		return pertubedInputs;	
	}

	// Credit card: ((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}
	/**
	 * Creditcard.
	 *
	 * @return the string
	 */
	private String creditcard () {
		String pertubedInputs =  new String();	
		// MO0 - Valid Input
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[0-9]{3})|(5[1-5][0-9]{2})|(6011)|(34[0-9]{1})|(37[0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
		// MO1 - Remove the Mandatory Sets
		pertubedInputs = pertubedInputs.concat("," + createRegEx("(([0-9]{3})|([1-5][0-9]{2})|([0-9]{1})|([0-9]{1}))-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}|3[4,7][0-9-]{15}"));
		// MO3 - Dangerous Inputs
		pertubedInputs = pertubedInputs.concat(", "); // Only "SpaceBar" Character
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[0-9]{4})|(5[1-5][0-9]{3})|(6011)|(34[0-9]{2})|(37[0-9]{3}))-?[0-9]{6}-?[0-9]{7}-?[0-9]{6}|3[4,7][0-9-]{20}"));		
		pertubedInputs = pertubedInputs.concat("," + createRegEx("((4[A-Za-z]{3})|(5[1-5][A-Za-z]{2})|(6011)|(34[A-Za-z]{1})|(37[A-Za-z]{1}))-?[A-Za-z]{4}-?[A-Za-z]{4}-?[A-Za-z]{4}|3[4,7][A-Za-z ]{15}"));
		return pertubedInputs;		
	}

	/**
	 * Creates the reg ex.
	 *
	 * @param regex the regex
	 * @return the string
	 */
	private String createRegEx (String regex) {
		return new Xeger(regex).generate();
	}

}