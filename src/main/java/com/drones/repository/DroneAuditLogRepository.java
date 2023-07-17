package com.drones.repository;

import com.drones.entities.DroneAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneAuditLogRepository extends JpaRepository<DroneAuditLogEntity, Long> {
}

