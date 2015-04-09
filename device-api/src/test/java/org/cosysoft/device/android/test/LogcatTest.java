package org.cosysoft.device.android.test;

import java.util.List;

import org.cosysoft.device.android.AndroidDevice;
import org.junit.Test;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatFilter;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;

public class LogcatTest extends AndroidDeviceTest {

	@Test
	public void testLog() throws InterruptedException {
		AndroidDevice device = pollFirst();

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

		Thread.sleep(50000000);
	}

}
