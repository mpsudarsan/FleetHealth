package com.fleethealth.service;

import com.fleethealth.dto.MaintenanceAlertDTO;
import com.fleethealth.entity.MaintenanceAlert;
import com.fleethealth.entity.Vehicle;
import com.fleethealth.entity.VehicleHealth;
import com.fleethealth.exception.ResourceNotFoundException;
import com.fleethealth.repository.MaintenanceAlertRepository;
import com.fleethealth.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceAlertService {

    private final MaintenanceAlertRepository alertRepository;
    private final VehicleRepository vehicleRepository;

    // Constructor injection
    public MaintenanceAlertService(MaintenanceAlertRepository alertRepository,
                                   VehicleRepository vehicleRepository) {
        this.alertRepository = alertRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * MAINTENANCE RULE ENGINE
     * Evaluates vehicle health data and creates alerts based on thresholds.
     */
    public void evaluateHealth(Long vehicleId, VehicleHealth health) {

        // Rule 1: Oil life below 10%
        if (health.getOilLife() != null && health.getOilLife() < 10) {
            createAlert(vehicleId, "HIGH", "Oil life below 10%");
        }

        // Rule 2: Tire pressure below 30 PSI
        if (health.getTirePressure() != null && health.getTirePressure() < 30) {
            createAlert(vehicleId, "MEDIUM", "Tire pressure below 30 PSI");
        }

        // Rule 3: Engine temperature above 100°C
        if (health.getEngineTemperature() != null && health.getEngineTemperature() > 100) {
            createAlert(vehicleId, "HIGH", "Engine temperature is high");
        }

        // Rule 4: Diagnostic trouble code exists
        if (health.getDiagnosticTroubleCode() != null
                && !health.getDiagnosticTroubleCode().isBlank()) {
            createAlert(vehicleId, "HIGH", "Diagnostic trouble code detected");
        }
    }

    // Create and save an alert
    private void createAlert(Long vehicleId, String severity, String reason) {
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setVehicleId(vehicleId);
        alert.setSeverity(severity);
        alert.setReason(reason);
        alert.setStatus("OPEN");
        alertRepository.save(alert);
    }

    // Get all alerts with vehicle identifier
    public List<MaintenanceAlertDTO> getAllAlerts() {
        return alertRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Resolve an alert by ID
    public MaintenanceAlertDTO resolveAlert(Long alertId) {
        MaintenanceAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));
        alert.setStatus("RESOLVED");
        MaintenanceAlert saved = alertRepository.save(alert);
        return toDTO(saved);
    }

    // Convert Entity to DTO (includes vehicleIdentifier lookup)
    private MaintenanceAlertDTO toDTO(MaintenanceAlert alert) {
        MaintenanceAlertDTO dto = new MaintenanceAlertDTO();
        dto.setId(alert.getId());
        dto.setVehicleId(alert.getVehicleId());
        dto.setSeverity(alert.getSeverity());
        dto.setReason(alert.getReason());
        dto.setStatus(alert.getStatus());
        dto.setCreatedAt(alert.getCreatedAt());

        // Look up the human-readable vehicleId string (e.g., "VH-102")
        vehicleRepository.findById(alert.getVehicleId())
                .ifPresent(vehicle -> dto.setVehicleIdentifier(vehicle.getVehicleId()));

        return dto;
    }
}
