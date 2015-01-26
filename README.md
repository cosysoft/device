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
xiaomi-2013022-ZDPB8PVCO7QGYDQG

### Screenshot

```java

AndroidDevice device = getDevices().pollFirst();
BufferedImage image = device.takeScreenshot();
ImageUtils.writeToFile(image, "d:\\a.png");

```
