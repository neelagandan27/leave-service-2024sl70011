package com.bits.leave;

import com.sun.net.httpserver.HttpServer;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {

    @Test
    public void testServerResponse() throws Exception {
        HttpServer server = Main.createServer(0, "staging");

        try {
            server.start();

            int port = server.getAddress().getPort();

            URL url = new URL("http://localhost:" + port + "/");
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            assertEquals(200, connection.getResponseCode());

            String response = new String(
                    connection.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);

            assertTrue(response.contains("Environment: staging"));
            assertTrue(response.contains("Remaining Leaves: 15"));
            assertTrue(response.contains("Encashment: Rs. 22500.0"));

            connection.disconnect();
        } finally {
            server.stop(0);
        }
    }

    @Test
public void testMainStartsServer() throws Exception {
    Main.main(new String[]{"0"});

    try {
        assertTrue(Main.server.isRunning());

        int port = Main.server.getAddress().getPort();

        URL url = new URL("http://localhost:" + port + "/");
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        assertEquals(200, connection.getResponseCode());

        String response = new String(
                connection.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8);

        assertTrue(response.contains("Environment: development"));
        assertTrue(response.contains("Remaining Leaves: 15"));

        connection.disconnect();
    } finally {
        Main.server.stop(0);
        Main.server = null;
    }
}
}
