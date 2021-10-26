package com.example.home365.controller;

import com.example.home365.model.Destination;
import com.example.home365.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAll());
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable long destinationId) {
        return ResponseEntity.of(destinationService.getDestination(destinationId));
    }

    @PutMapping
    public ResponseEntity addDestination(@RequestBody Destination destination) {
        destinationService.addDestination(destination);
        return ResponseEntity.ok().build();
    }

}
