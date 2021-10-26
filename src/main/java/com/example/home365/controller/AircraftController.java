package com.example.home365.controller;

import com.example.home365.model.Aircraft;
import com.example.home365.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAll() {
        return new ResponseEntity<>(aircraftService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getOne(@PathVariable(name="id") long id) {
        return ResponseEntity.of(aircraftService.getById(id));
    }

}
