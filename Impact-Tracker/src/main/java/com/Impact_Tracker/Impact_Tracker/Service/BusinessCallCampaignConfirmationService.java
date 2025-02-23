package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessCallCampaignConfirmationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessCallCampaignConfirmationService {

    @Autowired
    private BusinessCallCampaignConfirmationRepository callCampaignRepo;

    @Autowired
    private BusinessRepository businessRepository;

    // If not found, create with "Pending".
    public BusinessCallCampaignConfirmation createPending(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        BusinessCallCampaignConfirmation existing = callCampaignRepo
                .findByBusiness_BusinessId(businessId);
        if (existing != null) {
            return existing;
        }

        BusinessCallCampaignConfirmation newRec = new BusinessCallCampaignConfirmation();
        newRec.setBusiness(business);
        newRec.setUserResponse("Pending");
        return callCampaignRepo.save(newRec);
    }

    // If no record yet, create one. Then set userResponse + date
    public BusinessCallCampaignConfirmation updateUserResponse(Long businessId, String userResponse) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        BusinessCallCampaignConfirmation existing = callCampaignRepo
                .findByBusiness_BusinessId(businessId);
        if (existing == null) {
            existing = new BusinessCallCampaignConfirmation();
            existing.setBusiness(business);
        }
        existing.setUserResponse(userResponse);
        existing.setResponseDate(LocalDateTime.now());

        return callCampaignRepo.save(existing);
    }

    public BusinessCallCampaignConfirmation getByBusiness(Long businessId) {
        return callCampaignRepo.findByBusiness_BusinessId(businessId);
    }
}
