package com.fleethealth.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @NotBlank(message = "VIN is required")
    private String vin;

    private String manufacturer;

    private String model;

    private Integer mileage;

    // No-arg constructor
    public VehicleDTO() {
    }

    // All-arg constructor
    public VehicleDTO(Long id, String vehicleId, String vin, String manufacturer,
                      String model, Integer mileage) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vin = vin;
        this.manufacturer = manufacturer;
        this.model = model;
        this.mileage = mileage;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
}
