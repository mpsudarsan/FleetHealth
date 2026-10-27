package com.fleethealth.controller;

import com.fleethealth.dto.VehicleDTO;
import com.fleethealth.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // POST /api/vehicles — Create a new vehicle
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/vehicles — List all vehicles
    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // GET /api/vehicles/{id} — Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }
}
