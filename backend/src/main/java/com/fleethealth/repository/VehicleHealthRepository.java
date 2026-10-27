package com.fleethealth.repository;

import com.fleethealth.entity.VehicleHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleHealthRepository extends JpaRepository<VehicleHealth, Long> {

    List<VehicleHealth> findByVehicleIdOrderByRecordedAtDesc(Long vehicleId);
}
