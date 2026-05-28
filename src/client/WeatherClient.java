package client;

import shared.WeatherData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class WeatherClient implements Runnable {

    private final String host;
    private final int port;
    private final String sensorId;
    private final Random random = new Random();
    private int interval = 1000;

    public WeatherClient(String host, int port, String sensorId) {
        this.host     = host;
        this.port     = port;
        this.sensorId = sensorId;
    }

    @Override
    public void run() {
        System.out.println(sensorId + " starter — forbinder til " + host + ":" + port);

        try (
                Socket socket = new Socket(host, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            Thread listenerThread = new Thread(() -> listenForCommands(reader));
            listenerThread.setDaemon(true);
            listenerThread.start();

            while (true) {
                double temperature = 30 + random.nextDouble() * 15;
                double humidity    = 40 + random.nextDouble() * 40;
                WeatherData data   = new WeatherData(sensorId, temperature, humidity);
                writer.println(data.toMessage());
                System.out.println(sensorId + " sendt: " + data);
                Thread.sleep(interval);
            }

        } catch (Exception e) {
            System.out.println(sensorId + " fejl: " + e.getMessage());
        }
    }

    private void listenForCommands(BufferedReader reader) {
        try {
            String command;
            while ((command = reader.readLine()) != null) {
                System.out.println(sensorId + " modtog kommando: " + command);
                if (command.startsWith("SET_INTERVAL:")) {
                    interval = Integer.parseInt(command.split(":")[1]) * 1000;
                    System.out.println(sensorId + " skifter interval til " + interval + "ms");
                } else if (command.startsWith("ALARM:")) {
                    System.out.println("*** " + sensorId + " ALARM: " + command + " ***");
                } else if (command.startsWith("STATUS:")) {
                    System.out.println(sensorId + " status: kører normalt");
                } else if (command.startsWith("CONNECTED:")) {
                    System.out.println(sensorId + " forbundet til server!");
                }
            }
        } catch (Exception e) {
            System.out.println(sensorId + " listener fejl: " + e.getMessage());
        }
    }
}
