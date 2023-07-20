package com.drones.controller;

import com.drones.dtos.DroneDTO;
import com.drones.dtos.MedicationDTO;
import com.drones.exception.DroneException;
import com.drones.service.DroneService;
import com.drones.util.DroneResponse;
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
    public ResponseEntity<DroneResponse<String>> registerDrone(@RequestBody DroneDTO droneDto)
        throws DroneException {
        return droneService.registerDrone(droneDto);
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<DroneResponse<String>> loadMedications(@PathVariable String serialNumber,
        @RequestBody List<MedicationDTO> medications) throws DroneException {
        return droneService.loadMedications(serialNumber, medications);
    }

    @GetMapping("/{serialNumber}/loaded")
    public ResponseEntity<DroneResponse<List<MedicationDTO>>> getLoadedMedications(
        @PathVariable String serialNumber) {
        return droneService.getLoadedMedications(serialNumber);
    }

    @GetMapping("/available")
    public ResponseEntity<DroneResponse<List<DroneDTO>>> getAvailableDrones() {
        return droneService.getAvailableDrones();
    }

    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity<DroneResponse<Integer>> getBatteryLevel(
        @PathVariable String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }
}
