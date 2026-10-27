package com.fleethealth.controller;

import com.fleethealth.dto.VehicleDTO;
import com.fleethealth.exception.ResourceNotFoundException;
import com.fleethealth.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private VehicleDTO testVehicleDTO;

    @BeforeEach
    void setUp() {
        testVehicleDTO = new VehicleDTO(1L, "VH-102", "ABC123456789", "Toyota", "Corolla", 98500);
    }

    @Test
    @DisplayName("POST /api/vehicles - should create vehicle and return 201")
    void createVehicle_shouldReturn201() {
        // Arrange
        VehicleDTO inputDTO = new VehicleDTO();
        inputDTO.setVehicleId("VH-102");
        inputDTO.setVin("ABC123456789");
        inputDTO.setManufacturer("Toyota");
        inputDTO.setModel("Corolla");
        inputDTO.setMileage(98500);

        when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(testVehicleDTO);

        // Act
        ResponseEntity<VehicleDTO> response = vehicleController.createVehicle(inputDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VH-102", response.getBody().getVehicleId());
        assertEquals("ABC123456789", response.getBody().getVin());
        assertEquals("Toyota", response.getBody().getManufacturer());
        verify(vehicleService, times(1)).createVehicle(any(VehicleDTO.class));
    }

    @Test
    @DisplayName("GET /api/vehicles - should return list of vehicles")
    void getAllVehicles_shouldReturnList() {
        // Arrange
        List<VehicleDTO> vehicles = Arrays.asList(
                testVehicleDTO,
                new VehicleDTO(2L, "VH-103", "DEF987654321", "Honda", "Civic", 45000)
        );
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        // Act
        ResponseEntity<List<VehicleDTO>> response = vehicleController.getAllVehicles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("VH-102", response.getBody().get(0).getVehicleId());
        assertEquals("VH-103", response.getBody().get(1).getVehicleId());
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} - should return vehicle when found")
    void getVehicleById_shouldReturnVehicle() {
        // Arrange
        when(vehicleService.getVehicleById(1L)).thenReturn(testVehicleDTO);

        // Act
        ResponseEntity<VehicleDTO> response = vehicleController.getVehicleById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VH-102", response.getBody().getVehicleId());
        assertEquals("Toyota", response.getBody().getManufacturer());
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} - should propagate exception when not found")
    void getVehicleById_shouldThrow_whenNotFound() {
        // Arrange
        when(vehicleService.getVehicleById(99L))
                .thenThrow(new ResourceNotFoundException("Vehicle not found with id: 99"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> vehicleController.getVehicleById(99L));
    }
}
