package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignStrategies;
import com.Impact_Tracker.Impact_Tracker.Repo.CallCampaignStrategiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallCampaignStrategiesService {

    @Autowired
    private CallCampaignStrategiesRepository strategiesRepository;

   public CallCampaignStrategies createStrategy(String strategyName, String description) {
    CallCampaignStrategies strategy = new CallCampaignStrategies();
    strategy.setStrategyName(strategyName);
    strategy.setDescription(description);
    return strategiesRepository.save(strategy);
}


    public List<CallCampaignStrategies> getAllStrategies() {
        return strategiesRepository.findAll();
    }

    public CallCampaignStrategies getById(Long id) {
        return strategiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found with id=" + id));
    }

    public CallCampaignStrategies updateStrategy(Long id, String newName) {
        CallCampaignStrategies existing = getById(id);
        existing.setStrategyName(newName);
        return strategiesRepository.save(existing);
    }

    public void deleteStrategy(Long id) {
        if (!strategiesRepository.existsById(id)) {
            throw new RuntimeException("No strategy found with id=" + id);
        }
        strategiesRepository.deleteById(id);
    }
}
