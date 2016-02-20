package com.github.cosysoft.device.android;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;
import com.github.cosysoft.device.model.ClientDataInfo;
import com.github.cosysoft.device.model.DeviceInfo;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;

/**
 * @author ltyao
 */
public interface AndroidDevice {

  /**
   * test only
   */
  IDevice getDevice();

  String getSerialNumber();

  Locale getLocale();

  String getName();

  AndroidDeviceBrand getBrand();

  Dimension getScreenSize();

  void tap(int x, int y);

  void swipe(int x1, int y1, int x2, int y2);

  /**
   * @see KeyEvent
   */
  void inputKeyevent(int value);

  BufferedImage takeScreenshot();

  void takeScreenshot(String fileUrl);

  boolean isDeviceReady();

  boolean isScreenOn();

  DeviceTargetPlatform getTargetPlatform();

  String currentActivity();

  void invokeActivity(String activity);

  boolean start(AndroidApp app);

  /**
   * dump current activity view xml
   */
  String getDump();

  boolean handlePopBox(String deviceBrand);

  /**
   * io.appium.unlock/.Unlock
   */
  void unlock();

  void install(AndroidApp app);

  boolean isInstalled(String appBasePackage);

  boolean isInstalled(AndroidApp app);

  void uninstall(AndroidApp app);

  void uninstall(String appBasePackage);

  void forwardPort(int local, int remote);

  void removeForwardPort(int local);

  void clearUserData(AndroidApp app);

  void clearUserData(String appBasePackage);

  void kill(AndroidApp aut);

  void kill(String appBasePackage);

  String runAdbCommand(String parameter);

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

  /**
   * return all Dalvik/ART VM processes
   */
  Client[] getAllClient();

  /**
   *
   * @param appName
   * @return
   */
  Client getClientByAppName(String appName);
}
