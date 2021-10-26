package com.example.home365.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Airline")
@Table(name = "airline")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private long airlineId;

    @Column(name = "airline_name", nullable = false, unique = true)
    private String airlineName;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "home_base_location", referencedColumnName = "destination_id")
    private Destination homeBaseLocation;

//    private Destination destinations;

    @ToString.Exclude
    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aircraft> aircrafts = new ArrayList<>();

    public void addAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
        aircraft.setAirline(this);
    }

    public void removeAircraft(Aircraft aircraft) {
        aircrafts.remove(aircraft);
        aircraft.setAirline(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airline airline = (Airline) o;
        return airlineId == airline.airlineId && airlineName.equals(airline.airlineName) && balance.equals(airline.balance) && homeBaseLocation.equals(airline.homeBaseLocation) && aircrafts.equals(airline.aircrafts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airlineId, airlineName, balance, homeBaseLocation, aircrafts);
    }
}
