package com.fleethealth.service;

import com.fleethealth.dto.VehicleDTO;
import com.fleethealth.entity.Vehicle;
import com.fleethealth.exception.ResourceNotFoundException;
import com.fleethealth.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle testVehicle;
    private VehicleDTO testVehicleDTO;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setVehicleId("VH-102");
        testVehicle.setVin("ABC123456789");
        testVehicle.setManufacturer("Toyota");
        testVehicle.setModel("Corolla");
        testVehicle.setMileage(98500);

        testVehicleDTO = new VehicleDTO();
        testVehicleDTO.setVehicleId("VH-102");
        testVehicleDTO.setVin("ABC123456789");
        testVehicleDTO.setManufacturer("Toyota");
        testVehicleDTO.setModel("Corolla");
        testVehicleDTO.setMileage(98500);
    }

    @Test
    @DisplayName("Create vehicle - should save and return VehicleDTO")
    void createVehicle_shouldSaveAndReturnDTO() {
        // Arrange
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        // Act
        VehicleDTO result = vehicleService.createVehicle(testVehicleDTO);

        // Assert
        assertNotNull(result);
        assertEquals("VH-102", result.getVehicleId());
        assertEquals("ABC123456789", result.getVin());
        assertEquals("Toyota", result.getManufacturer());
        assertEquals("Corolla", result.getModel());
        assertEquals(98500, result.getMileage());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Get all vehicles - should return list of VehicleDTOs")
    void getAllVehicles_shouldReturnList() {
        // Arrange
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(2L);
        vehicle2.setVehicleId("VH-103");
        vehicle2.setVin("DEF987654321");
        vehicle2.setManufacturer("Honda");
        vehicle2.setModel("Civic");
        vehicle2.setMileage(45000);

        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(testVehicle, vehicle2));

        // Act
        List<VehicleDTO> result = vehicleService.getAllVehicles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("VH-102", result.get(0).getVehicleId());
        assertEquals("VH-103", result.get(1).getVehicleId());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get vehicle by ID - should return VehicleDTO when found")
    void getVehicleById_shouldReturnDTO_whenFound() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));

        // Act
        VehicleDTO result = vehicleService.getVehicleById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("VH-102", result.getVehicleId());
        assertEquals(1L, result.getId());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get vehicle by ID - should throw exception when not found")
    void getVehicleById_shouldThrowException_whenNotFound() {
        // Arrange
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.getVehicleById(99L)
        );
        assertEquals("Vehicle not found with id: 99", exception.getMessage());
    }
}
