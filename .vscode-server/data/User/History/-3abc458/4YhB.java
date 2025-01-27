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
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public BusinessDto createBusiness(BusinessDto businessDto) {
        Business business = mapToEntity(businessDto);
        Business savedBusiness = businessRepository.save(business);
        return mapToDto(savedBusiness);
    }

    @Override
    public BusinessDto getBusinessById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException(
                        "Business not found with ID: " + businessId));
        return mapToDto(business);
    }

    @Override
    public List<BusinessDto> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
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

        Business updatedBusiness = businessRepository.save(existingBusiness);
        return mapToDto(updatedBusiness);
    }

    @Override
    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new RuntimeException("Business not found with ID: " + businessId);
        }
        businessRepository.deleteById(businessId);
    }

    // ---------------------------------------
    // Helper methods for Entity <-> DTO
    // ---------------------------------------
    private BusinessDto mapToDto(Business business) {
        BusinessDto dto = new BusinessDto();
        dto.setBusinessId(business.getBusinessId());
        dto.setRegistrationNumber(business.getRegistrationNumber());
        dto.setBusinessName(business.getBusinessName());
        // Convert LocalTime to String, e.g. "09:00"
        dto.setOpeningTime(business.getOpeningTime() == null
                ? null : business.getOpeningTime().toString());
        dto.setClosingTime(business.getClosingTime() == null
                ? null : business.getClosingTime().toString());
        dto.setPhoneNumber(business.getPhoneNumber());
        dto.setEmail(business.getEmail());
        dto.setZipCode(business.getZipCode());
        return dto;
    }

    private Business mapToEntity(BusinessDto dto) {
        Business business = new Business();
        // ID is auto-generated, so no setBusinessId().
        business.setRegistrationNumber(dto.getRegistrationNumber());
        business.setBusinessName(dto.getBusinessName());
        business.setOpeningTime(parseStringToLocalTime(dto.getOpeningTime()));
        business.setClosingTime(parseStringToLocalTime(dto.getClosingTime()));
        business.setPhoneNumber(dto.getPhoneNumber());
        business.setEmail(dto.getEmail());
        business.setZipCode(dto.getZipCode());
        return business;
    }

    private LocalTime parseStringToLocalTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeString); // Expects format "HH:mm"
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid time format. Please use HH:mm. Error: " + e.getMessage());
        }
    }
}
