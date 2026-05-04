package shared;

// Delt dataklasse — bruges af både server og klient
public class WeatherData {

    private String sensorId;
    private double temperature;
    private double humidity;
    private long timestamp;

    public WeatherData(String sensorId, double temperature, double humidity) {
        this.sensorId    = sensorId;
        this.temperature = temperature;
        this.humidity    = humidity;
        this.timestamp   = System.currentTimeMillis();
    }

    // Konverter til tekst der sendes over netværk
    public String toMessage() {
        return sensorId + "," + temperature + "," + humidity + "," + timestamp;
    }

    // Opret WeatherData fra tekst modtaget over netværk
    public static WeatherData fromMessage(String message) {
        String[] parts = message.split(",");
        WeatherData wd = new WeatherData(parts[0],
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]));
        wd.timestamp = Long.parseLong(parts[3]);
        return wd;
    }

    public String getSensorId()    {
        return sensorId; }

    public double getTemperature() {
        return temperature; }

    public double getHumidity()    {
        return humidity; }

    public long getTimestamp()     {
        return timestamp; }

    @Override
    public String toString() {
        return "Sensor: " + sensorId + " | Temp: " + temperature + "°C | Fugtighed: " + humidity + "%";
    }
}
