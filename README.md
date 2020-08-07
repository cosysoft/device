## Android Device API Based on ddmlib

A lot of code quote from selendroid,but we will foucs on simplify ddmlib's usage

“[device] is being sponsored by the following tool; please help to support us by taking a look and signing up to a free trial”
<!--<a href="https://tracking.gitads.io/?repo=device"> <img src="https://images.gitads.io/device" alt="GitAds"/> </a>-->


## device-keeper
A distributed android device monitor system based on device-api

### Quick Start

You require the following to build:

* Latest stable [Oracle JDK 7+](http://www.oracle.com/technetwork/java/)
* Latest stable [Gradle 2.4+](http://gradle.org/downloads/)
* Android SDK
* node.js and bower

And be sure that JAVA_HOME,ANDROID_HOME at your environment path.


Plug a android device via usb or boot an emulator

```bash
git clone https://github.com/cosysoft/device.git
cd device/device-keeper
bower install

cd ..
gradle bootRun
```
Open <http://localhost:8080/keeper> in your browser

## device-api
Focus on stabilized android device operation via Android Debug Bridge

### Quick Start
#### Download
Maven
```xml
<dependency>
		<groupId>com.github.cosysoft</groupId>
		<artifactId>device-api</artifactId>
		<version>0.9.3</version>
</dependency>
```
Gradle
```groovy
dependencies {
		compile 'com.github.cosysoft:device-api:0.9.3'
}
```


### Take Devices

```java
TreeSet<AndroidDevice> devices = AndroidDeviceStore.getInstance()
		.getDevices();

for (AndroidDevice d : devices) {
  System.out.println(d.getSerialNumber());
}
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
@Test
public void testListClients() {

  Client[] clients = device.getAllClient();
  for (Client client : clients) {
    ClientData clientData = client.getClientData();
    System.out.println(clientData.getClientDescription() + " " + clientData.getPid());
  }
}

```
### List selected app threads
```java

@Test
public void testListTheads() {

  Client runningApp = device.getClientByAppName("com.android.calendar");

  ThreadInfo[] threads = runningApp.getClientData().getThreads();

  for (int i = 0; i < threads.length; i++) {
    System.out.println(threads[i].getThreadName()
        + " at "
        + threads[i].getStatus());
  }
}
```
## License
[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
