package com.drones.entities;

import com.drones.enums.DroneModel;
import com.drones.enums.DroneState;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "drones")
public class DroneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    private DroneModel model;
    private int weightLimit;
    private int batteryCapacity;
    @Enumerated(EnumType.STRING)
    private DroneState state;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MedicationEntity> loadedMedications;

    // Constructors
    public DroneEntity() {

    }

    public DroneEntity(String serialNumber, DroneModel model, int weightLimit, int batteryCapacity,
        DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<MedicationEntity> getLoadedMedications() {
        return loadedMedications;
    }

    public void setLoadedMedications(List<MedicationEntity> loadedMedications) {
        this.loadedMedications = loadedMedications;
    }
}

