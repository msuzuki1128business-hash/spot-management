package com.example.spotmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "spots")
@Data
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String spotNumber;

    @Column(nullable = false)
    private String maxWingspan;

    @Column(nullable = false)
    private java.time.LocalDateTime createdAt;
}