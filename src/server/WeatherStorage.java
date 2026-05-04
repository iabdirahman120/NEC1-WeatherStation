package server;

import shared.WeatherData;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WeatherStorage {

    // ReadWriteLock — flere kan læse samtidig, kun én kan skrive
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Liste der holder alle vejrmålinger i hukommelsen
    private final List<WeatherData> dataList = new ArrayList<>();

    // Fil der gemmer data permanent
    private final String filePath;

    public WeatherStorage(String filePath) {
        this.filePath = filePath;
    }

    // Gem ny måling — kun én tråd kan skrive ad gangen
    public void save(WeatherData data) {
        lock.writeLock().lock();
        try {
            dataList.add(data);
            // Skriv til fil
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
                pw.println(data.toMessage());
            } catch (Exception e) {
                System.out.println("Fejl ved filskrivning: " + e.getMessage());
            }
        } finally {
            // Frigiv lås — altid i finally så den frigives selv ved fejl
            lock.writeLock().unlock();
        }
    }

    // Hent alle målinger — flere tråde kan læse samtidig
    public List<WeatherData> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(dataList);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Hent seneste måling
    public WeatherData getLatest() {
        lock.readLock().lock();
        try {
            if (dataList.isEmpty()) return null;
            return dataList.get(dataList.size() - 1);
        } finally {
            lock.readLock().unlock();
        }
    }
}
