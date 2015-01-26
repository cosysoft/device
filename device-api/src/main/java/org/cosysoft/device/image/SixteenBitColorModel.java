package org.cosysoft.device.image;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import com.android.ddmlib.RawImage;

class SixteenBitColorModel extends ColorModel {
	private static final int[] BITS = { 8, 8, 8, 8 };

	public SixteenBitColorModel(RawImage rawImage) {
		super(32, BITS, ColorSpace.getInstance(1000), true, false, 3, 0);
	}

	public boolean isCompatibleRaster(Raster raster) {
		return true;
	}

	private int getPixel(Object inData) {
		byte[] data = (byte[]) inData;
		int value = data[0] & 0xFF;
		value |= data[1] << 8 & 0xFF00;

		return value;
	}

	public int getAlpha(Object inData) {
		return 255;
	}

	public int getBlue(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >> 0 & 0x1F) << 3;
	}

	public int getGreen(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >> 5 & 0x3F) << 2;
	}

	public int getRed(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >> 11 & 0x1F) << 3;
	}

	public int getAlpha(int pixel) {
		throw new UnsupportedOperationException();
	}

	public int getBlue(int pixel) {
		throw new UnsupportedOperationException();
	}

	public int getGreen(int pixel) {
		throw new UnsupportedOperationException();
	}

	public int getRed(int pixel) {
		throw new UnsupportedOperationException();
	}
}
