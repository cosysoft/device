## Android Device API Based on ddmlib

A lot of code quote from selendroid,but we will foucs on simplify ddmlib's usage


## Device

### Take Devices 
```java
TreeSet<AndroidDevice> devices = AndroidDeviceStore.getInstance().getDevices();
AndroidDevice device = devices.pollFirst();

```
