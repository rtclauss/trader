/*
       Copyright 2017 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ibm.hybrid.cloud.sample.stocktrader.trader.test;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class HealthEndpointIT {

    private String port = System.getProperty("http.port");
    private String warContext = System.getProperty("war.name");
    private String liveUrl = "http://localhost:" + port + "/health/live";
    private String readyUrl = "http://localhost:" + port + "/health/ready";
    private String url = "http://localhost:" + port;
    private static final int MAX_RETRY_COUNT = 5;
    private static final int SLEEP_TIMEOUT = 3000;

    @Test
    public void testLiveEndpoint() throws Exception {
        
        System.out.println("Testing endpoint " + liveUrl );
        int responseCode = makeRequest(liveUrl);
        for(int i = 0; (responseCode != 200) && (i < MAX_RETRY_COUNT); i++) {
          System.out.println("Response code : " + responseCode + ", retrying ... (" + i + " of " + MAX_RETRY_COUNT + ")");
          Thread.sleep(SLEEP_TIMEOUT);
          responseCode = makeRequest(liveUrl);
        }
        assertTrue("Incorrect response code: " + responseCode, responseCode == 200);
    }

    @Test
    public void testReadyEndpoint() throws Exception {
        System.out.println("Testing endpoint " + readyUrl);
        int responseCode = makeRequest(readyUrl);
        for(int i = 0; (responseCode != 200) && (i < MAX_RETRY_COUNT); i++) {
          System.out.println("Response code : " + responseCode + ", retrying ... (" + i + " of " + MAX_RETRY_COUNT + ")");
          Thread.sleep(SLEEP_TIMEOUT);
          responseCode = makeRequest(readyUrl);
        }
        assertTrue("Incorrect response code: " + responseCode, responseCode == 200);
    }

    private int makeRequest(String urlToTest) {
      Client client = ClientBuilder.newClient();
      Invocation.Builder invoBuild = client.target(urlToTest).request();
      Response response = invoBuild.get();
      int responseCode = response.getStatus();
      response.close();
      return responseCode;
    }
}
