package com.drones.repository;

import com.drones.entities.DroneEntity;
import com.drones.enums.DroneState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
    Optional<DroneEntity> findBySerialNumber(String serialNumber);
    List<DroneEntity> findByState(DroneState droneState);
}

