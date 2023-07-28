package com.drones.dtos;

import com.drones.enums.DroneModel;
import com.drones.enums.DroneState;

public class DroneDTO {

    private String serialNumber;
    private DroneModel model;
    private int weightLimit;
    private int batteryCapacity;
    private DroneState state;

    public DroneDTO() {

    }

    public DroneDTO(String serialNumber, DroneModel model, int weightLimit, int batteryCapacity,
        DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(DroneModel model) {
        this.model = model;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }
}

