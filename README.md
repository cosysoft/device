## Android Device API Based on ddmlib

A lot of code quote from selendroid,but we will foucs on simplify ddmlib's usage


## Device

### Take Devices 

```java
		TreeSet<AndroidDevice> devices = AndroidDeviceStore.getInstance()
				.getDevices();
		AndroidDevice device = devices.pollFirst();
		System.out.println(device.getName());

```

### Screenshot

```java
		BufferedImage image = device.takeScreenshot();
		String imagePath = new File(System.getProperty("java.io.tmpdir"),
				"screenshot.png").getAbsolutePath();
		ImageUtils.writeToFile(image, imagePath);

```

### Install/Uninstall App

```java
		AndroidApp app = new DefaultAndroidApp(new File(
				"d:\\uat\\com.android.chrome.apk"));
		device.install(app);
		if (device.isInstalled(app)) {
			device.uninstall(app);
		}
```
