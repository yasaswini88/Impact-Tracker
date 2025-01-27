package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessDto;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BusinessDto> createBusiness(@RequestBody BusinessDto businessDto) {
        BusinessDto created = businessService.createBusiness(businessDto);
        return ResponseEntity.ok(created);
    }

    // READ (GET by ID)
    @GetMapping("/{id}")
    public ResponseEntity<BusinessDto> getBusinessById(@PathVariable("id") Long businessId) {
        BusinessDto dto = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(dto);
    }

    // READ (GET all)
    @GetMapping
    public ResponseEntity<List<BusinessDto>> getAllBusinesses() {
        List<BusinessDto> list = businessService.getAllBusinesses();
        return ResponseEntity.ok(list);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BusinessDto> updateBusiness(
            @PathVariable("id") Long businessId,
            @RequestBody BusinessDto businessDto
    ) {
        BusinessDto updated = businessService.updateBusiness(businessId, businessDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable("id") Long businessId) {
        businessService.deleteBusiness(businessId);
        return ResponseEntity.noContent().build();
    }

     @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody BusinessDto loginDto) {
        // 1) Look up by email
        Business existingBusiness = businessRepository.findByEmail(loginDto.getEmail());
        if (existingBusiness == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password. (Business not found)");
        }

        // 2) Check password
        if (!existingBusiness.getPassword().equals(loginDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        // 3) If match, return the businessId (or entire DTO)
        // To keep it simple, let's return the DTO with at least businessId
        BusinessDto responseDto = new BusinessDto();
        responseDto.setBusinessId(existingBusiness.getBusinessId());
        responseDto.setBusinessName(existingBusiness.getBusinessName());
        responseDto.setEmail(existingBusiness.getEmail());
        // omit the password in the response for security reasons, or set it to null
        responseDto.setPassword(null);

        return ResponseEntity.ok(responseDto);
    }


}
