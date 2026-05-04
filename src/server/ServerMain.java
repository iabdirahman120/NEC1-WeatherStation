package server;

public class ServerMain {

    public static void main(String[] args) {
        // Gem vejrdata i denne fil
        WeatherStorage storage = new WeatherStorage("weatherdata.txt");

        // Start UDP alarm modtager på port 7000 i egen tråd
        Thread udpThread = new Thread(new UdpAlarmReceiver(7000));
        udpThread.setDaemon(true);
        udpThread.start();

        // Start HTTP server på port 8080 i egen tråd
        Thread httpThread = new Thread(new HttpServer(8080, storage));
        httpThread.setDaemon(true);
        httpThread.start();

        // Start TCP server på port 6000 — blokerer her og modtager klienter
        WeatherServer server = new WeatherServer(6000, storage);
        server.start();
    }
}
