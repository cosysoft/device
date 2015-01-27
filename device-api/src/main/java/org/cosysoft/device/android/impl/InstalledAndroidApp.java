package org.cosysoft.device.android.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cosysoft.device.android.AndroidApp;

public class InstalledAndroidApp implements AndroidApp {

	private String packageName;
	private String activityName;
	private String version;

	public InstalledAndroidApp(String appInfo) {
		Pattern infoPattern = Pattern.compile("(.+):(.+)/(.+)");
		Matcher patternMatcher = infoPattern.matcher(appInfo);
		if (patternMatcher.matches()) {
			packageName = patternMatcher.group(1);
			version = patternMatcher.group(2);
			activityName = patternMatcher.group(3);
		} else {
			throw new RuntimeException(
					"Format for installed app is:  tld.company.app:version/ActivityClass");
		}
	}

	@Override
	public String getBasePackage() {
		return packageName;
	}

	@Override
	public String getMainActivity() {
		return (activityName.contains(".")) ? activityName : packageName + "."
				+ activityName;
	}

	public void setMainActivity(String mainActivity) {
		this.activityName = mainActivity;
	}

	@Override
	public String getVersionName() {
		return version;
	}

	@Override
	public void deleteFileFromWithinApk(String file) {
		// no-op
	}

	@Override
	public String getAppId() {
		return packageName + ":" + version;
	}

	@Override
	public String getAbsolutePath() {
		return null;
	}

}