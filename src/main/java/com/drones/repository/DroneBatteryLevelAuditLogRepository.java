package com.drones.repository;

import com.drones.entities.DroneBatteryLevelAuditLogEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneBatteryLevelAuditLogRepository extends JpaRepository<DroneBatteryLevelAuditLogEntity, Long> {
    Optional<DroneBatteryLevelAuditLogEntity> findByDroneSerialNumber(String serialNumber);
}

