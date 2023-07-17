package com.drones.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drone_audit_log")
public class DroneAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String droneSerialNumber;

    private int batteryLevel;

    private LocalDateTime timestamp;

    public DroneAuditLogEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public DroneAuditLogEntity(String droneSerialNumber, int batteryLevel) {
        this.droneSerialNumber = droneSerialNumber;
        this.batteryLevel = batteryLevel;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDroneSerialNumber() {
        return droneSerialNumber;
    }

    public void setDroneSerialNumber(String droneSerialNumber) {
        this.droneSerialNumber = droneSerialNumber;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

