package com.example.spotmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spotmanagement.entity.AircraftType;
import com.example.spotmanagement.entity.Flight;
import com.example.spotmanagement.entity.Spot;
import com.example.spotmanagement.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    // ① 型式サイズチェック（警告：便バーを赤表示）
    public boolean isSizeOver(Spot spot, AircraftType aircraftType) {
        // ウィングスパンの数値部分を比較（例：W44 → 44）
        int maxWingspan = extractWingspanValue(spot.getMaxWingspan());
        int aircraftWingspan = extractWingspanValue(aircraftType.getWingspan());
        return aircraftWingspan > maxWingspan;
    }

    // ② 時間重複チェック（エラー：登録不可）
    public boolean isOverlap(Long spotId, LocalDateTime arrivalTime, LocalDateTime departureTime, Long excludeFlightId) {
        List<Flight> existingFlights = flightRepository.findByArrSpotId(spotId);
        for (Flight existing : existingFlights) {
            if (excludeFlightId != null && existing.getId().equals(excludeFlightId)) continue;
            if (arrivalTime.isBefore(existing.getDepScheduledDepartureTime()) &&
                departureTime.isAfter(existing.getArrScheduledArrivalTime())) {
                return true;
            }
        }
        return false;
    }

    // ③ 30分インターバルチェック（警告：便バーを黄色表示）
    public boolean isIntervalShort(Long spotId, LocalDateTime arrivalTime, LocalDateTime departureTime, Long excludeFlightId) {
        List<Flight> existingFlights = flightRepository.findByArrSpotId(spotId);
        for (Flight existing : existingFlights) {
            if (excludeFlightId != null && existing.getId().equals(excludeFlightId)) continue;

            long minutesAfterPrev = java.time.Duration.between(
                existing.getDepScheduledDepartureTime(), arrivalTime).toMinutes();
            System.out.println("minutesAfterPrev: " + minutesAfterPrev);

            if (minutesAfterPrev >= 0 && minutesAfterPrev < 30) {
                return true;
            }

            long minutesBeforeNext = java.time.Duration.between(
                departureTime, existing.getArrScheduledArrivalTime()).toMinutes();
            System.out.println("minutesBeforeNext: " + minutesBeforeNext);

            if (minutesBeforeNext >= 0 && minutesBeforeNext < 30) {
                return true;
            }
        }
        return false;
    }

    private int extractWingspanValue(String wingspan) {
        // "W44" → 44
        return Integer.parseInt(wingspan.replaceAll("[^0-9]", ""));
    }
}