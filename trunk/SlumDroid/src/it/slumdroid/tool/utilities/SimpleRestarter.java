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

package it.slumdroid.tool.utilities;

import it.slumdroid.tool.model.Restarter;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;

public class SimpleRestarter implements Restarter {

	private ContextWrapper main;

	@Override
	public void restart() {
		Intent i = this.main.getBaseContext().getPackageManager().getLaunchIntentForPackage(this.main.getBaseContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
		this.main.startActivity(i);
	}

	@Override
	public void setRestartPoint(Activity activity) {
		this.main = new ContextWrapper(activity);
	}

}