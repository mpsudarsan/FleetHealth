package com.fleethealth.controller;

import com.fleethealth.dto.VehicleHealthDTO;
import com.fleethealth.service.VehicleHealthService;
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
public class VehicleHealthController {

    private final VehicleHealthService healthService;

    public VehicleHealthController(VehicleHealthService healthService) {
        this.healthService = healthService;
    }

    // POST /api/vehicles/{id}/health — Record health data
    @PostMapping("/{id}/health")
    public ResponseEntity<VehicleHealthDTO> recordHealth(@PathVariable Long id,
                                                         @RequestBody VehicleHealthDTO healthDTO) {
        VehicleHealthDTO saved = healthService.recordHealth(id, healthDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/vehicles/{id}/health — Get health records
    @GetMapping("/{id}/health")
    public ResponseEntity<List<VehicleHealthDTO>> getHealthByVehicleId(@PathVariable Long id) {
        return ResponseEntity.ok(healthService.getHealthByVehicleId(id));
    }
}
