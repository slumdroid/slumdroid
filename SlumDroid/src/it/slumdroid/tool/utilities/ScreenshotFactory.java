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

import it.slumdroid.tool.model.ImageCaptor;
import it.slumdroid.tool.model.ImageStorage;
import android.graphics.Bitmap;

public class ScreenshotFactory {

	private static ImageCaptor theImageCaptor = null;
	private static ImageStorage theImageStorage = null;

	public static ImageStorage getTheImageStorage() {
		return theImageStorage;
	}

	public static void setTheImageStorage(ImageStorage imageStorage) {
		theImageStorage = imageStorage;
	}

	public static ImageCaptor getImageCaptor() {
		return theImageCaptor;
	}

	public static void setImageCaptor(ImageCaptor theCaptor) {
		theImageCaptor = theCaptor;
	}

	public static boolean saveScreenshot(String id) {
		try {
			Bitmap bitmap = theImageCaptor.captureImage();
			if (bitmap == null) return false;
			theImageStorage.saveImage(bitmap, id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;	
	}

	public static String getFileExtension() {
		return theImageStorage.imageFormat();
	}

}