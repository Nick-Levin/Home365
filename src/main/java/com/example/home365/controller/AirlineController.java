package com.example.home365.controller;

import com.example.home365.exception.AircraftNotFoundException;
import com.example.home365.exception.AirlineNotFoundException;
import com.example.home365.model.Aircraft;
import com.example.home365.model.Airline;
import com.example.home365.model.Destination;
import com.example.home365.model.Location;
import com.example.home365.service.AircraftService;
import com.example.home365.service.AirlineService;
import com.example.home365.service.DestinationService;
import com.example.home365.util.HaversineFormula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public ResponseEntity<List<Airline>> getAirlines() {
        return new ResponseEntity<>(airlineService.getAllAirlines(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable(name = "id") long id) {
        return ResponseEntity.of(airlineService.getById(id));
    }

    @GetMapping("/{id}/distance")
    public ResponseEntity<Map<String, Double>> calculateAllDistances(@PathVariable long id) {
        Optional<Airline> optional = airlineService.getById(id);

        if(optional.isPresent()) {
            Airline airline = optional.get();
            return ResponseEntity.ok(airlineService.createDistancesMap(airline));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/available-destinations")
    public ResponseEntity<Map<String, Double>> getAvailableDestinations(@PathVariable(name = "id") long id) {
        Optional<Airline> optional = airlineService.getById(id);

        if(optional.isPresent()) {
            Airline airline = optional.get();
            Map<String, Double> map = airlineService.createDistancesMap(airline);
            double maxDistance = airlineService.getMaxDistance(airline);
            Map<String, Double> mapResult = new HashMap<>();
            map.keySet().forEach(key -> {
                if(map.get(key) <= maxDistance) mapResult.put(key, map.get(key));
            });

            return ResponseEntity.ok(mapResult);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity addAirline(@RequestBody Airline airline) {
        airlineService.addOrUpdateAirline(airline);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{airlineId}")
    @Transactional
    public ResponseEntity addAircraftToAirline(
            @PathVariable long airlineId,
            @RequestBody Aircraft aircraft
    ) {
        Optional<Airline> optional = airlineService.getById(airlineId);
        if(optional.isPresent()) {
            Airline airline = optional.get();
            if(airline.getBalance().compareTo(aircraft.getPrice()) > 0) {
                airline.setBalance(airline.getBalance().subtract(aircraft.getPrice()));
                airline.addAircraft(aircraft);
                aircraft.setAirline(airline);
                airlineService.addOrUpdateAirline(airline);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("insufficient balance");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{airlineId}/{aircraftId}")
    @Transactional
    public ResponseEntity sellAircraft(
            @PathVariable long airlineId,
            @PathVariable long aircraftId
    ) {
        try {
            airlineService.sellAircraft(airlineId, aircraftId);
            return ResponseEntity.ok().build();
        } catch (AircraftNotFoundException | AirlineNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sellerId}/{aircraftId}/{buyerId}")
    @Transactional
    public ResponseEntity sellAircraftToAirline(
            @PathVariable long sellerId,
            @PathVariable long aircraftId,
            @PathVariable long buyerId
    ) {
        Optional<Airline> optionalSeller = airlineService.getById(sellerId);
        Optional<Airline> optionalBuyer = airlineService.getById(buyerId);

        if(optionalSeller.isPresent() && optionalBuyer.isPresent()) {
            try {
                Airline airline = optionalSeller.get();
                Airline buyer = optionalBuyer.get();
                Aircraft aircraft = airline.getAircrafts()
                        .stream()
                        .filter(aircrft -> aircrft.getAircraftId() == aircraftId)
                        .findFirst()
                        .orElseThrow(AirlineNotFoundException::new);

                airlineService.sellAircraft(sellerId, aircraftId);
                buyer.addAircraft(aircraft);
                buyer.setBalance(buyer.getBalance().subtract(airlineService.calculateAircraftPrice(aircraft.getPrice(), aircraft.getUseStartDate())));
                airlineService.addOrUpdateAirline(buyer);

                return ResponseEntity.ok().build();
            } catch (AircraftNotFoundException | AirlineNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

}
