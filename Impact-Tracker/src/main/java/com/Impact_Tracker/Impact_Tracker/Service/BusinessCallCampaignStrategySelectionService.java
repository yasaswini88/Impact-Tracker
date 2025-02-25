package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.CampaignSelectionRequest;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignStrategies;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignStrategySelection;
import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignOutgoingLog;   // <-- Make sure this is imported
import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;

import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallCampaignStrategiesRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessCallCampaignStrategySelectionRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallCampaignOutgoingLogRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;

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

    @Autowired
    private CallCampaignOutgoingLogRepository callCampaignOutgoingLogRepository;

    /**
     * Save multiple rows in "business_call_campaign_strategy_selection"
     * and immediately make calls to the relevant audience.
     */
    public void saveSelections(CampaignSelectionRequest request) {
        // 1) Find the Business
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + request.getBusinessId()));

        // 2) For each chosen strategy, create "selection" AND do the calls
        for (Long strategyId : request.getStrategyIds()) {

            // A) Look up strategy
            CallCampaignStrategies strategy = strategiesRepository.findById(strategyId)
                    .orElseThrow(() -> new RuntimeException("Strategy not found with ID: " + strategyId));

            // B) Create the selection record
            BusinessCallCampaignStrategySelection selection = new BusinessCallCampaignStrategySelection();
            selection.setBusiness(business);
            selection.setCallCampaignStrategy(strategy);
            selection.setCallCampaignVoice(request.getCallCampaignVoice());
            selection.setTargetAudience(request.getTargetAudience());
            selectionRepository.save(selection);

            // C) Decide whom to call based on targetAudience
            if (request.getTargetAudience() != null) {
                String target = request.getTargetAudience().toUpperCase();

                if ("POSITIVE".equals(target)) {
                    List<SentimentAnalysis> positiveList =
                            sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                                    request.getBusinessId(),
                                    "Positive"
                            );
                    for (SentimentAnalysis sa : positiveList) {
                        String phone = sa.getClientPhoneNumber();
                        if (phone != null && !phone.isEmpty()) {
                            // 1) Call Twilio
                            String twilioResponse = twilioStudioService.createStudioExecutionForCampaign(
                                    phone,
                                    request.getCallCampaignVoice(),
                                    999L,
                                    request.getBusinessId()
                            );
                            // 2) Create log
                            CallCampaignOutgoingLog log = new CallCampaignOutgoingLog();
                            log.setStrategySelection(selection);
                            log.setClientPhoneNumber(phone);
                            log.setTwilioResponse(twilioResponse);
                            callCampaignOutgoingLogRepository.save(log);
                        }
                    }

                } else if ("NEGATIVE".equals(target)) {
                    List<SentimentAnalysis> negativeList =
                            sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(
                                    request.getBusinessId(),
                                    "Negative"
                            );
                    for (SentimentAnalysis sa : negativeList) {
                        String phone = sa.getClientPhoneNumber();
                        if (phone != null && !phone.isEmpty()) {
                            String twilioResponse = twilioStudioService.createStudioExecutionForCampaign(
                                    phone,
                                    request.getCallCampaignVoice(),
                                    999L,
                                    request.getBusinessId()
                            );
                            CallCampaignOutgoingLog log = new CallCampaignOutgoingLog();
                            log.setStrategySelection(selection);
                            log.setClientPhoneNumber(phone);
                            log.setTwilioResponse(twilioResponse);
                            callCampaignOutgoingLogRepository.save(log);
                        }
                    }

                } else if ("ALL".equals(target)) {
                    List<SentimentAnalysis> allList = sentimentAnalysisRepository
                            .findAll()
                            .stream()
                            .filter(sa -> sa.getBusiness().getBusinessId().equals(request.getBusinessId()))
                            .toList();
                    for (SentimentAnalysis sa : allList) {
                        String phone = sa.getClientPhoneNumber();
                        if (phone != null && !phone.isEmpty()) {
                            String twilioResponse = twilioStudioService.createStudioExecutionForCampaign(
                                    phone,
                                    request.getCallCampaignVoice(),
                                    999L,
                                    request.getBusinessId()
                            );
                            CallCampaignOutgoingLog log = new CallCampaignOutgoingLog();
                            log.setStrategySelection(selection);
                            log.setClientPhoneNumber(phone);
                            log.setTwilioResponse(twilioResponse);
                            callCampaignOutgoingLogRepository.save(log);
                        }
                    }

                } else {
                    // If the string isn't POSITIVE, NEGATIVE, or ALL
                    System.out.println("Target audience does not match POSITIVE, NEGATIVE, or ALL. No calls made.");
                }

            } else {
                // If no target audience was specified at all
                System.out.println("No target audience specified. Skipping immediate calls.");
            }

        } // end for loop
    }

    /**
     * Example method if you want to list all submissions for a business.
     */
    public List<BusinessCallCampaignStrategySelection> getSubmissionsForBusiness(Long businessId) {
        return selectionRepository.findAllByBusinessIdDesc(businessId);
    }

}
