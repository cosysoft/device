package com.github.cosysoft.device.android.impl;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.github.cosysoft.device.android.DeviceTargetPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Locale;

/**
 * reviewed
 * 
 *
 */
public class DefaultHardwareDevice extends AbstractDevice implements
		Comparable<AbstractDevice> {

	private static final Logger log = LoggerFactory
			.getLogger(DefaultHardwareDevice.class);

	private Locale locale = null;
	private DeviceTargetPlatform targetPlatform = null;
	private Dimension screenSize = null;

	public DefaultHardwareDevice(IDevice device) {
		super(device);
	}

	@Override
	protected String getProp(String key) {
		return device.getProperty(key);
	}

	@Override
	public DeviceTargetPlatform getTargetPlatform() {
		if (targetPlatform == null) {
			String version = getProp("ro.build.version.sdk");
			targetPlatform = DeviceTargetPlatform.fromInt(version);
		}
		return targetPlatform;
	}

	@Override
	public Dimension getScreenSize() {
		if (this.screenSize == null) {
			try {
				RawImage screenshot = device.getScreenshot();
				this.screenSize = new Dimension(screenshot.width,
						screenshot.height);
			} catch (Exception e) {
				log.warn("was not able to determine screensize: "
						+ e.getMessage());
			}
		}

		return this.screenSize;
	}

	@Override
	public Locale getLocale() {
		if (this.locale == null) {
			String language = getProp("persist.sys.language");
			String country = getProp("persist.sys.country");
			if (language != null && country != null) {
				this.locale = new Locale(language, country);
			}

		}
		return locale;
	}

	@Override
	public boolean isDeviceReady() {
		return true;
	}

	@Override
	public String getSerialNumber() {
		return serial;
	}

	@Override
	public String toString() {
		return "DefaultHardwareDevice [brand=" + getBrand() + ", locale="
				+ getLocale() + ", targetVersion=" + getTargetPlatform()
				+ ", getName()=" + getName() + "]";
	}

	@Override
	public int compareTo(AbstractDevice o) {
		return this.serial.compareTo(o.serial);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultHardwareDevice other = (DefaultHardwareDevice) obj;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		return true;
	}

}
