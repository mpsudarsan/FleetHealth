package com.fleethealth.dto;

import java.time.LocalDateTime;

public class MaintenanceAlertDTO {

    private Long id;
    private Long vehicleId;
    private String vehicleIdentifier;  // e.g., "VH-102"
    private String severity;
    private String reason;
    private String status;
    private LocalDateTime createdAt;

    // No-arg constructor
    public MaintenanceAlertDTO() {
    }

    // All-arg constructor
    public MaintenanceAlertDTO(Long id, Long vehicleId, String vehicleIdentifier,
                               String severity, String reason, String status,
                               LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehicleIdentifier = vehicleIdentifier;
        this.severity = severity;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleIdentifier() {
        return vehicleIdentifier;
    }

    public void setVehicleIdentifier(String vehicleIdentifier) {
        this.vehicleIdentifier = vehicleIdentifier;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
