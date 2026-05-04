package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Sender UDP alarm når temperaturen er ekstrem
public class UdpAlarmSender {

    private final String host;
    private final int port;

    public UdpAlarmSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendAlarm(String message) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(host);

            // Pak beskeden ind i en UDP pakke og send
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

            System.out.println("UDP ALARM sendt: " + message);
        } catch (Exception e) {
            System.out.println("UDP fejl: " + e.getMessage());
        }
    }
}
