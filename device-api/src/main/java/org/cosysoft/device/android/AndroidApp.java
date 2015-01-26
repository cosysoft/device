package org.cosysoft.device.android;

import org.cosysoft.device.shell.AndroidSdkException;
import org.cosysoft.device.shell.ShellCommandException;

public interface AndroidApp {

	String getBasePackage() throws AndroidSdkException;

	String getMainActivity() throws AndroidSdkException;

	void setMainActivity(String mainActivity);

	String getVersionName() throws AndroidSdkException;

	void deleteFileFromWithinApk(String file) throws ShellCommandException,
			AndroidSdkException;

	String getAppId() throws AndroidSdkException;

	/**
	 * For testing only
	 */
	String getAbsolutePath();
}
