package com.github.cosysoft.device.android.test;

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.ThreadInfo;
import com.github.cosysoft.device.android.AndroidDevice;
import org.junit.Before;
import org.junit.Test;

public class PerformanceTest extends AndroidDeviceTest {

  private AndroidDevice device;

  @Before
  public void takeOne() throws InterruptedException {
    device = getDevices().pollFirst();
    Thread.sleep(2000);  //just wait 2 second at first time to collect client info
  }

  @Test
  public void testListClients() {

    Client[] clients = device.getAllClient();
    for (Client client : clients) {
      ClientData clientData = client.getClientData();
      System.out.println(clientData.getClientDescription() + " " + clientData.getPid());
    }
  }

  @Test
  public void testListTheads() {

    Client runningApp = device.getClientByAppName("com.android.calendar");

    ThreadInfo[] threads = runningApp.getClientData().getThreads();

    for (int i = 0; i < threads.length; i++) {
      System.out.println(threads[i].getThreadName()
          + " at "
          + threads[i].getStatus());
    }
  }
}
