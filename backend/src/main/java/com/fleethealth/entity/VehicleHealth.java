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
@Table(name = "vehicle_health")
public class VehicleHealth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehicleId;

    private Integer oilLife;

    private Integer tirePressure;

    private Integer fuelLevel;

    private Integer engineTemperature;

    private Integer batteryLevel;

    private String diagnosticTroubleCode;

    private LocalDateTime recordedAt;

    @PrePersist
    public void prePersist() {
        if (this.recordedAt == null) {
            this.recordedAt = LocalDateTime.now();
        }
    }

    // No-arg constructor
    public VehicleHealth() {
    }

    // All-arg constructor
    public VehicleHealth(Long id, Long vehicleId, Integer oilLife, Integer tirePressure,
                         Integer fuelLevel, Integer engineTemperature, Integer batteryLevel,
                         String diagnosticTroubleCode, LocalDateTime recordedAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.oilLife = oilLife;
        this.tirePressure = tirePressure;
        this.fuelLevel = fuelLevel;
        this.engineTemperature = engineTemperature;
        this.batteryLevel = batteryLevel;
        this.diagnosticTroubleCode = diagnosticTroubleCode;
        this.recordedAt = recordedAt;
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

    public Integer getOilLife() {
        return oilLife;
    }

    public void setOilLife(Integer oilLife) {
        this.oilLife = oilLife;
    }

    public Integer getTirePressure() {
        return tirePressure;
    }

    public void setTirePressure(Integer tirePressure) {
        this.tirePressure = tirePressure;
    }

    public Integer getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Integer fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Integer getEngineTemperature() {
        return engineTemperature;
    }

    public void setEngineTemperature(Integer engineTemperature) {
        this.engineTemperature = engineTemperature;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getDiagnosticTroubleCode() {
        return diagnosticTroubleCode;
    }

    public void setDiagnosticTroubleCode(String diagnosticTroubleCode) {
        this.diagnosticTroubleCode = diagnosticTroubleCode;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
