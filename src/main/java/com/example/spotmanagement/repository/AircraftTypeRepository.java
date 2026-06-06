package com.example.spotmanagement.repository;

import com.example.spotmanagement.entity.AircraftType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftTypeRepository extends JpaRepository<AircraftType, Long> {
}