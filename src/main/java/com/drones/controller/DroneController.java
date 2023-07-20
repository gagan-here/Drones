package com.drones.controller;

import com.drones.dtos.DroneDTO;
import com.drones.dtos.MedicationDTO;
import com.drones.exception.DroneException;
import com.drones.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
public class DroneController {

    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping
    public ResponseEntity<DroneDTO> registerDrone(@RequestBody DroneDTO droneDto)
        throws DroneException {
        DroneDTO registeredDrone = droneService.registerDrone(droneDto);
        return ResponseEntity.ok(registeredDrone);
    }

    @PostMapping("/{serialNumber}/load")
    public void loadMedications(@PathVariable String serialNumber,
        @RequestBody List<MedicationDTO> medications) {
        droneService.loadMedications(serialNumber, medications);
    }

    @GetMapping("/{serialNumber}/loaded")
    public List<MedicationDTO> getLoadedMedications(@PathVariable String serialNumber) {
        return droneService.getLoadedMedications(serialNumber);
    }

    @GetMapping("/available")
    public List<DroneDTO> getAvailableDrones() {
        return droneService.getAvailableDrones();
    }

    @GetMapping("/{serialNumber}/battery")
    public int getBatteryLevel(@PathVariable String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }
}
