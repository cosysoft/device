package com.github.cosysoft.device.android.xiaomi;

import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.AndroidDevice;
import com.github.cosysoft.device.android.impl.AndroidDeviceStore;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import com.github.cosysoft.device.exception.DeviceNotFoundException;
import java.io.File;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for Avatar or other non Java Platform
 * 
 * @author ltyao
 *
 */
public class MIInstaller {

	private static Logger logger = LoggerFactory.getLogger(MIInstaller.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ParseException {
		CommandLineParser parser = new GnuParser();

		Options options = new Options();

		options.addOption("i", "install", true, "Install App");
		options.addOption("h", "help", false, "Help");

		options.addOption(OptionBuilder.withArgName("udid").withLongOpt("udid")
				.withDescription("udid").hasArg().create());

		CommandLine line = parser.parse(options, args);

		// java -jar cap-device-1.0.0-SNAPSHOT-MIInstaller.jar --udid
		// HC46FWY03303 -i d
		if (args.length == 0 || line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("help", options);
			return;
		}

		String install = line.getOptionValue("i");
		String udid = line.getOptionValue("udid");

		logger.info(String.format(
				"with arguments:\n install package %s\n udid %s ", install,
				udid));

		if (install == null) {
			logger.info("app package path needed!");
			return;
		}
		File pkg = new File(install);
		if (!pkg.exists()) {
			logger.info("app package path is not exist");
			return;
		}
		install(udid, install);

	}

	private static void install(String udid, String iPackage) {

		AndroidDevice device = getDevice(udid);

		try {
			MIDeviceUtility.testInstall(device);
		} catch (Exception e) {
			logger.error("", e);
		}
		try {
			AndroidApp app = new DefaultAndroidApp(new File(iPackage));
			if (device.isInstalled(app)) {
				device.uninstall(app);
			}
			device.install(app);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			AndroidDeviceStore.getInstance().shutdownForcely();
		}

	}

	private static AndroidDevice getDevice(String udid) {

		Set<AndroidDevice> devices = AndroidDeviceStore.getInstance()
				.getDevices();
		for (AndroidDevice androidDevice : devices) {
			if (androidDevice.getSerialNumber().equals(udid)) {
				return androidDevice;
			}
		}
		throw new DeviceNotFoundException(String.format("udid %s not founded",
				udid));
	}

}
