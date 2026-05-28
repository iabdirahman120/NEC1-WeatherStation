package server;

import shared.WeatherData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

// Simpelt HTTP endpoint — kan åbnes i browser på localhost:8080
public class HttpServer implements Runnable {

    private final int port;
    private final WeatherStorage storage;

    public HttpServer(int port, WeatherStorage storage) {
        this.port    = port;
        this.storage = storage;
    }

    @Override
    public void run() {
        System.out.println("HTTP server starter på port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                // Håndter HTTP request i ny tråd
                new Thread(() -> handleRequest(client)).start();
            }
        } catch (Exception e) {
            System.out.println("HTTP fejl: " + e.getMessage());
        }
    }

    private void handleRequest(Socket client) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true)
        ) {
            // Læs og kassér HTTP request-linjer (header slutter ved blank linje)
            while (!reader.readLine().isEmpty()) { }

            // Byg JSON svar med alle målinger
            StringBuilder json = new StringBuilder("[");
            List<WeatherData> all = storage.getAll();
            for (int i = 0; i < all.size(); i++) {
                WeatherData d = all.get(i);
                json.append("{")
                        .append("\"sensor\":\"").append(d.getSensorId()).append("\",")
                        .append("\"temperature\":").append(d.getTemperature()).append(",")
                        .append("\"humidity\":").append(d.getHumidity()).append(",")
                        .append("\"timestamp\":").append(d.getTimestamp())
                        .append("}");
                if (i < all.size() - 1) json.append(",");
            }
            json.append("]");

            // Send HTTP svar
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: application/json");
            writer.println("Connection: close");
            writer.println();
            writer.println(json);

        } catch (Exception e) {
            System.out.println("HTTP request fejl: " + e.getMessage());
        } finally {
            try { client.close(); } catch (Exception ignored) {}
        }
    }
}
