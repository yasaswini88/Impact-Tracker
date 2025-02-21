package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.SentimentAnalysisDto;
import com.Impact_Tracker.Impact_Tracker.DTO.WeeklySentimentStatsDto;
import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import java.time.LocalDateTime;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;
import com.Impact_Tracker.Impact_Tracker.Service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentimentAnalysisService {

    @Autowired
    private SentimentAnalysisRepository sentimentAnalysisRepository;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private BusinessRepository businessRepository;

    // CREATE
    public SentimentAnalysisDto createSentimentAnalysis(SentimentAnalysisDto dto) {
        // 1) Find the Business by ID
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));

        // 2) Map DTO to Entity
        SentimentAnalysis sentimentAnalysis = mapToEntity(dto);
        sentimentAnalysis.setBusiness(business);

        // 3) Save
        SentimentAnalysis saved = sentimentAnalysisRepository.save(sentimentAnalysis);

        // 4) Map back to DTO
        return mapToDto(saved);
    }

    // READ by ID
    public SentimentAnalysisDto getSentimentAnalysisById(Long id) {
        SentimentAnalysis sa = sentimentAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sentiment record not found with ID: " + id));

        return mapToDto(sa);
    }

    // READ all
    public List<SentimentAnalysisDto> getAllSentimentAnalyses() {
        return sentimentAnalysisRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public List<SentimentAnalysisDto> getByBusinessIdAndSentiment(Long businessId, String sentimentValue) {
        List<SentimentAnalysis> entities =
                sentimentAnalysisRepository.findByBusiness_BusinessIdAndSentiment(businessId, sentimentValue);

        // Map each entity to DTO
        return entities.stream()
                       .map(this::mapToDto)
                       .collect(Collectors.toList());
    }

    public List<SentimentAnalysisDto> getSentimentAnalysesBySentiment(String sentiment) {
        // Use the custom repository method
        List<SentimentAnalysis> entities = sentimentAnalysisRepository.findBySentiment(sentiment);
        
        // Map each entity to DTO
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // UPDATE
    public SentimentAnalysisDto updateSentimentAnalysis(Long id, SentimentAnalysisDto dto) {
        SentimentAnalysis existing = sentimentAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sentiment record not found with ID: " + id));

        // Optionally update the business if the incoming dto has a different businessId
        if (!existing.getBusiness().getBusinessId().equals(dto.getBusinessId())) {
            Business newBusiness = businessRepository.findById(dto.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));
            existing.setBusiness(newBusiness);
        }

        // Update other fields
        existing.setClientPhoneNumber(dto.getClientPhoneNumber());
        existing.setSentiment(dto.getSentiment());
        existing.setText(dto.getText());
        existing.setAudioUrl(dto.getAudioUrl());
        existing.setAiHandled(dto.getAiHandled());
        existing.setUpdatedAt(dto.getUpdatedAt());



        SentimentAnalysis saved = sentimentAnalysisRepository.save(existing);
        return mapToDto(saved);
    }


     public WeeklySentimentStatsDto getWeeklyStatsForBusiness(Long businessId) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        // 1) Query only last 7 days
        List<SentimentAnalysis> lastWeekList = sentimentAnalysisRepository
                .findByBusinessIdAndGeneratedAfter(businessId, oneWeekAgo);

        if (lastWeekList.isEmpty()) {
            // No data => zero percentages
            return new WeeklySentimentStatsDto(0.0, 0.0);
        }

        // 2) Count negative vs. positive
        long negativeCount = lastWeekList.stream()
                .filter(sa -> "Negative".equalsIgnoreCase(sa.getSentiment()))
                .count();

        long positiveCount = lastWeekList.stream()
                .filter(sa -> "Positive".equalsIgnoreCase(sa.getSentiment()))
                .count();

        long total = lastWeekList.size();

        double negativePct = (negativeCount / (double) total) * 100.0;
        double positivePct = (positiveCount / (double) total) * 100.0;

        // 3) Return in DTO
        return new WeeklySentimentStatsDto(negativePct, positivePct);
    }


public String analyzeWeeklyTrend(Long businessId) {
        // 1) Get the time range: last 7 days
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusDays(7);

      
        List<SentimentAnalysis> lastWeekAnalyses = sentimentAnalysisRepository.findAll()
                .stream()
                .filter(sa -> sa.getBusiness().getBusinessId().equals(businessId))
                .filter(sa -> {
                    LocalDateTime generatedAt = sa.getGeneratedAt();
                    return generatedAt != null && (generatedAt.isAfter(oneWeekAgo) || generatedAt.isEqual(oneWeekAgo));
                })
                .toList();

       
     List<String> lines = lastWeekAnalyses.stream()
    .map(sa -> "Date: " + sa.getGeneratedAt() 
            + ", Sentiment: " + sa.getSentiment())
    .collect(Collectors.toList());


        // 4) Call your new OpenAI method
        String summaryJson = openAiService.analyzeWeeklyTrendData(lines);

        return summaryJson;
    }


    // DELETE
    public void deleteSentimentAnalysis(Long id) {
        if (!sentimentAnalysisRepository.existsById(id)) {
            throw new RuntimeException("Sentiment record not found with ID: " + id);
        }
        sentimentAnalysisRepository.deleteById(id);
    }

  
    private SentimentAnalysisDto mapToDto(SentimentAnalysis sa) {
        SentimentAnalysisDto dto = new SentimentAnalysisDto();
        dto.setId(sa.getId());
        dto.setBusinessId(sa.getBusiness().getBusinessId());
        dto.setClientPhoneNumber(sa.getClientPhoneNumber());
        dto.setSentiment(sa.getSentiment());
        dto.setText(sa.getText());
        dto.setAudioUrl(sa.getAudioUrl());
        dto.setGeneratedAt(sa.getGeneratedAt());
        dto.setUpdatedAt(sa.getUpdatedAt());
        dto.setAiHandled(sa.getAiHandled());

        return dto;
    }

    private SentimentAnalysis mapToEntity(SentimentAnalysisDto dto) {
        SentimentAnalysis sa = new SentimentAnalysis();
       
        sa.setClientPhoneNumber(dto.getClientPhoneNumber());
        sa.setSentiment(dto.getSentiment());
        sa.setText(dto.getText());
        sa.setAudioUrl(dto.getAudioUrl());
        
        return sa;
    }
}
