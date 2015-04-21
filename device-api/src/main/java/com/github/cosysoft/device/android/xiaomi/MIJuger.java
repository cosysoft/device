package com.github.cosysoft.device.android.xiaomi;
/*package com.ctrip.cap.device.android.xiaomi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.cap.device.image.ImageUtils;

*//**
 * 
 * @author ltyao
 *
 *//*
public interface MIJuger {

	public static final int iHeight = 300;

	boolean isConfirm(BufferedImage image);

	public static class MI3Juger implements MIJuger {

		private static final Logger logger = LoggerFactory
				.getLogger(MI3Juger.class);

		private static BufferedImage CONFIRM_IMAGE = null;
		private static BufferedImage LOCKED_IMAGE = null;

		static {
			try {
				CONFIRM_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m3/isure.png"));
				LOCKED_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m3/lock.png"));
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		public boolean isConfirm(BufferedImage image) {
			BufferedImage sub = image.getSubimage(0, image.getHeight()
					- iHeight, image.getWidth(), iHeight);
			toTmpFile(sub);
			return ImageUtils.sameAs(sub, CONFIRM_IMAGE, 0.8);
		}

		public boolean isLocked(BufferedImage image) {
			return ImageUtils.sameAs(image, LOCKED_IMAGE, 0.8);
		}

		private static void toTmpFile(BufferedImage sub) {
			try {
				File tmp = File.createTempFile("xiaomi-sub", ".png");
				ImageIO.write(sub, "png", tmp);
			} catch (IOException e) {
				logger.warn("toTempFile", e);
			}
		}

	}

	public class MI3WJuger implements MIJuger {

		private static final Logger logger = LoggerFactory
				.getLogger(MI3WJuger.class);

		private static BufferedImage CONFIRM_IMAGE = null;
		private static BufferedImage LOCKED_IMAGE = null;

		static {
			try {
				CONFIRM_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m3/isure.png"));
				LOCKED_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m3/lock.png"));
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		public boolean isConfirm(BufferedImage image) {
			BufferedImage sub = image.getSubimage(0, image.getHeight()
					- iHeight, image.getWidth(), iHeight);
			toTmpFile(sub);
			return ImageUtils.sameAs(sub, CONFIRM_IMAGE, 0.8);
		}

		public boolean isLocked(BufferedImage image) {
			return ImageUtils.sameAs(image, LOCKED_IMAGE, 0.8);
		}

		private static void toTmpFile(BufferedImage sub) {
			try {
				File tmp = File.createTempFile("xiaomi-sub", ".png");
				ImageIO.write(sub, "png", tmp);
			} catch (IOException e) {
				logger.warn("toTempFile", e);
			}
		}

	}

	public class MI2Juger implements MIJuger {
		private static final Logger logger = LoggerFactory
				.getLogger(MI3Juger.class);

		private static BufferedImage M2_CONFIRM_IMAGE = null;
		private static BufferedImage M2_LOCKED_IMAGE = null;

		static {
			try {
				M2_CONFIRM_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m2/isure.png"));
				M2_LOCKED_IMAGE = ImageIO.read(MIJuger.class
						.getResourceAsStream("m2/lock.png"));
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		@Override
		public boolean isConfirm(BufferedImage image) {

			BufferedImage sub = image.getSubimage(0, image.getHeight()
					- iHeight, image.getWidth(), iHeight);

			return ImageUtils.sameAs(sub, M2_CONFIRM_IMAGE, 0.8);
		}

		public boolean isLocked(BufferedImage image) {
			return ImageUtils.sameAs(image, M2_LOCKED_IMAGE, 0.8);
		}
	}

}*/
