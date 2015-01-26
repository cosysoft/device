package org.cosysoft.device.image;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import com.android.ddmlib.RawImage;

class ThirtyTwoBitColorModel extends ColorModel {
	private static final int[] BITS = { 8, 8, 8, 8 };
	private final int alphaLength;
	private final int alphaMask;
	private final int alphaOffset;
	private final int blueMask;
	private final int blueLength;
	private final int blueOffset;
	private final int greenMask;
	private final int greenLength;
	private final int greenOffset;
	private final int redMask;
	private final int redLength;
	private final int redOffset;

	public ThirtyTwoBitColorModel(RawImage rawImage) {
		super(32, BITS, ColorSpace.getInstance(1000), true, false, 3, 0);

		this.redOffset = rawImage.red_offset;
		this.redLength = rawImage.red_length;
		this.redMask = ImageUtils.getMask(this.redLength);
		this.greenOffset = rawImage.green_offset;
		this.greenLength = rawImage.green_length;
		this.greenMask = ImageUtils.getMask(this.greenLength);
		this.blueOffset = rawImage.blue_offset;
		this.blueLength = rawImage.blue_length;
		this.blueMask = ImageUtils.getMask(this.blueLength);
		this.alphaLength = rawImage.alpha_length;
		this.alphaOffset = rawImage.alpha_offset;
		this.alphaMask = ImageUtils.getMask(this.alphaLength);
	}

	public boolean isCompatibleRaster(Raster raster) {
		return true;
	}

	private int getPixel(Object inData) {
		byte[] data = (byte[]) inData;
		int value = data[0] & 0xFF;
		value |= (data[1] & 0xFF) << 8;
		value |= (data[2] & 0xFF) << 16;
		value |= (data[3] & 0xFF) << 24;

		return value;
	}

	public int getAlpha(Object inData) {
		int pixel = getPixel(inData);
		if (this.alphaLength == 0) {
			return 255;
		}
		return (pixel >>> this.alphaOffset & this.alphaMask) << 8 - this.alphaLength;
	}

	public int getBlue(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >>> this.blueOffset & this.blueMask) << 8 - this.blueLength;
	}

	public int getGreen(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >>> this.greenOffset & this.greenMask) << 8 - this.greenLength;
	}

	public int getRed(Object inData) {
		int pixel = getPixel(inData);
		return (pixel >>> this.redOffset & this.redMask) << 8 - this.redLength;
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
