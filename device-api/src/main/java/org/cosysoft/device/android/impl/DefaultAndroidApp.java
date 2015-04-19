package org.cosysoft.device.android.impl;

import org.apache.commons.exec.CommandLine;
import org.cosysoft.device.android.AndroidApp;
import org.cosysoft.device.model.AppInfo;
import org.cosysoft.device.shell.AndroidSdk;
import org.cosysoft.device.shell.AndroidSdkException;
import org.cosysoft.device.shell.ShellCommand;
import org.cosysoft.device.shell.ShellCommandException;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultAndroidApp implements AndroidApp {
	private File apkFile;
	private String mainPackage = null;
	protected String mainActivity = null;
	private String versionName = null;

	public DefaultAndroidApp(File apkFile) {
		this.apkFile = apkFile;
	}

	private String extractApkDetails(String regex)
			throws ShellCommandException, AndroidSdkException {
		CommandLine line = new CommandLine(AndroidSdk.aapt());

		line.addArgument("dump", false);
		line.addArgument("badging", false);
		line.addArgument(apkFile.getAbsolutePath(), false);
		String output="";
		try {
			output = ShellCommand.exec(line, 20000);
		} catch (Exception e) {
			output=e.getCause().getMessage();
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(output);
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	@Override
	public String getBasePackage() throws AndroidSdkException {
		if (mainPackage == null) {
			try {
				mainPackage = extractApkDetails("package: name='(.*?)'");
			} catch (ShellCommandException e) {
				
				
				throw new RuntimeException("The base package name of the apk "
						+ apkFile.getName() + " cannot be extracted.");
			}

		}
		return mainPackage;
	}

	@Override
	public String getMainActivity() throws AndroidSdkException {
		if (mainActivity == null) {
			try {
				mainActivity = extractApkDetails("launchable-activity: name='(.*?)'");
			} catch (ShellCommandException e) {
				throw new RuntimeException("The main activity of the apk "
						+ apkFile.getName() + " cannot be extracted.");
			}
		}
		return mainActivity;
	}

	public void setMainActivity(String mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void deleteFileFromWithinApk(String file)
			throws ShellCommandException, AndroidSdkException {
		CommandLine line = new CommandLine(AndroidSdk.aapt());
		line.addArgument("remove", false);
		line.addArgument(apkFile.getAbsolutePath(), false);
		line.addArgument(file, false);

		ShellCommand.exec(line, 20000);
	}

	@Override
	public String getAbsolutePath() {
		return apkFile.getAbsolutePath();
	}

	@Override
	public String getVersionName() throws AndroidSdkException {
		if (versionName == null) {
			try {
				versionName = extractApkDetails("versionName='(.*?)'");
			} catch (ShellCommandException e) {
				throw new RuntimeException("The versionName of the apk "
						+ apkFile.getName() + " cannot be extracted.");
			}
		}
		return versionName;
	}

	public String getAppId() throws AndroidSdkException {
		return getBasePackage() + ":" + getVersionName();
	}

	public AppInfo toAppInfo() {
		
		AppInfo appInfo = new AppInfo();
		appInfo.setMainActivity(this.getMainActivity());
		appInfo.setPackageURI(this.getAbsolutePath());
		appInfo.setBasePackage(this.getBasePackage());
		appInfo.setVersion(this.getVersionName());

		return appInfo;
	}
}
