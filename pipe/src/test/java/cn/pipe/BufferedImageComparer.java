
package cn.pipe;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public final class BufferedImageComparer {

	public static boolean isSame(BufferedImage img0, BufferedImage img1) {
		int width0 = img0.getWidth();
		int height0 = img0.getHeight();
		int width1 = img1.getWidth();
		int height1 = img1.getHeight();

		if (width0 != width1 || height0 != height1) {
			throw new AssertionError("Width and/or height do not match.");
		}
		
		if (!img0.getColorModel().equals(img1.getColorModel())) {
			String message = "Color models do not match. [%s, %s]";
			message = String.format(message, img0.getColorModel(), img1.getColorModel());
			throw new AssertionError(message);
		}
		
		if (!img0.getSampleModel().equals(img1.getSampleModel())) {
			String message = "Sample models do not match. [%s, %s]";
			message = String.format(message, img0.getSampleModel(), img1.getSampleModel());
			throw new AssertionError(message);
		}
		
		if (img0.getType() != img1.getType()) {
			String message = "Image types do not match. [%d, %d]";
			message = String.format(message, img0.getType(), img1.getType());
			throw new AssertionError(message);
		}
		
		/*
		 * Check the RGB data.
		 */
		for (int i = 0; i < width0; i++) {
			for (int j = 0; j < height0; j++) {
				if (img0.getRGB(i, j) != img1.getRGB(i, j)) {
					String message = "Pixels do not match. location: (%d, %d), rgb: (%d, %d)";
					message = String.format(message, i, j, img0.getRGB(i, j), img1.getRGB(i, j));
					throw new AssertionError(message);
				}
			}
		}
		
		/*
		 * Check the raw data.
		 */
		Raster d0 = img0.getData();
		Raster d1 = img1.getData();
		
		if (d0.getNumBands() != d1.getNumBands()) {
			String message = "Number of bands do not match. [%d, %d]";
			message = String.format(message, d0.getNumBands(), d1.getNumBands());
			throw new AssertionError(message);
		}
		
		for (int i = 0; i < width0; i++) {
			for (int j = 0; j < height0; j++) {
				int[] i0 = new int[d0.getNumBands()];
				int[] i1 = new int[d1.getNumBands()];
				
				d0.getPixel(i, j, i0);
				d1.getPixel(i, j, i1);
				
				for (int k = 0; k < d0.getNumBands(); k++) {
					if (i0[k] != i1[k]) {
						String message = "Pixels do not match. rgb: (%d, %d), band: %d";
						message = String.format(message, i, j, k);
						throw new AssertionError(message);
					}
				}
			}
		}
		
		return true;
	}

}
