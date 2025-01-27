package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Get all appointments associated with a business
    List<Appointment> findByBusiness_BusinessId(Long businessId);

    // Get all appointments associated with a business on a specific date
    @Query("SELECT a FROM Appointment a WHERE a.business.businessId = :businessId AND a.appointmentDate = :date")
    List<Appointment> findByBusinessIdAndDate(@Param("businessId") Long businessId, @Param("date") String date);
}
