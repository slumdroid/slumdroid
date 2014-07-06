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

package it.slumdroid.droidmodels.model;

import static it.slumdroid.droidmodels.model.InputType.*;

public class WidgetType {

	private final int type;
	private String nameLowerCase;
	private String valueLowerCase;

	public WidgetType (int t, String name, String value) {
		type = new Integer(t);
		nameLowerCase = new String(name.toLowerCase());
		valueLowerCase =  new String(value.toLowerCase());
	}

	public String convert () {
		String output = "";

		if (isText()) {
			output = "Text";
			if (isTextMultiline()) output += ", Multiline";

			if (isTextEmailAddress()) {
				output += ", EmailAddress";
			} else if (isTextPostalAddress()) {
				output += ", PostalAddress";
			} else if (isTextURI()) {
				output += ", URI";
			} else if (isPassword()){
				output += ", Password";
			} else if (isPersonName()){
				output += ", PersonName";
			} else if (nameLowerCase.contains("isbn")){
				output += ", ISBN";
			} else if (nameLowerCase.contains("credit") && nameLowerCase.contains("card")){
				output += ", CreditCard";
			}

		} else if (isNumber()) {
			output = "Number";

			if (isNumberSigned()) {
				output += ", Signed";
			}
			if (isNumberDecimal()) {
				output += ", Decimal";
			} 
			if (isNumberPassword()){
				output += ", NumberPassword";
			}

		} else if (isDatetime()) {
			output = "DateTime";
		} else if (isPhone()) {
			output = "Phone";
		}

		return output;
	}

	private boolean isText () {
		if ((type & TYPE_MASK_CLASS) != TYPE_CLASS_TEXT) {
			return false;
		}
		return true;
	}

	private boolean isNumber () {
		if ((type & TYPE_MASK_CLASS) != TYPE_CLASS_NUMBER) {
			return false;
		}
		return true;
	}

	private boolean isDatetime () {
		if ((type & TYPE_MASK_CLASS) != TYPE_CLASS_DATETIME) {
			return false;
		}
		return true;
	}

	private boolean isPhone () {
		if ((type & TYPE_MASK_CLASS) != TYPE_CLASS_PHONE) {
			return false;
		}
		return true;
	}

	private boolean isTextMultiline () {
		if (!isText() 
				|| ((type & TYPE_MASK_FLAGS) != TYPE_TEXT_FLAG_MULTI_LINE)
				|| ((type & TYPE_MASK_FLAGS) != TYPE_TEXT_FLAG_IME_MULTI_LINE)) {
			return false;
		}
		return true;
	}

	private boolean isTextEmailAddress () {
		if (nameLowerCase.contains("e-mail")
				|| nameLowerCase.contains("mail")
				|| (valueLowerCase.contains("@") && valueLowerCase.contains(".")))
			return true;
		if (!isText() || ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_EMAIL_ADDRESS)) {
			return false;
		}
		return true;
	}

	private boolean isTextPostalAddress () {
		if (!isText() || ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_POSTAL_ADDRESS)) {
			return false;
		}
		return true;
	}

	private boolean isTextURI () {
		if (valueLowerCase.contains("http")
				|| valueLowerCase.contains("www")
				|| nameLowerCase.contains("site")
				|| nameLowerCase.contains("server")
				|| nameLowerCase.contains("url"))
			return true;
		if (!isText() || ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_URI)) {
			return false;
		}
		return true;
	}

	private boolean isPersonName () {
		if (!isText() || ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_PERSON_NAME)) {
			return false;
		}
		return true;
	}

	private boolean isPassword (){
		if (!isText() 
				|| ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_PASSWORD)
				|| ((type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ) {
			return false;
		}
		return true;
	}

	private boolean isNumberSigned () {
		if (!isNumber() || ((type & TYPE_MASK_FLAGS) != TYPE_NUMBER_FLAG_SIGNED)) {
			return false;
		}
		return true;
	}

	private boolean isNumberDecimal () {
		if (!isNumber() || ((type & TYPE_MASK_FLAGS) != TYPE_NUMBER_FLAG_DECIMAL)) {
			return false;
		}
		return true;
	}

	private boolean isNumberPassword () {
		if (!isNumber() || ((type & TYPE_MASK_VARIATION) != TYPE_NUMBER_VARIATION_PASSWORD)) {
			return false;
		}
		return true;
	}
}