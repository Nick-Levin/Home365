package com.example.home365.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {

    @Column(name = "altitude", nullable = false)
    private double altitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

}
