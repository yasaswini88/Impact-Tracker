package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.SentimentAnalysisDto;
import com.Impact_Tracker.Impact_Tracker.DTO.WeeklySentimentStatsDto;
import com.Impact_Tracker.Impact_Tracker.DTO.CallVolumeRequest;
import com.Impact_Tracker.Impact_Tracker.Service.SentimentAnalysisService;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;




import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/sentiment-analyses")
public class SentimentAnalysisController {

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private BusinessRepository businessRepository;



    // CREATE
    @PostMapping
    public ResponseEntity<SentimentAnalysisDto> create(@RequestBody SentimentAnalysisDto dto) {
        SentimentAnalysisDto created = sentimentAnalysisService.createSentimentAnalysis(dto);
        return ResponseEntity.ok(created);
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<SentimentAnalysisDto> getById(@PathVariable Long id) {
        SentimentAnalysisDto saDto = sentimentAnalysisService.getSentimentAnalysisById(id);
        return ResponseEntity.ok(saDto);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<SentimentAnalysisDto>> getAll() {
        List<SentimentAnalysisDto> list = sentimentAnalysisService.getAllSentimentAnalyses();
        return ResponseEntity.ok(list);
    }


// READ by sentiment
    @GetMapping("/by-sentiment/{sentimentValue}")
    public ResponseEntity<List<SentimentAnalysisDto>> getBySentiment(@PathVariable("sentimentValue") String sentimentValue) {
        List<SentimentAnalysisDto> list = sentimentAnalysisService.getSentimentAnalysesBySentiment(sentimentValue);
        return ResponseEntity.ok(list);
    }


     @GetMapping("/by-sentiment/{businessId}/{sentimentValue}")
    public ResponseEntity<List<SentimentAnalysisDto>> getByBusinessAndSentiment(
            @PathVariable("businessId") Long businessId,
            @PathVariable("sentimentValue") String sentimentValue) {

        List<SentimentAnalysisDto> results =
                sentimentAnalysisService.getByBusinessIdAndSentiment(businessId, sentimentValue);

        return ResponseEntity.ok(results);
    }

        @GetMapping("/weekly-stats/{businessId}")
    public ResponseEntity<WeeklySentimentStatsDto> getWeeklyStats(
            @PathVariable("businessId") Long businessId) {

        WeeklySentimentStatsDto stats = sentimentAnalysisService.getWeeklyStatsForBusiness(businessId);
        return ResponseEntity.ok(stats);
    }


@GetMapping("/weekly-trend/{businessId}")
    public ResponseEntity<String> getWeeklyTrendSummary(@PathVariable Long businessId) {
        String summaryJson = sentimentAnalysisService.analyzeWeeklyTrend(businessId);
        return ResponseEntity.ok(summaryJson);
    }


@PostMapping("/call-volume-trend/{businessId}")
public ResponseEntity<Map<String, Object>> getCallVolumeTrend(@PathVariable("businessId") Long bizId) {
    Business biz = businessRepository.findById(bizId)
            .orElseThrow(() -> new RuntimeException("Business not found with ID: " + bizId));

    CallVolumeRequest request = SentimentAnalysisService.BUSINESS_CALL_VOLUME_DATA.getOrDefault(
            bizId,
            new CallVolumeRequest(
                    List.of(30, 30, 30, 30, 30, 30, 30),
                    List.of(10, 10, 10, 10, 10, 10, 10),
                    List.of(5, 5, 5, 5, 5, 5, 5),
                    List.of("aug", "sep", "oct", "nov", "dec", "jan", "feb")
            )
    );

    Map<String, Object> response = sentimentAnalysisService.analyzeCallVolumeTrends(
            request.getAnswered(),
            request.getMissed(),
            request.getVoicemail(),
            request.getMonths(),
            biz
    );

    return ResponseEntity.ok(response);
}



    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SentimentAnalysisDto> update(@PathVariable Long id,
                                                       @RequestBody SentimentAnalysisDto dto) {
        SentimentAnalysisDto updated = sentimentAnalysisService.updateSentimentAnalysis(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sentimentAnalysisService.deleteSentimentAnalysis(id);
        return ResponseEntity.noContent().build();
    }
}
