package com.example.spotmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 到着便情報
    @ManyToOne
    @JoinColumn(name = "arr_spot_id", nullable = false)
    private Spot arrSpot;

    @ManyToOne
    @JoinColumn(name = "arr_aircraft_type_id", nullable = false)
    private AircraftType arrAircraftType;

    @Column(nullable = false)
    private String arrFlightNumber;

    @Column(nullable = false)
    private String arrFromAirport;

    @Column(nullable = false)
    private LocalDateTime arrScheduledDepartureTime;  // 出発予定時刻

    @Column(nullable = false)
    private String arrToAirport;

    @Column(nullable = false)
    private LocalDateTime arrScheduledArrivalTime;    // 到着予定時刻

    private LocalDateTime arrActualDepartureTime;     // 出発実績時刻（任意）
    private LocalDateTime arrActualArrivalTime;       // 到着実績時刻（任意）

    // 出発便情報
    @ManyToOne
    @JoinColumn(name = "dep_spot_id", nullable = false)
    private Spot depSpot;

    @ManyToOne
    @JoinColumn(name = "dep_aircraft_type_id", nullable = false)
    private AircraftType depAircraftType;

    @Column(nullable = false)
    private String depFlightNumber;

    @Column(nullable = false)
    private String depFromAirport;

    @Column(nullable = false)
    private LocalDateTime depScheduledDepartureTime;  // 出発予定時刻

    @Column(nullable = false)
    private String depToAirport;

    @Column(nullable = false)
    private LocalDateTime depScheduledArrivalTime;    // 到着予定時刻

    private LocalDateTime depActualDepartureTime;     // 出発実績時刻（任意）
    private LocalDateTime depActualArrivalTime;       // 到着実績時刻（任意）
    
    @Column(nullable = false)
    private boolean sizeWarning = false;

    @Column(nullable = false)
    private boolean intervalWarning = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}