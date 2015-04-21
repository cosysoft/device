package com.github.cosysoft.device.image;

import com.android.ddmlib.RawImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author ltyao
 */
public class ImageUtils {

    static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    private static Hashtable<?, ?> EMPTY_HASH = new Hashtable<>();
    private static int[] BAND_OFFSETS_32 = {0, 1, 2, 3};
    private static int[] BAND_OFFSETS_16 = {0, 1};

    public static BufferedImage convertImage(RawImage rawImage) {
        switch (rawImage.bpp) {
            case 16:
                return rawImage16toARGB(rawImage);
            case 32:
                return rawImage32toARGB(rawImage);
        }
        return null;
    }

    static int getMask(int length) {
        int res = 0;
        for (int i = 0; i < length; i++) {
            res = (res << 1) + 1;
        }
        return res;
    }

    private static BufferedImage rawImage32toARGB(RawImage rawImage) {
        DataBufferByte dataBuffer = new DataBufferByte(rawImage.data,
                rawImage.size);

        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(
                0, rawImage.width, rawImage.height, 4, rawImage.width * 4,
                BAND_OFFSETS_32);

        WritableRaster raster = Raster.createWritableRaster(sampleModel,
                dataBuffer, new Point(0, 0));

        return new BufferedImage(new ThirtyTwoBitColorModel(rawImage), raster,
                false, EMPTY_HASH);
    }

    private static BufferedImage rawImage16toARGB(RawImage rawImage) {
        DataBufferByte dataBuffer = new DataBufferByte(rawImage.data,
                rawImage.size);

        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(
                0, rawImage.width, rawImage.height, 2, rawImage.width * 2,
                BAND_OFFSETS_16);

        WritableRaster raster = Raster.createWritableRaster(sampleModel,
                dataBuffer, new Point(0, 0));

        return new BufferedImage(new SixteenBitColorModel(rawImage), raster,
                false, EMPTY_HASH);
    }

    public static byte[] toByteArray(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "gif", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }

    public static void writeToFile(BufferedImage image, String filePath) {

        try {
            ImageIO.write(image, "png", new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean sameAs(BufferedImage myImage,
                                 BufferedImage otherImage, double percent) {
        long start = System.currentTimeMillis();

        int width = myImage.getWidth();
        int height = myImage.getHeight();

        int numDiffPixels = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
                    numDiffPixels++;
                }
            }
        }
        double numberPixels = height * width;
        double rs = 1.0 - (int) ((numDiffPixels / numberPixels) * 100) / 100.0;

        long now = System.currentTimeMillis();
        logger.debug("picture compare spend {} millseconds and result is", now
                - start, rs);
        return rs >= percent;
    }
}
