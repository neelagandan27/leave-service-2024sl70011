package com.bits.leave;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {

    static HttpServer createServer(int port, String environment)
            throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0);

        server.createContext("/", (HttpExchange exchange) -> {
            int remainingLeaves =
                    LeaveCalculator.calculateLeaveBalance(20, 5);

            double encashment =
                    LeaveCalculator.calculateLeaveEncashment(
                            remainingLeaves, 1500.0);

            String response = "Employee Leave Service\n"
                    + "Environment: " + environment + "\n"
                    + "Total Leaves: 20\n"
                    + "Leaves Taken: 5\n"
                    + "Remaining Leaves: " + remainingLeaves + "\n"
                    + "Encashment: Rs. " + encashment + "\n";

            byte[] responseBytes =
                    response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type", "text/plain; charset=UTF-8");

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (var output = exchange.getResponseBody()) {
                output.write(responseBytes);
            }
        });

        return server;
    }

static HttpServer server;

    public static void main(String[] args) throws IOException {
        String environment =
                System.getenv().getOrDefault("APP_ENV", "development");

        HttpServer server = createServer(8080, environment);
        server.start();

        System.out.println("Employee Leave Service running on port 8080");
        System.out.println("Environment: " + environment);
    }
}
