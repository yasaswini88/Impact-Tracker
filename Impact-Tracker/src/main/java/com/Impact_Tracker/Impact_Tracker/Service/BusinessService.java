package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * Create a new Business record
     */
    public BusinessDto createBusiness(BusinessDto businessDto) {
        Business business = mapToEntity(businessDto);
        Business savedBusiness = businessRepository.save(business);
        return mapToDto(savedBusiness);
    }

    /**
     * Get a Business by its ID
     */
    public BusinessDto getBusinessById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException(
                        "Business not found with ID: " + businessId));
        return mapToDto(business);
    }

    /**
     * Get all Businesses
     */
    public List<BusinessDto> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Update a Business by ID
     */
    public BusinessDto updateBusiness(Long businessId, BusinessDto businessDto) {
        Business existingBusiness = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException(
                        "Business not found with ID: " + businessId));

        // Update fields
        existingBusiness.setRegistrationNumber(businessDto.getRegistrationNumber());
        existingBusiness.setBusinessName(businessDto.getBusinessName());
        existingBusiness.setOpeningTime(parseStringToLocalTime(businessDto.getOpeningTime()));
        existingBusiness.setClosingTime(parseStringToLocalTime(businessDto.getClosingTime()));
        existingBusiness.setPhoneNumber(businessDto.getPhoneNumber());
        existingBusiness.setEmail(businessDto.getEmail());
        existingBusiness.setZipCode(businessDto.getZipCode());

        existingBusiness.setPassword(businessDto.getPassword());
        Business updatedBusiness = businessRepository.save(existingBusiness);
        return mapToDto(updatedBusiness);
    }

    /**
     * Delete a Business by ID
     */
    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new RuntimeException("Business not found with ID: " + businessId);
        }
        businessRepository.deleteById(businessId);
    }

    // ---------------------------------------------------------------
    // Helper methods for converting between DTO and Entity
    // ---------------------------------------------------------------
    private BusinessDto mapToDto(Business business) {
        BusinessDto dto = new BusinessDto();
        dto.setBusinessId(business.getBusinessId());
        dto.setRegistrationNumber(business.getRegistrationNumber());
        dto.setBusinessName(business.getBusinessName());
        dto.setOpeningTime(business.getOpeningTime() == null 
                ? null : business.getOpeningTime().toString());
        dto.setClosingTime(business.getClosingTime() == null 
                ? null : business.getClosingTime().toString());
        dto.setPhoneNumber(business.getPhoneNumber());
        dto.setEmail(business.getEmail());
        dto.setZipCode(business.getZipCode());
        dto.setPassword(business.getPassword()); 

        return dto;
    }

    private Business mapToEntity(BusinessDto dto) {
        Business business = new Business();
        business.setRegistrationNumber(dto.getRegistrationNumber());
        business.setBusinessName(dto.getBusinessName());
        business.setOpeningTime(parseStringToLocalTime(dto.getOpeningTime()));
        business.setClosingTime(parseStringToLocalTime(dto.getClosingTime()));
        business.setPhoneNumber(dto.getPhoneNumber());
        business.setEmail(dto.getEmail());
        business.setZipCode(dto.getZipCode());

        business.setPassword(dto.getPassword());

        return business;
    }

    /**
     * Convert a "HH:mm" string to LocalTime
     */
    private LocalTime parseStringToLocalTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeString);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid time format. Please use HH:mm. Error: " + e.getMessage());
        }
    }
}
