package com.fleethealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_alert")
public class MaintenanceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = "OPEN";
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // No-arg constructor
    public MaintenanceAlert() {
    }

    // All-arg constructor
    public MaintenanceAlert(Long id, Long vehicleId, String severity, String reason,
                            String status, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
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
