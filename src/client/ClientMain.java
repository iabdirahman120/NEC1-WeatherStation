package client;

public class ClientMain {

    public static void main(String[] args) {
        // Start to klienter i hver sin tråd — implementerer Runnable
        Thread sensor1 = new Thread(new WeatherClient("localhost", 6000, "SENSOR-1"));
        Thread sensor2 = new Thread(new WeatherClient("localhost", 6000, "SENSOR-2"));

        sensor1.start();
        sensor2.start();
    }
}
