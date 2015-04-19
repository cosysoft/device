package org.cosysoft.device.android.impl;

import com.android.ddmlib.*;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatReceiverTask;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cosysoft.device.android.AndroidApp;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.android.AndroidDeviceBrand;
import org.cosysoft.device.android.DeviceTargetPlatform;
import org.cosysoft.device.exception.AndroidDeviceException;
import org.cosysoft.device.exception.DeviceUnlockException;
import org.cosysoft.device.exception.NestedException;
import org.cosysoft.device.image.ImageUtils;
import org.cosysoft.device.model.ClientDataInfo;
import org.cosysoft.device.model.DeviceInfo;
import org.cosysoft.device.shell.AndroidSdk;
import org.cosysoft.device.shell.AndroidSdkException;
import org.cosysoft.device.shell.ShellCommand;
import org.cosysoft.device.shell.ShellCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDevice implements AndroidDevice {
    private static final Logger log = LoggerFactory
            .getLogger(AbstractDevice.class);
    protected String serial = null;
    protected IDevice device;
    private static final Integer COMMAND_TIMEOUT = 20000;
    private AndroidDeviceBrand brand = null;

    /**
     * Constructor meant to be used with Android Emulators because a reference
     * to the {@link IDevice} will become available if the emulator will be
     * started. Please make sure that #setIDevice is called on the emulator.
     *
     * @param serial
     */
    public AbstractDevice(String serial) {
        this.serial = serial;
    }

    /**
     * Constructor mean to be used with Android Hardware devices because a
     * reference to the {@link IDevice} will be available immediately after they
     * are connected.
     *
     * @param device
     */
    public AbstractDevice(IDevice device) {
        this.device = device;
        this.serial = device.getSerialNumber();
    }

    @Override
    public IDevice getDevice() {
        return device;
    }

    protected AbstractDevice() {
    }

    protected boolean isSerialConfigured() {
        return serial != null && !serial.isEmpty();
    }

    @Override
    public boolean isDeviceReady() {
        CommandLine command = adbCommand("shell", "getprop init.svc.bootanim");
        String bootAnimDisplayed = null;
        try {
            bootAnimDisplayed = ShellCommand.exec(command);
        } catch (ShellCommandException e) {
            log.info("Could not get property init.svc.bootanim", e);
        }
        return bootAnimDisplayed != null
                && bootAnimDisplayed.contains("stopped");
    }

    /**
     * ugly implementation
     */
    @Override
    public boolean isScreenOn() {
        CommandLine command = adbCommand("shell", "dumpsys power");
        try {
            String powerState = ShellCommand.exec(command).toLowerCase();
            if (powerState.indexOf("mscreenon=true") > -1
                    || powerState.indexOf("mpowerstate=0") == -1) {
                return true;
            }
        } catch (ShellCommandException e) {
            log.info("Could not get property init.svc.bootanim", e);
        }
        return false;
    }

    ;

    @Override
    public String currentActivity() {

        CommandLine command = adbCommand("shell", "dumpsys activity top");

        String out = executeCommandQuietly(command);
        if (out.indexOf("ACTIVITY") > -1) {
            try {
                List<String> lines = IOUtils.readLines(new StringReader(out));
                for (String line : lines) {
                    if (line.contains("ACTIVITY")) {
                        String[] tokens = StringUtils.split(line, " ");
                        return tokens[1];
                    }
                }
            } catch (IOException e) {
                log.debug("currentActivity {}", out);

            }
        }
        throw new NestedException("Can't get currentActivity");

    }

    @Override
    public void unlock() {
        String unlockPackage = "ctrip.cap.mi";
        String activity = ".CapMI";

        if (this.isInstalled(unlockPackage)) {
            innerUnlock(unlockPackage, activity);
            return;
        }

        unlockPackage = "io.appium.unlock";
        activity = ".Unlock";

        if (!this.isInstalled(unlockPackage)) {
            throw new DeviceUnlockException(
                    "UnLock app not installed on your device,Please install it manully.in windows You can try to execute"
                            + System.lineSeparator()
                            + "adb install "
                            + System.getProperty("user.home")
                            + "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\unlock_apk\\unlock_apk-debug.apk");
        }
        innerUnlock(unlockPackage, activity);

    }

    private void innerUnlock(String unlockPackage, String activity) {
        CommandLine command = adbCommand("shell", "am", "start", "-a",
                "android.intent.action.MAIN", "-n", unlockPackage + "/"
                        + activity);

        String out = executeCommandQuietly(command);
        try {
            // give it a second to recover from the activity start
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            log.warn("unlock", ie);
        }
        log.debug("unlock {}", out);
    }

    ;

    @Override
    public boolean isInstalled(String appBasePackage)
            throws AndroidSdkException {
        CommandLine command = adbCommand("shell", "pm", "list", "packages");

        command.addArgument(appBasePackage, false);
        String result = null;
        try {
            result = ShellCommand.exec(command);
        } catch (ShellCommandException e) {
        }

        return result != null && result.contains("package:" + appBasePackage);
    }

    @Override
    public boolean isInstalled(AndroidApp app) {
        return isInstalled(app.getBasePackage());
    }

    @Override
    public void install(AndroidApp app) {
        // Reinstall if already installed, Install otherwise
        CommandLine command = adbCommand("install", "-r", app.getAbsolutePath());

        String out = executeCommandQuietly(command, COMMAND_TIMEOUT * 6);
        try {
            // give it a second to recover from the install
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
        if (!out.contains("Success")) {
            throw new AndroidSdkException("APK installation failed. Output:\n"
                    + out);
        }
    }

    public boolean start(AndroidApp app) {
        if (!isInstalled(app)) {
            install(app);
        }

        String mainActivity = app.getMainActivity().replace(
                app.getBasePackage(), "");
        CommandLine command = adbCommand("shell", "am", "start", "-a",
                "android.intent.action.MAIN", "-n", app.getBasePackage() + "/"
                        + mainActivity);

        String out = executeCommandQuietly(command);
        try {
            // give it a second to recover from the activity start
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
        return out.contains("Starting: Intent");
    }

    protected String executeCommandQuietly(CommandLine command) {
        return executeCommandQuietly(command, COMMAND_TIMEOUT);
    }

    protected String executeCommandQuietly(CommandLine command, long timeout) {
        try {
            return ShellCommand.exec(command, timeout);
        } catch (ShellCommandException e) {
            String logMessage = String.format("Could not execute command: %s",
                    command);
            log.warn(logMessage, e);
            return "";
        }
    }

    @Override
    public void uninstall(String appBasePackage) {
        CommandLine command = adbCommand("uninstall", appBasePackage);

        executeCommandQuietly(command);
        try {
            // give it a second to recover from the uninstall
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    @Override
    public void uninstall(AndroidApp app) {
        uninstall(app.getBasePackage());
    }

    @Override
    public void clearUserData(String appBasePackage) {
        CommandLine command = adbCommand("shell", "pm", "clear", appBasePackage);
        executeCommandQuietly(command);
    }

    @Override
    public void clearUserData(AndroidApp app) {
        clearUserData(app.getBasePackage());
    }

    @Override
    public void kill(String appBasePackage) {
        try {
            CommandLine command = adbCommand("shell", "am", "force-stop",
                    appBasePackage);
            executeCommandQuietly(command);
        } finally {
        }

    }

    @Override
    public void kill(AndroidApp aut) {
        kill(aut.getBasePackage());
    }

    public void removeForwardPort(int port) {
        CommandLine command = adbCommand("forward", "--remove", "tcp:" + port);
        try {
            ShellCommand.exec(command, COMMAND_TIMEOUT);
        } catch (ShellCommandException e) {
            log.warn("Could not free Selendroid port", e);
        }
    }

    public void forwardPort(int local, int remote) {
        CommandLine command = adbCommand("forward", "tcp:" + local, "tcp:"
                + remote);
        try {
            ShellCommand.exec(command, COMMAND_TIMEOUT);
        } catch (ShellCommandException forwardException) {
            String debugForwardList;
            try {
                debugForwardList = ShellCommand.exec(
                        adbCommand("forward", "--list"), COMMAND_TIMEOUT);
            } catch (ShellCommandException listException) {
                debugForwardList = "Could not get list of forwarded ports.";
            }

            throw new RuntimeException("Could not forward port: " + command
                    + "\nList of forwarded ports:\n" + debugForwardList,
                    forwardException);
        }
    }

    protected String getProp(String key) {
        CommandLine command = adbCommand("shell", "getprop", key);
        String prop = executeCommandQuietly(command);

        return prop == null ? "" : prop.replace("\r", "").replace("\n", "");
    }

    protected static String extractValue(String regex, String output) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public String runAdbCommand(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return null;
        }
        CommandLine command = adbCommand();

        String[] params = parameter.split(" ");
        for (String param : params) {
            command.addArgument(param, false);
        }

        String commandOutput = executeCommandQuietly(command);
        return commandOutput.trim();
    }

    public BufferedImage takeScreenshot() {
        if (device == null) {
            throw new AndroidDeviceException(
                    "Device not accessible via ddmlib.");
        }
        RawImage rawImage;
        try {
            Profiler profiler = new Profiler("native screen");
            profiler.start("start");
            rawImage = device.getScreenshot();

            profiler.stop();
            profiler.print();
        } catch (IOException ioe) {
            throw new AndroidDeviceException("Unable to get frame buffer: "
                    + ioe.getMessage());
        } catch (TimeoutException e) {
            throw new AndroidDeviceException(e.getMessage());
        } catch (AdbCommandRejectedException e) {
            throw new AndroidDeviceException(e.getMessage());
        }

        BufferedImage image = ImageUtils.convertImage(rawImage);

        return image;
    }

    @Override
    public void takeScreenshot(String fileUrl) {
        BufferedImage image = takeScreenshot();
        ImageUtils.writeToFile(image, fileUrl);
    }

    /**
     * Use adb to send a keyevent to the device.
     * <p>
     * Full list of keys available here:
     * http://developer.android.com/reference/android/view/KeyEvent.html
     *
     * @param value - Key to be sent to 'adb shell input keyevent'
     */
    public void inputKeyevent(int value) {
        executeCommandQuietly(adbCommand("shell", "input", "keyevent", ""
                + value));
        // need to wait a beat for the UI to respond
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn("", e);
        }
    }

    public void invokeActivity(String activity) {
        executeCommandQuietly(adbCommand("shell", "am", "start", "-a", activity));
        // need to wait a beat for the UI to respond
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn("", e);
        }
    }

    public void restartADB() {
        executeCommandQuietly(adbCommand("kill-server"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn("", e);
        }
        // make sure it's backup again
        executeCommandQuietly(adbCommand("devices"));
    }

    private CommandLine adbCommand() {
        CommandLine command = new CommandLine(AndroidSdk.adb());
        if (isSerialConfigured()) {
            command.addArgument("-s", false);
            command.addArgument(serial, false);
        }
        return command;
    }

    private CommandLine adbCommand(String... args) {
        CommandLine command = adbCommand();
        for (String arg : args) {
            command.addArgument(arg, false);
        }
        return command;
    }

    public String getExternalStoragePath() {
        return runAdbCommand("shell echo $EXTERNAL_STORAGE");
    }

    /**
     * Get crash log from AUT
     *
     * @return empty string if there is no crash log on the device, otherwise
     * returns the stack trace caused by the crash of the AUT
     */
    public String getCrashLog() {
        String crashLogFileName = null;
        File crashLogFile = new File(getExternalStoragePath(), crashLogFileName);

        // the "test" utility doesn't exist on all devices so we'll check the
        // output of ls.
        CommandLine directoryListCommand = adbCommand("shell", "ls",
                crashLogFile.getParentFile().getAbsolutePath());
        String directoryList = executeCommandQuietly(directoryListCommand);
        if (directoryList.contains(crashLogFileName)) {
            return executeCommandQuietly(adbCommand("shell", "cat",
                    crashLogFile.getAbsolutePath()));
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass() || device == null)
            return false;

        AbstractDevice that = (AbstractDevice) o;

        return device.equals(that.device);
    }

    @Override
    public void tap(int x, int y) {
        CommandLine command = adbCommand("shell", "input", "tap",
                String.valueOf(x), String.valueOf(y));

        executeCommandQuietly(command, COMMAND_TIMEOUT * 6);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    ;

    @Override
    public void swipe(int x1, int y1, int x2, int y2) {
        CommandLine command = adbCommand("shell", "input", "swipe",
                String.valueOf(x1), String.valueOf(y1), String.valueOf(x2),
                String.valueOf(y2));

        executeCommandQuietly(command, COMMAND_TIMEOUT * 6);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    @Override
    public boolean isWifiOff() {
        CommandLine command = adbCommand("shell", "settings", "get", "global",
                "wifi_on");
        String commandOutput = executeCommandQuietly(command);
        String result = commandOutput.trim();

        return "1".equals(result) ? false : true;

    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public AndroidDeviceBrand getBrand() {
        if (brand != null) {
            return brand;
        }
        String name = getName();
        String manufacture = StringUtils.substringBefore(name, "-");
        String model = StringUtils.substringBetween(name, "-", "-");
        brand = AndroidDeviceBrand.from(manufacture, model);
        return brand;

    }

    ;

    @Override
    public DeviceInfo getDeviceInfo() {

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setName(this.getName());
        deviceInfo.setSerial(this.getSerialNumber());
        deviceInfo.setDensity(device.getDensity());
        DeviceTargetPlatform tf = this.getTargetPlatform();
        deviceInfo.setOsName(tf.formatedName());

        try {
            deviceInfo.setKernel(device.getSystemProperty("ro.build.kernel.id")
                    .get());
            deviceInfo.setBattery(device.getBattery().get());

        } catch (InterruptedException | ExecutionException e) {
            log.warn("getDeviceInfo", e);
        }

        return deviceInfo;

    }

    ;

    private LogCatReceiverTask logCatReceiverTask;
    private final Set<LogCatListener> logCatListeners = new HashSet<>();
    ;
    private boolean logCatRunning = false;

    @Override
    public synchronized void addLogCatListener(LogCatListener logCatListener) {
        if (logCatReceiverTask == null) {
            logCatReceiverTask = new LogCatReceiverTask(device);
        }
        logCatReceiverTask.addLogCatListener(logCatListener);
        logCatListeners.add(logCatListener);
        if (!logCatRunning) {
            logCatReceiverTask.run();
            logCatRunning = true;
        }
    }

    @Override
    public synchronized void removeLogCatListener(LogCatListener logCatListener) {
        if (logCatReceiverTask == null) {
            logCatReceiverTask = new LogCatReceiverTask(device);
        }
        logCatReceiverTask.removeLogCatListener(logCatListener);
        logCatListeners.remove(logCatListener);
        if (logCatListeners.size() < 1) {
            logCatReceiverTask.stop();
            logCatRunning = false;
        }
    }

    @Override
    public List<ClientDataInfo> getClientDatasInfo() {
        List<ClientDataInfo> dataInfos = new ArrayList<ClientDataInfo>();
        Client clients[] = this.device.getClients();
        for (int i = 0; i < clients.length; i++) {
            dataInfos.add(new ClientDataInfo(clients[i]));
        }
        return dataInfos;
    }

    @Override
    public Client getClientByAppName(String appName) {
        return null;
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }
}
