package com.example.spotmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aircraft_types")
@Data
public class AircraftType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String typeName;

    @Column(nullable = false)
    private String wingspan;

    @Column(nullable = false)
    private java.time.LocalDateTime createdAt;
}