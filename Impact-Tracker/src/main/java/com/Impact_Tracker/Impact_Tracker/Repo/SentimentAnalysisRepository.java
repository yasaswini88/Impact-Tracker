package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SentimentAnalysisRepository extends JpaRepository<SentimentAnalysis, Long> {

    List<SentimentAnalysis> findBySentiment(String sentiment);

    List<SentimentAnalysis> findByBusiness_BusinessIdAndSentiment(Long businessId, String sentiment);
  

  @Query("SELECT s FROM SentimentAnalysis s " +
       "WHERE s.business.businessId = :bizId " +
       "  AND s.generatedAt >= :startDate")
List<SentimentAnalysis> findByBusinessIdAndGeneratedAfter(
    @Param("bizId") Long businessId,
    @Param("startDate") LocalDateTime startDate
);
    
  
}
