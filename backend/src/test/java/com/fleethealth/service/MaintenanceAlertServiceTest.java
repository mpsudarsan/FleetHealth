package com.fleethealth.service;

import com.fleethealth.entity.MaintenanceAlert;
import com.fleethealth.entity.VehicleHealth;
import com.fleethealth.repository.MaintenanceAlertRepository;
import com.fleethealth.repository.VehicleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceAlertServiceTest {

    @Mock
    private MaintenanceAlertRepository alertRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private MaintenanceAlertService alertService;

    @Test
    @DisplayName("Rule Engine - should create HIGH alert when oil life < 10")
    void evaluateHealth_shouldCreateHighAlert_whenOilLifeLow() {
        // Arrange
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(8);
        health.setTirePressure(35);
        health.setEngineTemperature(85);

        when(alertRepository.save(any(MaintenanceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert
        ArgumentCaptor<MaintenanceAlert> captor = ArgumentCaptor.forClass(MaintenanceAlert.class);
        verify(alertRepository, times(1)).save(captor.capture());

        MaintenanceAlert savedAlert = captor.getValue();
        assertEquals("HIGH", savedAlert.getSeverity());
        assertEquals("Oil life below 10%", savedAlert.getReason());
        assertEquals(1L, savedAlert.getVehicleId());
    }

    @Test
    @DisplayName("Rule Engine - should create MEDIUM alert when tire pressure < 30")
    void evaluateHealth_shouldCreateMediumAlert_whenTirePressureLow() {
        // Arrange
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(50);
        health.setTirePressure(27);
        health.setEngineTemperature(85);

        when(alertRepository.save(any(MaintenanceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert
        ArgumentCaptor<MaintenanceAlert> captor = ArgumentCaptor.forClass(MaintenanceAlert.class);
        verify(alertRepository, times(1)).save(captor.capture());

        MaintenanceAlert savedAlert = captor.getValue();
        assertEquals("MEDIUM", savedAlert.getSeverity());
        assertEquals("Tire pressure below 30 PSI", savedAlert.getReason());
    }

    @Test
    @DisplayName("Rule Engine - should create HIGH alert when engine temperature > 100")
    void evaluateHealth_shouldCreateHighAlert_whenEngineTemperatureHigh() {
        // Arrange
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(50);
        health.setTirePressure(35);
        health.setEngineTemperature(105);

        when(alertRepository.save(any(MaintenanceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert
        ArgumentCaptor<MaintenanceAlert> captor = ArgumentCaptor.forClass(MaintenanceAlert.class);
        verify(alertRepository, times(1)).save(captor.capture());

        MaintenanceAlert savedAlert = captor.getValue();
        assertEquals("HIGH", savedAlert.getSeverity());
        assertEquals("Engine temperature is high", savedAlert.getReason());
    }

    @Test
    @DisplayName("Rule Engine - should create HIGH alert when DTC exists")
    void evaluateHealth_shouldCreateHighAlert_whenDTCExists() {
        // Arrange
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(50);
        health.setTirePressure(35);
        health.setEngineTemperature(85);
        health.setDiagnosticTroubleCode("ENGINE_OVERHEAT");

        when(alertRepository.save(any(MaintenanceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert
        ArgumentCaptor<MaintenanceAlert> captor = ArgumentCaptor.forClass(MaintenanceAlert.class);
        verify(alertRepository, times(1)).save(captor.capture());

        MaintenanceAlert savedAlert = captor.getValue();
        assertEquals("HIGH", savedAlert.getSeverity());
        assertEquals("Diagnostic trouble code detected", savedAlert.getReason());
    }

    @Test
    @DisplayName("Rule Engine - should create MULTIPLE alerts for unhealthy vehicle")
    void evaluateHealth_shouldCreateMultipleAlerts_whenMultipleIssues() {
        // Arrange - matches the example from the requirements
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(8);               // < 10 → HIGH
        health.setTirePressure(27);          // < 30 → MEDIUM
        health.setFuelLevel(40);
        health.setEngineTemperature(105);    // > 100 → HIGH
        health.setBatteryLevel(75);
        health.setDiagnosticTroubleCode("ENGINE_OVERHEAT"); // exists → HIGH

        when(alertRepository.save(any(MaintenanceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert - should create exactly 4 alerts
        ArgumentCaptor<MaintenanceAlert> captor = ArgumentCaptor.forClass(MaintenanceAlert.class);
        verify(alertRepository, times(4)).save(captor.capture());

        List<MaintenanceAlert> savedAlerts = captor.getAllValues();
        assertEquals(4, savedAlerts.size());

        // Verify each alert
        assertEquals("Oil life below 10%", savedAlerts.get(0).getReason());
        assertEquals("HIGH", savedAlerts.get(0).getSeverity());

        assertEquals("Tire pressure below 30 PSI", savedAlerts.get(1).getReason());
        assertEquals("MEDIUM", savedAlerts.get(1).getSeverity());

        assertEquals("Engine temperature is high", savedAlerts.get(2).getReason());
        assertEquals("HIGH", savedAlerts.get(2).getSeverity());

        assertEquals("Diagnostic trouble code detected", savedAlerts.get(3).getReason());
        assertEquals("HIGH", savedAlerts.get(3).getSeverity());
    }

    @Test
    @DisplayName("Rule Engine - should create NO alerts for healthy vehicle")
    void evaluateHealth_shouldCreateNoAlerts_whenHealthy() {
        // Arrange
        VehicleHealth health = new VehicleHealth();
        health.setOilLife(80);
        health.setTirePressure(35);
        health.setFuelLevel(60);
        health.setEngineTemperature(85);
        health.setBatteryLevel(90);

        // Act
        alertService.evaluateHealth(1L, health);

        // Assert - should NOT save any alerts
        verify(alertRepository, never()).save(any(MaintenanceAlert.class));
    }
}
