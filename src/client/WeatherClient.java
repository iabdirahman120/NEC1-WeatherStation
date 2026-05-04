package client;

import shared.WeatherData;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

// Klient der sender vejrdata til serveren automatisk
public class WeatherClient {

    private final String host;
    private final int port;
    private final String sensorId;
    private final Random random = new Random();

    public WeatherClient(String host, int port, String sensorId) {
        this.host     = host;
        this.port     = port;
        this.sensorId = sensorId;
    }

    public void start() {
        System.out.println("Klient starter — forbinder til " + host + ":" + port);

        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            // Send vejrdata hvert sekund
            while (true) {
                // Simuler tilfældig temperatur og luftfugtighed
                double temperature = 15 + random.nextDouble() * 20;
                double humidity    = 40 + random.nextDouble() * 40;

                WeatherData data = new WeatherData(sensorId, temperature, humidity);

                // Send som tekst over netværk
                writer.println(data.toMessage());
                System.out.println("Sendt: " + data);

                // Vent 1 sekund før næste måling
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println("Klient fejl: " + e.getMessage());
        }
    }
}
