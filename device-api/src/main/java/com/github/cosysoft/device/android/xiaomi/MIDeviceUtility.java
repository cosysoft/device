package com.github.cosysoft.device.android.xiaomi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.AndroidDevice;
import com.github.cosysoft.device.android.AndroidDeviceBrand;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fix xiaomi3 device installer popup problem
 * 
 * @author ltyao
 *
 */
public class MIDeviceUtility {

	private static final Logger logger = LoggerFactory
			.getLogger(MIDeviceUtility.class);

	public static boolean install(AndroidDevice device, AndroidApp app) {

		boolean result = false;
		ExecutorService installExecutor = Executors.newFixedThreadPool(2);
		MIDevice miDevice = new MIDevice(device);
		InstallHelperTask helper = new InstallHelperTask(miDevice);
		InstallTask installTask = new InstallTask(miDevice, app);

		Future<Boolean> rs = installExecutor.submit(installTask);
		installExecutor.submit(helper);
		installExecutor.shutdown();

		try {
			Boolean rsw = rs.get(60000, TimeUnit.MILLISECONDS);
			logger.info("iresult=" + rsw);
			result = device.isInstalled(app);

		} catch (Exception e) {
			logger.warn("xiaomi install ", e);
		} finally {
			installExecutor.shutdownNow();
		}
		logger.info("Xiaomi Installer result={}", result);
		return result;
	}

	public static boolean testInstall(AndroidDevice device) {

		boolean result = false;
		InputStream io = MIDeviceUtility.class
				.getResourceAsStream("CapMI_1.0.apk");
		String serial = device.getSerialNumber();
		File dest = new File(FileUtils.getTempDirectory(), serial + ".apk");
		try {
			FileUtils.copyInputStreamToFile(io, dest);
			AndroidApp app = new DefaultAndroidApp(dest);
			device.unlock();
			device.uninstall(app);
			result = install(device, app);

		} catch (IOException e) {
			logger.warn("mi test install ", e);
		}
		return result;

	}

	public static class MIDevice {

		public AndroidDevice getDevice() {
			return device;
		}

		private AndroidDevice device;
		private AndroidDeviceBrand brand = null;

		public MIDevice(AndroidDevice device) {
			this.device = device;
			this.brand = device.getBrand();
		}

		public void unlock() {
			device.unlock();
		}

		public void crossConfirmView() {
			if (AndroidDeviceBrand.XIAOMI_MI_3.equals(brand)
					|| AndroidDeviceBrand.XIAOMI_MI_3W.equals(brand)) {
				device.tap(780, 1800);
			} else if (AndroidDeviceBrand.XIAOMI_MI_2.equals(brand)) {
				device.tap(510, 1190);
			}

		}

		public boolean isConfirm() {
			String activity = device.currentActivity();
			return StringUtils.containsIgnoreCase(activity,
					"PackageInstallerActivity");

		}

	}

	static class InstallTask implements Callable<Boolean> {

		MIDevice miDevice;
		AndroidApp app;

		public InstallTask(MIDevice miDevice, AndroidApp app) {
			super();
			this.miDevice = miDevice;
			this.app = app;
		}

		public InstallTask(AndroidApp app) {
			this.app = app;
		}

		@Override
		public Boolean call() throws Exception {
			miDevice.getDevice().unlock();
			miDevice.getDevice().install(app);
			return true;

		}

	}

	static class InstallHelperTask implements Callable<Void> {

		protected volatile boolean finished = false;
		MIDevice miDevice;

		public InstallHelperTask(MIDevice miDevice) {
			super();
			this.miDevice = miDevice;
		}

		@Override
		public Void call() throws Exception {
			try {

				long milliseconds = 10000;
				long start = System.currentTimeMillis();
				int count = 0;
				while ((System.currentTimeMillis() - start) < milliseconds) {
					Thread.sleep(500);
					if (miDevice.isConfirm() && count++ > 1) {
						logger.debug("确定安装");
						miDevice.crossConfirmView();
						break;
					} else {
						logger.debug("Wait PackageInstaller Activity");
					}
				}

			} catch (Exception e) {
				// logger.info("InstallHelperTask 超时后任务暂停", e);
			} finally {
				logger.debug("fishined");
				finished = true;
			}
			return null;
		}

	}

}
