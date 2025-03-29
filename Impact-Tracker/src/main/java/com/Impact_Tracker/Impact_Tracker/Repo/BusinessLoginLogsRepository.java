package com.Impact_Tracker.Impact_Tracker.Repo;

import org.springframework.stereotype.Repository;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessLoginLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


@Repository
public interface BusinessLoginLogsRepository extends JpaRepository<BusinessLoginLogs,Long>{
        List<BusinessLoginLogs> findByBusiness_BusinessId(Long businessId);
    }
    


