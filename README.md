# NEC1 WeatherStation

Java client/server system der demonstrerer TCP, UDP og HTTP i ét projekt.

## System

- **Server** modtager vejrmålinger fra klienter via TCP (port 6000), sender alarmer via UDP (port 7000) og eksponerer data via HTTP (port 8080)
- **Klient** simulerer en temperatursensor og kommunikerer tovejs med serveren over TCP

## Start

Kør `ServerMain` først, derefter `ClientMain`.

HTTP-data kan ses i browser på `http://localhost:8080`.

## Teknologier

| Protokol | Port | Formål |
|----------|------|--------|
| TCP | 6000 | Vejrdata og serverkommandoer |
| UDP | 7000 | Alarmbeskeder ved høj temperatur |
| HTTP | 8080 | JSON-endpoint til browser |

Trådsikkerhed håndteres med `ReentrantReadWriteLock` i `WeatherStorage`.
