package org.cosysoft.device.android;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Locale;

import org.cosysoft.device.model.DeviceInfo;

import com.android.ddmlib.log.LogReceiver;

/**
 * 
 * @author ltyao
 *
 */
public interface AndroidDevice {

	String getSerialNumber();

	Locale getLocale();

	String getName();

	AndroidDeviceBrand getBrand();

	Dimension getScreenSize();

	void tap(int x, int y);

	void swipe(int x1, int y1, int x2, int y2);

	BufferedImage takeScreenshot();

	void takeScreenshot(String fileUrl);

	boolean isDeviceReady();

	boolean isScreenOn();

	DeviceTargetPlatform getTargetPlatform();
	
	String currentActivity();

	void invokeActivity(String activity);
	/**
	 * io.appium.unlock/.Unlock
	 */
	void unlock();

	void install(AndroidApp app);

	boolean isInstalled(String appBasePackage);

	boolean isInstalled(AndroidApp app);

	void uninstall(AndroidApp app);

	void uninstall(String appBasePackage);

	boolean start(AndroidApp app);

	void forwardPort(int local, int remote);

	void removeForwardPort(int local);

	void clearUserData(AndroidApp app);

	void clearUserData(String appBasePackage);

	void kill(AndroidApp aut);

	void kill(String appBasePackage);

	String runAdbCommand(String parameter);

	/**
	 * @see KeyEvent
	 * @param value
	 */
	void inputKeyevent(int value);

	String getExternalStoragePath();

	String getCrashLog();

	boolean isWifiOff();

	DeviceInfo getDeviceInfo();
	
	void restartADB();
	
	void runLogService(LogReceiver logReceiver);

}
