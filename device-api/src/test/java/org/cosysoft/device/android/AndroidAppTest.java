package org.cosysoft.device.android;

import static org.junit.Assert.*;

import java.io.File;

import org.cosysoft.device.android.AndroidApp;
import org.cosysoft.device.android.impl.DefaultAndroidApp;
import org.junit.Test;

public class AndroidAppTest {

	@Test
	public void testExtract() {
		AndroidApp app = new DefaultAndroidApp(new File("d:\\aut\\qua.apk"));

		assertEquals("com.taobao.trip", app.getBasePackage());

	}

}
