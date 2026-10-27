package com.fleethealth.service;

import com.fleethealth.dto.VehicleDTO;
import com.fleethealth.entity.Vehicle;
import com.fleethealth.exception.ResourceNotFoundException;
import com.fleethealth.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    // Constructor injection
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Create a new vehicle
    public VehicleDTO createVehicle(VehicleDTO dto) {
        Vehicle vehicle = toEntity(dto);
        Vehicle saved = vehicleRepository.save(vehicle);
        return toDTO(saved);
    }

    // Get all vehicles
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get vehicle by ID
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return toDTO(vehicle);
    }

    // Convert Entity to DTO
    private VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setVin(vehicle.getVin());
        dto.setManufacturer(vehicle.getManufacturer());
        dto.setModel(vehicle.getModel());
        dto.setMileage(vehicle.getMileage());
        return dto;
    }

    // Convert DTO to Entity
    private Vehicle toEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(dto.getVehicleId());
        vehicle.setVin(dto.getVin());
        vehicle.setManufacturer(dto.getManufacturer());
        vehicle.setModel(dto.getModel());
        vehicle.setMileage(dto.getMileage());
        return vehicle;
    }
}
