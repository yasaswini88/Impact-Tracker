package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.EndUserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndUserConfirmationRepository extends JpaRepository<EndUserConfirmation, Long> {
    
    // If you want to find by appointmentId + customerId + status, for example:
    // Optional<EndUserConfirmation> findByAppointmentIdAndCustomerIdAndStatus(Long appointmentId, Long customerId, String status);

}
