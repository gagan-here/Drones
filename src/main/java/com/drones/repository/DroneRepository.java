package com.drones.repository;

import com.drones.entities.DroneEntity;
import com.drones.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<DroneEntity, String> {
    List<DroneEntity> findByState(DroneState state);
}

