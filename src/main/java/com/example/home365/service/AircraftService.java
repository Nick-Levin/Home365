package com.example.home365.service;

import com.example.home365.model.Aircraft;
import com.example.home365.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AircraftService {

    @Autowired
    AircraftRepository aircraftRepository;

    public List<Aircraft> getAll() {
        return aircraftRepository.findAll();
    }

    public Optional<Aircraft> getById(long id) {
        return aircraftRepository.findById(id);
    }

    public void addAircraft(Aircraft aircraft) {
        aircraftRepository.save(aircraft);
    }

}
