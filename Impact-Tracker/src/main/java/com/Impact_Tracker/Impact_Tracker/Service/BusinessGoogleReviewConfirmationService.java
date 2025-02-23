package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessGoogleReviewConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessGoogleReviewConfirmationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessGoogleReviewConfirmationService {

    @Autowired
    private BusinessGoogleReviewConfirmationRepository confirmationRepository;

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * Creates a new record with userResponse = "Pending" 
     * for a specific business, if it doesn't already exist.
     */
   public BusinessGoogleReviewConfirmation createPendingConfirmation(Long businessId) {
        // fetch the business
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

        // check if there's already a record
        BusinessGoogleReviewConfirmation existing =
            confirmationRepository.findByBusiness_BusinessId(businessId);

        if (existing != null) {
            return existing;  // or re-set to "Pending" if you wish
        }

        BusinessGoogleReviewConfirmation newRecord = new BusinessGoogleReviewConfirmation();
        newRecord.setBusiness(business);
        newRecord.setUserResponse("Pending");
        // responseDate stays null
        return confirmationRepository.save(newRecord);
    }
    /**
     * Updates the user’s response to either "Y" or "N", sets the date/time.
     */
    public BusinessGoogleReviewConfirmation updateConfirmationResponse(Long businessId, String userResponse) {
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

        BusinessGoogleReviewConfirmation existing =
            confirmationRepository.findByBusiness_BusinessId(businessId);

        if (existing == null) {
            existing = new BusinessGoogleReviewConfirmation();
            existing.setBusiness(business);
        }
        existing.setUserResponse(userResponse);
        existing.setResponseDate(LocalDateTime.now());
        return confirmationRepository.save(existing);
    }

    public BusinessGoogleReviewConfirmation getConfirmation(Long businessId) {
        return confirmationRepository.findByBusiness_BusinessId(businessId);
    }
    /**
     * Gets the record for a given business ID
     */
   
}
