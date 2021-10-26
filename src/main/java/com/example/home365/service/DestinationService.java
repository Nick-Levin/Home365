package com.example.home365.service;

import com.example.home365.model.Destination;
import com.example.home365.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    @Autowired
    DestinationRepository destinationRepository;

    public Optional<Destination> getDestination(long id) {
        return destinationRepository.findById(id);
    }

    public List<Destination> getAll() {
        return destinationRepository.findAll();
    }

    public void addDestination(Destination destination) {
        destinationRepository.save(destination);
    }

}
