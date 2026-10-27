package com.fleethealth.controller;

import com.fleethealth.dto.MaintenanceAlertDTO;
import com.fleethealth.service.MaintenanceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceAlertController {

    private final MaintenanceAlertService alertService;

    public MaintenanceAlertController(MaintenanceAlertService alertService) {
        this.alertService = alertService;
    }

    // GET /api/maintenance/alerts — List all alerts
    @GetMapping("/alerts")
    public ResponseEntity<List<MaintenanceAlertDTO>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    // PUT /api/maintenance/alerts/{id}/resolve — Resolve an alert
    @PutMapping("/alerts/{id}/resolve")
    public ResponseEntity<MaintenanceAlertDTO> resolveAlert(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.resolveAlert(id));
    }
}
