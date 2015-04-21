package com.github.cosysoft.device.android;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;

import com.github.cosysoft.device.model.ClientDataInfo;
import com.github.cosysoft.device.model.DeviceInfo;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;

/**
 * 
 * @author ltyao
 *
 */
public interface AndroidDevice {

	/**
	 * test only
	 * 
	 * @return
	 */
	IDevice getDevice();

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

	/**
	 * 
	 * @param logCatListener
	 */
	void addLogCatListener(LogCatListener logCatListener);

	void removeLogCatListener(LogCatListener logCatListener);

	List<ClientDataInfo> getClientDatasInfo();

	Client getClientByAppName(String appName);

}
