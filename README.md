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

### LogCat with custom filter
```java
final LogCatFilter filter = new LogCatFilter("", "", "com.android", "",
		"", LogLevel.WARN);
final LogCatListener lcl = new LogCatListener() {
	@Override
	public void log(List<LogCatMessage> msgList) {
		for (LogCatMessage msg : msgList) {
			if (filter.matches(msg)) {
				System.out.println(msg);
			}
		}
	}
};

device.addLogCatListener(lcl);
Thread.sleep(60000);
```

## Monitor
Ddmlib can monitor one app's cpu/heap/threads and much more,but we need list running client first.

### List running client for app
```java
List<ClientDataInfo> clientDataInfos = device.getClientDatasInfo();
for (ClientDataInfo client : clientDataInfos) {
	System.out.println(client.getName());
	System.out.println(client.getPid());
}
```

## License
[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
