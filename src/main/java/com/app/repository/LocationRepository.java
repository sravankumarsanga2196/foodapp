package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Location;

public interface LocationRepository extends JpaRepository<Location, String> {

    List<Location> findByCityIgnoreCase(String city);

    List<Location> findByAreaIgnoreCase(String area);

    List<Location> findByDeliveryRadiusGreaterThan(Double radius);
}