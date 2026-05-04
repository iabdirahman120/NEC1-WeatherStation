package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

// Lytter på UDP port 7000 og printer alarmer
public class UdpAlarmReceiver implements Runnable {

    private final int port;

    public UdpAlarmReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("UDP alarm modtager starter på port " + port);

        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];

            while (true) {
                // Vent på næste UDP pakke
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Konverter bytes til tekst
                String alarm = new String(packet.getData(), 0, packet.getLength());
                System.out.println("*** ALARM MODTAGET: " + alarm + " ***");
            }

        } catch (Exception e) {
            System.out.println("UDP modtager fejl: " + e.getMessage());
        }
    }
}
