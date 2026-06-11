package com.example.spotmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spotmanagement.entity.Flight;
import com.example.spotmanagement.entity.Spot;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    // 到着スポットで検索
    List<Flight> findByArrSpot(Spot arrSpot);

    // 出発スポットで検索
    List<Flight> findByDepSpot(Spot depSpot);

    // 到着スポットIDで検索
    List<Flight> findByArrSpotId(Long spotId);

    // 日付で検索
    @Query("SELECT f FROM Flight f WHERE CAST(f.arrScheduledArrivalTime AS localdate) = :date")
    List<Flight> findByDate(@Param("date") LocalDate date);
}