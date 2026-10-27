package com.fleethealth.repository;

import com.fleethealth.entity.MaintenanceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceAlertRepository extends JpaRepository<MaintenanceAlert, Long> {

    List<MaintenanceAlert> findByVehicleId(Long vehicleId);

    List<MaintenanceAlert> findByStatus(String status);
}
