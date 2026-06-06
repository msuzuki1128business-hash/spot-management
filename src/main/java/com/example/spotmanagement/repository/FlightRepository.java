package com.example.spotmanagement.repository;

import com.example.spotmanagement.entity.Flight;
import com.example.spotmanagement.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    // 到着スポットで検索
    List<Flight> findByArrSpot(Spot arrSpot);

    // 出発スポットで検索
    List<Flight> findByDepSpot(Spot depSpot);

    // 到着スポットIDで検索
    List<Flight> findByArrSpotId(Long spotId);
}