package org.cosysoft.device.model;

public class AppInfo {

	private String packageURI;
	private String basePackage;
	private String mainActivity;
	private String version;

	public String getPackageURI() {
		return packageURI;
	}

	public void setPackageURI(String packageURI) {
		this.packageURI = packageURI;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(String mainActivity) {
		this.mainActivity = mainActivity;
	}

}
