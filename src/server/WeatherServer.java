package server;

import java.net.ServerSocket;
import java.net.Socket;

// Accepterer klienter og starter en ny tråd for hver
public class WeatherServer {

    private final int port;
    private final WeatherStorage storage;

    public WeatherServer(int port, WeatherStorage storage) {
        this.port    = port;
        this.storage = storage;
    }

    public void start() {
        System.out.println("Server starter på port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            // Kør i løkke — accepter klienter for evigt
            while (true) {
                // Vent på næste klient — blokerer indtil en klient forbinder
                Socket clientSocket = serverSocket.accept();

                // Start ny tråd for denne klient
                Thread thread = new Thread(new ClientHandler(clientSocket, storage));
                thread.start();

                System.out.println("Ny tråd startet for klient.");
            }

        } catch (Exception e) {
            System.out.println("Server fejl: " + e.getMessage());
        }
    }
}
