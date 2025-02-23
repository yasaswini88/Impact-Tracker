package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessCallCampaignConfirmationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessCallCampaignStrategySelectionRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessCallCampaignConfirmationService {

    @Autowired
    private BusinessCallCampaignConfirmationRepository callCampaignRepo;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessCallCampaignStrategySelectionRepository selectionRepository;
    
    // If not found, create with "Pending".
    public BusinessCallCampaignConfirmation createPending(Long businessId) {
        System.out.println("Entered createPending with businessId: " + businessId);

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> {
                    System.out.println("Business not found for ID: " + businessId);
                    return new RuntimeException("Business not found");
                });

        System.out.println("Business found: " + business);

        BusinessCallCampaignConfirmation existing = callCampaignRepo
                .findByBusiness_BusinessId(businessId);

        System.out.println("Existing BusinessCallCampaignConfirmation: " + existing);

        if (existing != null) {
            System.out.println("Returning existing record for createPending.");
            return existing;
        }

        BusinessCallCampaignConfirmation newRec = new BusinessCallCampaignConfirmation();
        newRec.setBusiness(business);
        newRec.setUserResponse("Pending");

        BusinessCallCampaignConfirmation savedRec = callCampaignRepo.save(newRec);
        System.out.println("No existing record found. Created new record: " + savedRec);

        return savedRec;
    }

    // If no record yet, create one. Then set userResponse + date
    public BusinessCallCampaignConfirmation updateUserResponse(Long businessId, String userResponse) {
        System.out.println("Entered updateUserResponse with businessId: " + businessId + ", userResponse: " + userResponse);

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> {
                    System.out.println("Business not found for ID: " + businessId);
                    return new RuntimeException("Business not found");
                });

        System.out.println("Business found: " + business);

        BusinessCallCampaignConfirmation existing = callCampaignRepo
                .findByBusiness_BusinessId(businessId);

        System.out.println("Existing BusinessCallCampaignConfirmation: " + existing);

        if (existing == null) {
            System.out.println("No existing confirmation record. Creating new one.");
            existing = new BusinessCallCampaignConfirmation();
            existing.setBusiness(business);
        }

        existing.setUserResponse(userResponse);
        existing.setResponseDate(LocalDateTime.now());

        BusinessCallCampaignConfirmation updatedRec = callCampaignRepo.save(existing);
        System.out.println("Record updated/saved: " + updatedRec);

        return updatedRec;
    }

    public BusinessCallCampaignConfirmation getByBusiness(Long businessId) {
        System.out.println("Entered getByBusiness with businessId: " + businessId);
        BusinessCallCampaignConfirmation record = callCampaignRepo.findByBusiness_BusinessId(businessId);
        System.out.println("Returning record from getByBusiness: " + record);
        return record;
    }
}
