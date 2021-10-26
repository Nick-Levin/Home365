package com.example.home365.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "destination")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private long id;

    @Column(name = "destination_name", nullable = false)
    private String name;

    @Embedded
    private Location location;

    @OneToOne(mappedBy = "homeBaseLocation")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Airline airline;

}
