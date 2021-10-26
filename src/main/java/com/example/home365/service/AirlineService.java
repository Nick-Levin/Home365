package com.example.home365.service;

import com.example.home365.exception.AircraftNotFoundException;
import com.example.home365.exception.AirlineNotFoundException;
import com.example.home365.model.Aircraft;
import com.example.home365.model.Airline;
import com.example.home365.model.Destination;
import com.example.home365.model.Location;
import com.example.home365.repository.AirlineRepository;
import com.example.home365.repository.DestinationRepository;
import com.example.home365.util.HaversineFormula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    public void addOrUpdateAirline(Airline airline) {
        airlineRepository.save(airline);
    }

    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    public Optional<Airline> getById(long id) {
        return airlineRepository.findById(id);
    }

    @Transactional
    public void sellAircraft(long airlineId, long aircraftId) throws AircraftNotFoundException, AirlineNotFoundException {
        Airline airline = getById(airlineId).orElseThrow(AirlineNotFoundException::new);
        Aircraft aircraft = airline.getAircrafts()
                .stream()
                .filter(aircraftIter -> aircraftIter.getAircraftId() == aircraftId)
                .findFirst()
                .orElseThrow(AircraftNotFoundException::new);
        airline.setBalance(airline.getBalance().add(calculateAircraftPrice(aircraft.getPrice(), aircraft.getUseStartDate())));
        airline.removeAircraft(aircraft);
        aircraft.setAirline(null);
        airlineRepository.save(airline);
    }

    public Map<String, Double> createDistancesMap(Airline airline) {
        List<Destination> list = destinationRepository.findAll();
        Map<String, Double> map = new HashMap<>();
        list.forEach(dest -> {
            Location startLocation = airline.getHomeBaseLocation().getLocation();
            Location endLocation = dest.getLocation();
            double distance = HaversineFormula.distance(
                    startLocation.getAltitude(), startLocation.getLongitude(),
                    endLocation.getAltitude(), endLocation.getLongitude()
            );
            map.put(dest.getName(), distance);
        });

        return map;
    }

    public double getMaxDistance(Airline airline) {
        return airline.getAircrafts()
                .stream()
                .mapToDouble(Aircraft::getMaxDistance)
                .max()
                .orElseThrow();
    }

    public BigDecimal calculateAircraftPrice(BigDecimal originalPrice, Date useStartDate) {
        final double usageFactor = 0.02;
        final long num = 1;
        Period period = Period.between(
                useStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        long monthsBetween = period.getMonths() + period.getYears() * 12L;
        return originalPrice.multiply(BigDecimal.valueOf(num - (monthsBetween*usageFactor)));
    }

}
