package com.example.home365.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Aircraft")
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aircraft_id")
    private long aircraftId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "use_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date useStartDate;

    @Column(name = "max_distance", nullable = false)
    private double maxDistance;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Airline airline;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return aircraftId == aircraft.aircraftId && Double.compare(aircraft.maxDistance, maxDistance) == 0 && price.equals(aircraft.price) && useStartDate.equals(aircraft.useStartDate) && airline.equals(aircraft.airline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftId, price, useStartDate, maxDistance, airline);
    }
}
