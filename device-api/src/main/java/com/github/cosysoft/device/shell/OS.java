package com.github.cosysoft.device.shell;

public class OS {
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}

	public static String platformExecutableSuffixExe() {
		return isWindows() ? ".exe" : "";
	}

	public static String platformExecutableSuffixBat() {
		return isWindows() ? ".bat" : "";
	}
}
