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

package it.slumdroid.droidmodels.model;

import static it.slumdroid.droidmodels.model.InputType.TYPE_CLASS_DATETIME;
import static it.slumdroid.droidmodels.model.InputType.TYPE_CLASS_NUMBER;
import static it.slumdroid.droidmodels.model.InputType.TYPE_CLASS_PHONE;
import static it.slumdroid.droidmodels.model.InputType.TYPE_CLASS_TEXT;
import static it.slumdroid.droidmodels.model.InputType.TYPE_MASK_CLASS;
import static it.slumdroid.droidmodels.model.InputType.TYPE_MASK_FLAGS;
import static it.slumdroid.droidmodels.model.InputType.TYPE_MASK_VARIATION;
import static it.slumdroid.droidmodels.model.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static it.slumdroid.droidmodels.model.InputType.TYPE_NUMBER_FLAG_SIGNED;
import static it.slumdroid.droidmodels.model.InputType.TYPE_NUMBER_VARIATION_PASSWORD;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_FLAG_MULTI_LINE;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_URI;
import static it.slumdroid.droidmodels.model.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class WidgetType {

	private final int type;
	private String nameLowerCase;
	private String valueLowerCase;

	public WidgetType (int t, String name, String value) {
		this.type = new Integer(t);
		this.nameLowerCase = new String(name.toLowerCase());
		this.valueLowerCase =  new String(value.toLowerCase());
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
			} else if (this.nameLowerCase.contains("isbn")){
				output += ", ISBN";
			} else if (this.nameLowerCase.contains("credit") && this.nameLowerCase.contains("card")){
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
		if ((this.type & TYPE_MASK_CLASS) != TYPE_CLASS_TEXT) {
			return false;
		}
		return true;
	}

	private boolean isNumber () {
		if ((this.type & TYPE_MASK_CLASS) != TYPE_CLASS_NUMBER) {
			return false;
		}
		return true;
	}

	private boolean isDatetime () {
		if ((this.type & TYPE_MASK_CLASS) != TYPE_CLASS_DATETIME) {
			return false;
		}
		return true;
	}

	private boolean isPhone () {
		if ((this.type & TYPE_MASK_CLASS) != TYPE_CLASS_PHONE) {
			return false;
		}
		return true;
	}

	private boolean isTextMultiline () {
		if (!isText() 
				|| ((this.type & TYPE_MASK_FLAGS) != TYPE_TEXT_FLAG_MULTI_LINE)
				|| ((this.type & TYPE_MASK_FLAGS) != TYPE_TEXT_FLAG_IME_MULTI_LINE)) {
			return false;
		}
		return true;
	}

	private boolean isTextEmailAddress () {
		if (this.nameLowerCase.contains("e-mail")
				|| this.nameLowerCase.contains("mail")
				|| (this.valueLowerCase.contains("@") && this.valueLowerCase.contains(".")))
			return true;
		if (!isText() || ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_EMAIL_ADDRESS)) {
			return false;
		}
		return true;
	}

	private boolean isTextPostalAddress () {
		if (!isText() || ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_POSTAL_ADDRESS)) {
			return false;
		}
		return true;
	}

	private boolean isTextURI () {
		if (this.valueLowerCase.contains("http")
				|| this.valueLowerCase.contains("www")
				|| this.nameLowerCase.contains("site")
				|| this.nameLowerCase.contains("server")
				|| this.nameLowerCase.contains("url"))
			return true;
		if (!isText() || ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_URI)) {
			return false;
		}
		return true;
	}

	private boolean isPersonName () {
		if (!isText() || ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_PERSON_NAME)) {
			return false;
		}
		return true;
	}

	private boolean isPassword (){
		if (!isText() 
				|| ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_PASSWORD)
				|| ((this.type & TYPE_MASK_VARIATION) != TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ) {
			return false;
		}
		return true;
	}

	private boolean isNumberSigned () {
		if (!isNumber() || ((this.type & TYPE_MASK_FLAGS) != TYPE_NUMBER_FLAG_SIGNED)) {
			return false;
		}
		return true;
	}

	private boolean isNumberDecimal () {
		if (!isNumber() || ((this.type & TYPE_MASK_FLAGS) != TYPE_NUMBER_FLAG_DECIMAL)) {
			return false;
		}
		return true;
	}

	private boolean isNumberPassword () {
		if (!isNumber() || ((this.type & TYPE_MASK_VARIATION) != TYPE_NUMBER_VARIATION_PASSWORD)) {
			return false;
		}
		return true;
	}
}