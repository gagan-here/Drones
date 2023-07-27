package com.drones.service;

import com.drones.dtos.DroneDTO;
import com.drones.dtos.MedicationDTO;
import com.drones.entities.DroneBatteryLevelAuditLogEntity;
import com.drones.entities.DroneEntity;
import com.drones.entities.MedicationEntity;
import com.drones.enums.DroneState;
import com.drones.exception.DroneException;
import com.drones.repository.DroneBatteryLevelAuditLogRepository;
import com.drones.repository.DroneRepository;
import com.drones.util.DroneResponse;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final DroneBatteryLevelAuditLogRepository droneBatteryLevelAuditLogRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository,
        DroneBatteryLevelAuditLogRepository droneBatteryLevelAuditLogRepository) {
        this.droneRepository = droneRepository;
        this.droneBatteryLevelAuditLogRepository = droneBatteryLevelAuditLogRepository;
    }

    public ResponseEntity<DroneResponse<String>> registerDrone(DroneDTO droneDto) {
        Optional<DroneEntity> droneEntity = droneRepository.findBySerialNumber(
            droneDto.getSerialNumber());
        if (droneEntity.isPresent()) {
            DroneResponse<String> response = new DroneResponse<>(400,
                "Drone with serial number: " + droneDto.getSerialNumber() + " already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if weight limit of drone is > 500gm
        if (droneDto.getWeightLimit() > 500) {
            DroneResponse<String> response = new DroneResponse<>(400,
                "Drone cannot have weight greater than 500gm");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Prevent the drone from being in LOADING state if the battery level is below 25%;
        if (droneDto.getState() == DroneState.LOADING && droneDto.getBatteryCapacity() < 25) {
            DroneResponse<String> response = new DroneResponse<>(400,
                "Drone cannot be in LOADING state with low battery level.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        DroneEntity drone = new DroneEntity(
            droneDto.getSerialNumber(),
            droneDto.getModel(),
            droneDto.getWeightLimit(),
            droneDto.getBatteryCapacity(),
            droneDto.getState()
        );
        droneRepository.save(drone);
        DroneResponse<String> response = new DroneResponse<>(200, "Drone registered successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<DroneResponse<String>> loadMedicationItems(String serialNumber,
        List<MedicationDTO> medications) {
        Optional<DroneEntity> droneEntity = droneRepository.findBySerialNumber(serialNumber);

        if (droneEntity.isPresent()) {
            DroneEntity drone = droneEntity.get();

            int loadedWeight = calculateLoadedWeight(medications);

            // Prevent the drone from being loaded with more weight that it can carry
            if (loadedWeight > drone.getWeightLimit()) {
                DroneResponse<String> response = new DroneResponse<>(400,
                    "Drone weight limit exceeded.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Convert list of MedicationDTO to list of MedicationEntity
            List<MedicationEntity> loadedMedications = medications.stream().map(medicationDTO -> {
                MedicationEntity medicationEntity = new MedicationEntity();
                medicationEntity.setName(medicationDTO.getName());
                medicationEntity.setWeight(medicationDTO.getWeight());
                medicationEntity.setCode(medicationDTO.getCode());
                medicationEntity.setImage(medicationDTO.getImage());

                // Set the DroneEntity in the MedicationEntity
                medicationEntity.setDrone(drone);

                return medicationEntity;
            }).collect(Collectors.toList());

            // Load medication items into drone and update the state to LOADED
            drone.setLoadedMedications(loadedMedications);
            drone.setState(DroneState.LOADED);

            // Save the drone in database
            droneRepository.save(drone);
            DroneResponse<String> response = new DroneResponse<>(200,
                "Medications loaded in drone successfully");
            return ResponseEntity.ok(response);

        } else {
            DroneResponse<String> response = new DroneResponse<>(404,
                "Drone not found with serial number: " + serialNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<DroneResponse<List<MedicationDTO>>> getLoadedMedications(
        String serialNumber) {
        Optional<DroneEntity> droneEntity = droneRepository.findBySerialNumber(serialNumber);

        if (droneEntity.isPresent()) {
            // Retrieve the loaded medications from the drone
            List<MedicationDTO> loadedMedications = droneEntity.get().getLoadedMedications()
                .stream()
                .map(
                    medicationEntity -> {
                        MedicationDTO medicationDTO = new MedicationDTO();
                        medicationDTO.setName(medicationEntity.getName());
                        medicationDTO.setWeight(medicationEntity.getWeight());
                        medicationDTO.setCode(medicationEntity.getCode());
                        medicationDTO.setImage(medicationEntity.getImage());
                        return medicationDTO;
                    }
                ).collect(Collectors.toList());
            DroneResponse<List<MedicationDTO>> response = new DroneResponse<>(200,
                "Loaded medications in drone retrieved successfully.", loadedMedications);
            return ResponseEntity.ok(response);
        } else {
            DroneResponse<List<MedicationDTO>> response = new DroneResponse<>(404,
                "Drone not found with serial number: " + serialNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<DroneResponse<List<DroneDTO>>> getAvailableDrones() {
        List<DroneEntity> availableDrones = droneRepository.findByState(DroneState.IDLE);

        // Convert the entities to DTOs
        List<DroneDTO> drones = convertToDtoList(availableDrones);
        DroneResponse<List<DroneDTO>> response = new DroneResponse<>(200,
            "Available drones retrieved successfully.", drones);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<DroneResponse<Integer>> getBatteryLevel(String serialNumber) {
        Optional<DroneEntity> droneEntity = droneRepository.findBySerialNumber(serialNumber);
        if (droneEntity.isPresent()) {
            int batteryLevel = droneEntity.get().getBatteryCapacity();
            DroneResponse<Integer> response = new DroneResponse<>(200,
                "Drone battery level retrieved successfully.", batteryLevel);
            return ResponseEntity.ok(response);
        } else {
            DroneResponse<Integer> response = new DroneResponse<>(404,
                "Drone not found with serial number: " + serialNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public void createDroneBatteryLevelAuditLog(DroneEntity droneEntity) {
        int batteryLevel = droneEntity.getBatteryCapacity();
        String droneSerialNumber = droneEntity.getSerialNumber();

        // Create battery level audit log entry
        DroneBatteryLevelAuditLogEntity auditLogEntity = new DroneBatteryLevelAuditLogEntity(
            droneSerialNumber,
            batteryLevel);
        droneBatteryLevelAuditLogRepository.save(auditLogEntity);
    }

    private DroneDTO convertToDto(DroneEntity droneEntity) {
        return new DroneDTO(
            droneEntity.getSerialNumber(),
            droneEntity.getModel(),
            droneEntity.getWeightLimit(),
            droneEntity.getBatteryCapacity(),
            droneEntity.getState()
        );
    }

    private List<DroneDTO> convertToDtoList(List<DroneEntity> droneEntities) {
        List<DroneDTO> droneDTOs = new ArrayList<>();
        for (DroneEntity droneEntity : droneEntities) {
            droneDTOs.add(convertToDto(droneEntity));
        }
        return droneDTOs;
    }

    private int calculateLoadedWeight(List<MedicationDTO> medications) {
        int loadedWeight = 0;
        for (MedicationDTO medication : medications) {
            loadedWeight += medication.getWeight();
        }
        return loadedWeight;
    }
}

