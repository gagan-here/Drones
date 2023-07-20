package com.drones.service;

import com.drones.dtos.DroneDTO;
import com.drones.dtos.MedicationDTO;
import com.drones.entities.DroneAuditLogEntity;
import com.drones.entities.DroneEntity;
import com.drones.entities.MedicationEntity;
import com.drones.enums.DroneState;
import com.drones.exception.DroneException;
import com.drones.repository.DroneAuditLogRepository;
import com.drones.repository.DroneRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final DroneAuditLogRepository auditLogRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository,
        DroneAuditLogRepository auditLogRepository) {
        this.droneRepository = droneRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public DroneDTO registerDrone(DroneDTO droneDto) throws DroneException {
        if (droneDto.getWeightLimit() > 500){
            throw new DroneException(HttpStatus.BAD_REQUEST,"Drone cannot have weight greater than 500gm");
        }
        DroneEntity droneEntity = new DroneEntity(
            droneDto.getSerialNumber(),
            droneDto.getModel(),
            droneDto.getWeightLimit(),
            droneDto.getBatteryCapacity(),
            droneDto.getState()
        );
        DroneEntity savedDrone = droneRepository.save(droneEntity);
        return convertToDto(savedDrone);
    }

    public void loadMedications(String serialNumber, List<MedicationDTO> medications) {
        DroneEntity droneEntity = droneRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new IllegalArgumentException("Drone not found"));

        // Check weight limit and battery level
        int loadedWeight = calculateLoadedWeight(medications);
        if (loadedWeight > droneEntity.getWeightLimit()) {
            throw new IllegalArgumentException(
                "Drone cannot be loaded with weight exceeding the limit");
        }
        if (droneEntity.getState() == DroneState.LOADING && droneEntity.getBatteryCapacity() < 25) {
            throw new IllegalArgumentException("Drone cannot be loaded with low battery level");
        }

        // Perform the loading operation
        List<MedicationEntity> loadedMedications = medications.stream().map(medicationDTO -> {
            MedicationEntity medicationEntity = new MedicationEntity();
            medicationEntity.setName(medicationDTO.getName());
            medicationEntity.setWeight(medicationDTO.getWeight());
            medicationEntity.setCode(medicationDTO.getCode());
            medicationEntity.setImage(medicationDTO.getImage());
            return medicationEntity;
        }).collect(Collectors.toList());

        droneEntity.setLoadedMedications(loadedMedications);
        droneEntity.setState(DroneState.LOADED);

        // Save the droneEntity in database
        droneRepository.save(droneEntity);

    }

    public List<MedicationDTO> getLoadedMedications(String serialNumber) {
        DroneEntity droneEntity = droneRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                "Drone with specified serial number doesnot exist"));

        // Retrieve the loaded medications for the drone
        return droneEntity.getLoadedMedications().stream().map(
            medicationEntity -> {
                MedicationDTO medicationDTO = new MedicationDTO();
                medicationDTO.setName(medicationEntity.getName());
                medicationDTO.setWeight(medicationEntity.getWeight());
                medicationDTO.setCode(medicationEntity.getCode());
                medicationDTO.setImage(medicationEntity.getImage());
                return medicationDTO;
            }
        ).collect(Collectors.toList());
    }

    public List<DroneDTO> getAvailableDrones() {
        List<DroneEntity> availableDrones = droneRepository.findByState(DroneState.IDLE);

        // Convert the entities to DTOs
        return convertToDtoList(availableDrones);
    }

    public int getBatteryLevel(String serialNumber) {
        DroneEntity droneEntity = droneRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                "Drone with specified serial number doesnot exist"));

        return droneEntity.getBatteryCapacity();
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void checkBatteryLevels() {
        List<DroneEntity> allDrones = droneRepository.findAll();
        for (DroneEntity droneEntity : allDrones) {
            int batteryLevel = droneEntity.getBatteryCapacity();
            String droneSerialNumber = droneEntity.getSerialNumber();

            // Create audit log entry
            DroneAuditLogEntity auditLogEntity = new DroneAuditLogEntity(droneSerialNumber,
                batteryLevel);
            auditLogRepository.save(auditLogEntity);

            // Update drone state if battery level is below 10%
            if (batteryLevel < 10) {
                droneEntity.setState(DroneState.RETURNING);
                droneRepository.save(droneEntity);
            }
        }
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
        // Convert the list of entities to DTOs
        // ...
        return null; // Placeholder
    }

    private int calculateLoadedWeight(List<MedicationDTO> medications) {
        int loadedWeight = 0;
        for (MedicationDTO medication : medications) {
            loadedWeight += medication.getWeight();
        }
        return loadedWeight;
    }
}

