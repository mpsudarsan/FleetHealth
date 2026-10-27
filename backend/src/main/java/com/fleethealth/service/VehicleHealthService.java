package com.fleethealth.service;

import com.fleethealth.dto.VehicleHealthDTO;
import com.fleethealth.entity.VehicleHealth;
import com.fleethealth.exception.ResourceNotFoundException;
import com.fleethealth.repository.VehicleHealthRepository;
import com.fleethealth.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleHealthService {

    private final VehicleHealthRepository healthRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceAlertService alertService;

    // Constructor injection
    public VehicleHealthService(VehicleHealthRepository healthRepository,
                                VehicleRepository vehicleRepository,
                                MaintenanceAlertService alertService) {
        this.healthRepository = healthRepository;
        this.vehicleRepository = vehicleRepository;
        this.alertService = alertService;
    }

    // Record new health data for a vehicle
    public VehicleHealthDTO recordHealth(Long vehicleId, VehicleHealthDTO dto) {
        // Verify vehicle exists
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        VehicleHealth health = toEntity(dto);
        health.setVehicleId(vehicleId);
        VehicleHealth saved = healthRepository.save(health);

        // Run the maintenance rule engine
        alertService.evaluateHealth(vehicleId, saved);

        return toDTO(saved);
    }

    // Get all health records for a vehicle
    public List<VehicleHealthDTO> getHealthByVehicleId(Long vehicleId) {
        // Verify vehicle exists
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        return healthRepository.findByVehicleIdOrderByRecordedAtDesc(vehicleId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convert Entity to DTO
    private VehicleHealthDTO toDTO(VehicleHealth health) {
        VehicleHealthDTO dto = new VehicleHealthDTO();
        dto.setId(health.getId());
        dto.setVehicleId(health.getVehicleId());
        dto.setOilLife(health.getOilLife());
        dto.setTirePressure(health.getTirePressure());
        dto.setFuelLevel(health.getFuelLevel());
        dto.setEngineTemperature(health.getEngineTemperature());
        dto.setBatteryLevel(health.getBatteryLevel());
        dto.setDiagnosticTroubleCode(health.getDiagnosticTroubleCode());
        dto.setRecordedAt(health.getRecordedAt());
        return dto;
    }

    // Convert DTO to Entity
    private VehicleHealth toEntity(VehicleHealthDTO dto) {
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(dto.getOilLife());
        health.setTirePressure(dto.getTirePressure());
        health.setFuelLevel(dto.getFuelLevel());
        health.setEngineTemperature(dto.getEngineTemperature());
        health.setBatteryLevel(dto.getBatteryLevel());
        health.setDiagnosticTroubleCode(dto.getDiagnosticTroubleCode());
        return health;
    }
}
