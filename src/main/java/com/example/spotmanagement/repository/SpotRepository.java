package com.example.spotmanagement.repository;

import com.example.spotmanagement.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}