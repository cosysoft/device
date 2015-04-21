package com.github.cosysoft.device.android.test;

import static org.junit.Assert.*;

import java.io.File;

import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import org.junit.Test;

public class AndroidAppTest {

	@Test
	public void testExtract() {
		AndroidApp app = new DefaultAndroidApp(new File("d:\\aut\\qua.apk"));

		assertEquals("com.taobao.trip", app.getBasePackage());

	}

}
