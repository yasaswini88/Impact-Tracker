package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.CampaignSelectionRequest;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignStrategies;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignStrategySelection;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallCampaignStrategiesRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessCallCampaignStrategySelectionRepository;
import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;
import com.Impact_Tracker.Impact_Tracker.Service.TwilioStudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BusinessCallCampaignStrategySelectionService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CallCampaignStrategiesRepository strategiesRepository;

    @Autowired
    private BusinessCallCampaignStrategySelectionRepository selectionRepository;

    @Autowired
    private SentimentAnalysisRepository sentimentAnalysisRepository;

    @Autowired
    private TwilioStudioService twilioStudioService;

    /**
     * Save multiple rows in "business_call_campaign_strategy_selection"
     * based on the user’s selected strategies in the form.
     */
    // public void saveSelections(CampaignSelectionRequest request) {
    //     System.out.println("Entered (COMMENTED OUT) saveSelections with request: " + request);
    //     // 1) Find the business
    //     Business business = businessRepository.findById(request.getBusinessId())
    //         .orElseThrow(() -> {
    //             System.out.println("Business not found with ID: " + request.getBusinessId());
    //             return new RuntimeException("Business not found with ID: " + request.getBusinessId());
    //         });
    //     System.out.println("Found business (COMMENTED OUT method): " + business);

    //     // 2) For each strategyId in request, create a new row
    //     for (Long strategyId : request.getStrategyIds()) {
    //         System.out.println("Processing strategyId (COMMENTED OUT method): " + strategyId);
    //         CallCampaignStrategies strategy = strategiesRepository.findById(strategyId)
    //             .orElseThrow(() -> {
    //                 System.out.println("Strategy not found with ID (COMMENTED OUT method): " + strategyId);
    //                 return new RuntimeException("Strategy not found with ID: " + strategyId);
    //             });
    //         System.out.println("Found strategy (COMMENTED OUT method): " + strategy);

    //         // 3) Build entity
    //         BusinessCallCampaignStrategySelection selection = 
    //             new BusinessCallCampaignStrategySelection();

    //         selection.setBusiness(business);
    //         selection.setCallCampaignStrategy(strategy);
    //         selection.setCallCampaignVoice(request.getCallCampaignVoice());
    //         selection.setTargetAudience(request.getTargetAudience());

    //         // 4) Save each row
    //         selectionRepository.save(selection);
    //         System.out.println("Saved selection (COMMENTED OUT method): " + selection);
    //     }
    // }

    public void saveSelections(CampaignSelectionRequest request) {
        System.out.println("Entered saveSelections with request: " + request);

        // 1) Find the business
        Business business = businessRepository.findById(request.getBusinessId())
            .orElseThrow(() -> {
                System.out.println("Business not found with ID: " + request.getBusinessId());
                return new RuntimeException("Business not found with ID: " + request.getBusinessId());
            });
        System.out.println("Found business: " + business);

        // 2) For each strategyId in request, create/save a new row
        for (Long strategyId : request.getStrategyIds()) {
            System.out.println("Processing strategyId: " + strategyId);
            CallCampaignStrategies strategy = strategiesRepository.findById(strategyId)
                .orElseThrow(() -> {
                    System.out.println("Strategy not found with ID: " + strategyId);
                    return new RuntimeException("Strategy not found with ID: " + strategyId);
                });
            System.out.println("Found strategy: " + strategy);

            BusinessCallCampaignStrategySelection selection =
                new BusinessCallCampaignStrategySelection();

            selection.setBusiness(business);
            selection.setCallCampaignStrategy(strategy);
            selection.setCallCampaignVoice(request.getCallCampaignVoice());
            selection.setTargetAudience(request.getTargetAudience());

            selectionRepository.save(selection);
            System.out.println("Saved selection: " + selection);
        }

        // 3) **Immediately** call the relevant audience if user wants POSITIVE/NEGATIVE/ALL
        if (request.getTargetAudience() != null) {
            String target = request.getTargetAudience().toUpperCase();
            System.out.println("Target audience is: " + target);

            if ("POSITIVE".equals(target)) {
                System.out.println("Fetching Positive sentiment records...");
                List<SentimentAnalysis> positiveList =
                    sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                        request.getBusinessId(),
                        "Positive"
                    );
                System.out.println("Number of Positive records found: " + positiveList.size());

                for (SentimentAnalysis sa : positiveList) {
                    String phone = sa.getClientPhoneNumber();
                    System.out.println("Calling phone (Positive): " + phone);
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                                phone,
                                request.getCallCampaignVoice(),
                                999L,
                                request.getBusinessId()
                        );
                    }
                }

            } else if ("NEGATIVE".equals(target)) {
                System.out.println("Fetching Negative sentiment records...");
                List<SentimentAnalysis> negativeList =
                    sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                        request.getBusinessId(),
                        "Negative"
                    );
                System.out.println("Number of Negative records found: " + negativeList.size());

                for (SentimentAnalysis sa : negativeList) {
                    String phone = sa.getClientPhoneNumber();
                    System.out.println("Calling phone (Negative): " + phone);
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                                phone,
                                request.getCallCampaignVoice(),
                                999L,
                                request.getBusinessId()
                        );
                    }
                }

            } else if ("ALL".equals(target)) {
                System.out.println("Fetching ALL sentiment records for this business...");
                List<SentimentAnalysis> allList = sentimentAnalysisRepository.findAll()
                        .stream()
                        .filter(sa -> sa.getBusiness().getBusinessId().equals(request.getBusinessId()))
                        .toList();
                System.out.println("Number of ALL sentiment records found: " + allList.size());

                for (SentimentAnalysis sa : allList) {
                    String phone = sa.getClientPhoneNumber();
                    System.out.println("Calling phone (ALL): " + phone);
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                                phone,
                                request.getCallCampaignVoice(),
                                999L,
                                request.getBusinessId()
                        );
                    }
                }
            } else {
                System.out.println("Target audience does not match POSITIVE, NEGATIVE, or ALL. No calls made.");
            }
        } else {
            System.out.println("No target audience specified. Skipping immediate calls.");
        }
    }


    public List<BusinessCallCampaignStrategySelection> getSubmissionsForBusiness(Long businessId) {
        return selectionRepository.findAllByBusinessIdDesc(businessId);
    }

}
