package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.Plans;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.PlansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private PlansRepository plansRepository;

    public BusinessDto createBusiness(BusinessDto businessDto) {
        Business business = mapToEntity(businessDto);
        business.setCreatedDate(LocalDateTime.now());
        Business savedBusiness = businessRepository.save(business);
        return mapToDto(savedBusiness);
    }

    public BusinessDto getBusinessById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));
        return mapToDto(business);
    }

    public List<BusinessDto> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public BusinessDto updateBusiness(Long businessId, BusinessDto businessDto) {
        Business existingBusiness = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));

        existingBusiness.setRegistrationNumber(businessDto.getRegistrationNumber());
        existingBusiness.setBusinessName(businessDto.getBusinessName());
        existingBusiness.setOpeningTime(parseStringToLocalTime(businessDto.getOpeningTime()));
        existingBusiness.setClosingTime(parseStringToLocalTime(businessDto.getClosingTime()));
        existingBusiness.setPhoneNumber(businessDto.getPhoneNumber());
        existingBusiness.setEmail(businessDto.getEmail());
        existingBusiness.setZipCode(businessDto.getZipCode());
        existingBusiness.setBusinessType(businessDto.getBusinessType());
        existingBusiness.setBusinessSize(businessDto.getBusinessSize());
        existingBusiness.setPassword(businessDto.getPassword());
        existingBusiness.setAddress(businessDto.getAddress());
        existingBusiness.setGooglePlacesLink(businessDto.getGooglePlacesLink());

        if (businessDto.getPlanId() != null) {
            Plans plan = plansRepository.findById(businessDto.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + businessDto.getPlanId()));
            existingBusiness.setPlan(plan);
        }

        if (businessDto.getDateOfPlanUpdated() != null) {
            existingBusiness.setDateOfPlanUpdated(LocalDateTime.parse(businessDto.getDateOfPlanUpdated()));
        }

        Business updatedBusiness = businessRepository.save(existingBusiness);
        return mapToDto(updatedBusiness);
    }

    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new RuntimeException("Business not found with ID: " + businessId);
        }
        businessRepository.deleteById(businessId);
    }

    private BusinessDto mapToDto(Business business) {
        BusinessDto dto = new BusinessDto();
        dto.setBusinessId(business.getBusinessId());
        dto.setRegistrationNumber(business.getRegistrationNumber());
        dto.setBusinessName(business.getBusinessName());
        dto.setOpeningTime(business.getOpeningTime() == null ? null : business.getOpeningTime().toString());
        dto.setClosingTime(business.getClosingTime() == null ? null : business.getClosingTime().toString());
        dto.setPhoneNumber(business.getPhoneNumber());
        dto.setEmail(business.getEmail());
        dto.setZipCode(business.getZipCode());
        dto.setPassword(business.getPassword());
        dto.setBusinessType(business.getBusinessType());
        dto.setBusinessSize(business.getBusinessSize());
        dto.setAddress(business.getAddress());
        dto.setGooglePlacesLink(business.getGooglePlacesLink());

        int ageOfAccount = (business.getCreatedDate() != null) ? 
            (LocalDateTime.now().getYear() - business.getCreatedDate().getYear()) : 0;
        dto.setAgeOfAccount(ageOfAccount);

        dto.setPlanId(business.getPlan() != null ? business.getPlan().getPlanId() : null);
        dto.setDateOfPlanUpdated(business.getDateOfPlanUpdated() != null
                ? business.getDateOfPlanUpdated().toString() : null);

        dto.setPlanName(business.getPlan() != null ? business.getPlan().getPlanName() : null);

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
        business.setBusinessType(dto.getBusinessType());

        if (dto.getBusinessSize() == null) {
            business.setBusinessSize("small");
        } else {
            business.setBusinessSize(dto.getBusinessSize());
        }

        business.setAddress(dto.getAddress());
        business.setGooglePlacesLink(dto.getGooglePlacesLink());

        if (dto.getPlanId() != null) {
            Plans plan = plansRepository.findById(dto.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + dto.getPlanId()));
            business.setPlan(plan);
        }

        if (dto.getDateOfPlanUpdated() != null) {
            business.setDateOfPlanUpdated(LocalDateTime.parse(dto.getDateOfPlanUpdated()));
        }

        return business;
    }

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
