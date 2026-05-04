package client;

public class ClientMain {

    public static void main(String[] args) {
        // Start to klienter i hver sin tråd — simulerer 2 sensorer
        Thread sensor1 = new Thread(() -> {
            WeatherClient client = new WeatherClient("localhost", 6000, "SENSOR-1");
            client.start();
        });

        Thread sensor2 = new Thread(() -> {
            WeatherClient client = new WeatherClient("localhost", 6000, "SENSOR-2");
            client.start();
        });

        sensor1.start();
        sensor2.start();
    }
}
