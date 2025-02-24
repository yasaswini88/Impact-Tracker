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
import com.Impact_Tracker.Impact_Tracker.DTO.CallCampaignSelectionDto;
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
            .orElseThrow(() -> new RuntimeException(
                "Business not found with ID: " + request.getBusinessId())
            );

        // 2) For each selection (strategyId, voice, audience)
        for (CallCampaignSelectionDto selectionDto : request.getSelections()) {

            Long strategyId = selectionDto.getStrategyId();
            String voice = selectionDto.getCallCampaignVoice();
            String audience = selectionDto.getTargetAudience();

            // fetch the strategy
            CallCampaignStrategies strategy = strategiesRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException(
                    "Strategy not found with ID: " + strategyId)
                );

            // 3) Build entity
            BusinessCallCampaignStrategySelection entity = new BusinessCallCampaignStrategySelection();
            entity.setBusiness(business);
            entity.setCallCampaignStrategy(strategy);
            entity.setCallCampaignVoice(voice != null ? voice : "");
            entity.setTargetAudience(audience != null ? audience : "ALL");

            // 4) Save each row
            selectionRepository.save(entity);
            System.out.println("Saved selection entity: " + entity);
        }

        // 5) (Optional) Immediately call each selection's audience
        //    If you want to do calls, do it here:
        for (CallCampaignSelectionDto selectionDto : request.getSelections()) {
            String audience = selectionDto.getTargetAudience();
            String voice = selectionDto.getCallCampaignVoice();

            if (audience == null) audience = "ALL";

            // fetch appropriate phone numbers
            if ("POSITIVE".equalsIgnoreCase(audience)) {
                List<SentimentAnalysis> positiveList =
                    sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                        request.getBusinessId(), "Positive");

                for (SentimentAnalysis sa : positiveList) {
                    String phone = sa.getClientPhoneNumber();
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                            phone,
                            voice,    // pass the custom voice
                            999L,
                            request.getBusinessId()
                        );
                    }
                }
            } else if ("NEGATIVE".equalsIgnoreCase(audience)) {
                List<SentimentAnalysis> negativeList =
                    sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                        request.getBusinessId(), "Negative");

                for (SentimentAnalysis sa : negativeList) {
                    String phone = sa.getClientPhoneNumber();
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                            phone,
                            voice,
                            999L,
                            request.getBusinessId()
                        );
                    }
                }
            } else if ("ALL".equalsIgnoreCase(audience)) {
                List<SentimentAnalysis> allList = sentimentAnalysisRepository.findAll()
                    .stream()
                    .filter(sa -> sa.getBusiness().getBusinessId().equals(request.getBusinessId()))
                    .toList();
                for (SentimentAnalysis sa : allList) {
                    String phone = sa.getClientPhoneNumber();
                    if (phone != null && !phone.isEmpty()) {
                        twilioStudioService.createStudioExecutionForCampaign(
                            phone,
                            voice,
                            999L,
                            request.getBusinessId()
                        );
                    }
                }
            } else {
                System.out.println("Unknown audience type, skipping calls: " + audience);
            }
        }

        System.out.println("All selections processed and calls triggered if needed.");
    }
}
