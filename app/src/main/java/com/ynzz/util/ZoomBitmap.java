package com.ynzz.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ZoomBitmap {

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {

		float width = bgimage.getWidth();
		float height = bgimage.getHeight();

		Matrix matrix = new Matrix();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
}
