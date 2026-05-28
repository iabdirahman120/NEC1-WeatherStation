package server;

import shared.WeatherData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final WeatherStorage storage;
    private final UdpAlarmSender alarmSender;
    private PrintWriter writer; // bruges til at sende kommandoer til klient

    private static final double MAX_TEMP = 35.0;

    public ClientHandler(Socket clientSocket, WeatherStorage storage) {
        this.clientSocket = clientSocket;
        this.storage      = storage;
        this.alarmSender  = new UdpAlarmSender("localhost", 7000);
    }

    // Server sender kommando til denne klient
    public void sendCommand(String command) {
        if (writer != null) {
            writer.println(command);
            System.out.println("Kommando sendt til klient: " + command);
        }
    }

    @Override
    public void run() {
        System.out.println("Klient forbundet: " + clientSocket.getInetAddress());

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            this.writer = pw;

            // Send velkomst-kommando til klient ved forbindelse
            writer.println("CONNECTED:Server klar");

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Modtaget: " + message);

                WeatherData data = WeatherData.fromMessage(message);
                storage.save(data);
                System.out.println("Gemt: " + data);

                // Send alarm til klient hvis temperatur er for høj
                if (data.getTemperature() > MAX_TEMP) {
                    writer.println("ALARM:Høj temperatur " + data.getTemperature());
                    alarmSender.sendAlarm("ALARM! Høj temperatur: "
                            + data.getTemperature() + "°C fra " + data.getSensorId());
                }

                // Server beder om status hver 10. måling
                if (storage.getAll().size() % 10 == 0) {
                    writer.println("STATUS:Send systemstatus");
                }
            }

        } catch (Exception e) {
            System.out.println("Klient afbrudt: " + e.getMessage());
        } finally {
            try { clientSocket.close(); } catch (Exception e) {}
            System.out.println("Klient forbindelse lukket.");
        }
    }
}
