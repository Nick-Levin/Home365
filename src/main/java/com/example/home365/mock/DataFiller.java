package com.example.home365.mock;

import com.example.home365.model.Aircraft;
import com.example.home365.model.Airline;
import com.example.home365.model.Destination;
import com.example.home365.model.Location;
import com.example.home365.service.AircraftService;
import com.example.home365.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class DataFiller implements CommandLineRunner {

    private static final Random random = new Random();

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private AircraftService aircraftService;

    @Override
    public void run(String... args) throws Exception {
        initAirlines();
        initAircraft();
    }

    public void initAirlines() {
        IntStream.range(0, 10).forEach(id -> airlineService.addOrUpdateAirline(generateRandomAirline()));
    }

    public void initAircraft() {
        IntStream.range(0, 100).forEach(aircraft_id -> {
            Airline airline = airlineService.getById(random.nextInt(10) + 1).orElseThrow();
            Aircraft aircraft = generateRandomAircraft();
            airline.addAircraft(aircraft);
            airlineService.addOrUpdateAirline(airline);
        });
    }

    private Airline generateRandomAirline() {
        Airline airline = new Airline();
        airline.setBalance(BigDecimal.valueOf(5_000_000));
        airline.setAirlineName(randomString(5, 12));
        airline.setHomeBaseLocation(generateRandomDestination());
        return airline;
    }

    private Destination generateRandomDestination() {
        Destination destination = new Destination();
        destination.setName(randomString(6, 10));
        destination.setLocation(
                new Location(
                        random.nextInt(100) + random.nextDouble(),
                        random.nextInt(100) + random.nextDouble()
                )
        );
        return destination;
    }

    private Aircraft generateRandomAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setPrice(BigDecimal.valueOf(5_000));
        aircraft.setMaxDistance(random.nextInt(2_000) + 200);
        aircraft.setUseStartDate(generateRandomDate());
        return aircraft;
    }

    private Date generateRandomDate() {
        return Date.from(
                LocalDate.of(
                        random.nextInt(11) + 2010,
                        random.nextInt(11) + 1,
                        random.nextInt(28) + 1
                ).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    private String randomString(int minLength, int maxLength) {
        final int leftLimit = 97; //a
        final int rightLimit = 122; //z
        final int length = random.nextInt(maxLength - minLength + 1) + minLength;
        return random.ints(leftLimit, rightLimit)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
