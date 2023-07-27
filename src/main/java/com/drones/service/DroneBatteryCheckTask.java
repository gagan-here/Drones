package com.drones.service;

import com.drones.entities.DroneEntity;
import com.drones.enums.DroneState;
import com.drones.repository.DroneBatteryLevelAuditLogRepository;
import com.drones.repository.DroneRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DroneBatteryCheckTask implements Runnable {

    private final DroneRepository droneRepository;
    private final DroneService droneService;

    private final DroneBatteryLevelAuditLogRepository droneBatteryLevelAuditLogRepository;

    @Autowired
    public DroneBatteryCheckTask(DroneRepository droneRepository, DroneService droneService,
        DroneBatteryLevelAuditLogRepository droneBatteryLevelAuditLogRepository) {
        this.droneRepository = droneRepository;
        this.droneService = droneService;
        this.droneBatteryLevelAuditLogRepository = droneBatteryLevelAuditLogRepository;
    }

    // Periodic task to check drones battery levels and create history/audit event log
    @Override
    public void run() {
        List<DroneEntity> drones = droneRepository.findAll();
        for (DroneEntity drone : drones) {
            int batteryLevel = drone.getBatteryCapacity();
            boolean isLoaded = drone.getState() == DroneState.LOADED;
            boolean existsInBatteryLevelAuditLog = droneBatteryLevelAuditLogRepository.findByDroneSerialNumber(
                drone.getSerialNumber()).isPresent();

            if (batteryLevel < 25 && !isLoaded && !existsInBatteryLevelAuditLog) {
                droneService.createDroneBatteryLevelAuditLog(drone);
            }
        }
    }
}
