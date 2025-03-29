package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessDto;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessLoginLogsService;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessLoginLogsService loginLogsService;

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
            @RequestBody BusinessDto businessDto) {
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

        loginLogsService.createLoginLog(existingBusiness.getBusinessId());

        // 3) Prepare response with default values instead of null
        BusinessDto responseDto = new BusinessDto();
        responseDto.setBusinessId(existingBusiness.getBusinessId());
        responseDto.setBusinessName(existingBusiness.getBusinessName() != null ? existingBusiness.getBusinessName()
                : "Unknown Business Name");
        responseDto.setEmail(existingBusiness.getEmail() != null ? existingBusiness.getEmail() : "No Email Provided");
        responseDto.setRegistrationNumber(
                existingBusiness.getRegistrationNumber() != null ? existingBusiness.getRegistrationNumber() : "N/A");
        responseDto.setOpeningTime(
                existingBusiness.getOpeningTime() != null ? existingBusiness.getOpeningTime().toString() : "Not Set");
        responseDto.setClosingTime(
                existingBusiness.getClosingTime() != null ? existingBusiness.getClosingTime().toString() : "Not Set");
        responseDto.setPhoneNumber(
                existingBusiness.getPhoneNumber() != null ? existingBusiness.getPhoneNumber() : "No Phone Number");
        responseDto.setZipCode(existingBusiness.getZipCode() != null ? existingBusiness.getZipCode() : "00000");
        responseDto.setBusinessType(
                existingBusiness.getBusinessType() != null ? existingBusiness.getBusinessType() : "Unknown Type");
        responseDto.setBusinessSize(
                existingBusiness.getBusinessSize() != null ? existingBusiness.getBusinessSize() : "small");
        responseDto.setAddress(
                existingBusiness.getAddress() != null ? existingBusiness.getAddress() : "No Address Provided");
        responseDto.setGooglePlacesLink(
                existingBusiness.getGooglePlacesLink() != null ? existingBusiness.getGooglePlacesLink()
                        : "No Link Available");

        // Don't send the password back in the response
        responseDto.setPassword(null);

        return ResponseEntity.ok(responseDto);
    }

}
