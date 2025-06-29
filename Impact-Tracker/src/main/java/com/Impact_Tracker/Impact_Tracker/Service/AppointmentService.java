package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.AppointmentDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.Customer;
import com.Impact_Tracker.Impact_Tracker.Repo.AppointmentRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AppointmentConflictService conflictService;

    // CREATE
 public AppointmentDto createAppointment(AppointmentDto dto) {
        // 2) Check for conflicts FIRST
        conflictService.checkForConflict(
            dto.getBusinessId(),
            dto.getAppointmentDate(),
            dto.getAppointmentStartTime(),
            dto.getAppointmentEndTime(),
            null  // No current appointment ID, since this is new
        );

        // Then proceed to create & save
        Appointment appointment = mapToEntity(dto);
        Appointment saved = appointmentRepository.save(appointment);
        return mapToDto(saved);
    }
    // READ (Get by ID)
    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        return mapToDto(appointment);
    }

    // READ (Get all)
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // UPDATE
 public AppointmentDto updateAppointment(Long appointmentId, AppointmentDto dto) {
    Appointment existing = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

    // 1) Conflict check
    conflictService.checkForConflict(
        dto.getBusinessId() != null ? dto.getBusinessId() : existing.getBusiness().getBusinessId(),
        dto.getAppointmentDate() != null ? dto.getAppointmentDate() : existing.getAppointmentDate(),
        dto.getAppointmentStartTime() != null ? dto.getAppointmentStartTime() : existing.getAppointmentStartTime(),
        dto.getAppointmentEndTime() != null ? dto.getAppointmentEndTime() : existing.getAppointmentEndTime(),
        appointmentId
    );

    // 2) Update fields
    if (dto.getAppointmentDate() != null) {
        existing.setAppointmentDate(dto.getAppointmentDate());
    }
    if (dto.getAppointmentStartTime() != null) {
        existing.setAppointmentStartTime(dto.getAppointmentStartTime());
    }
    if (dto.getAppointmentEndTime() != null) {
        existing.setAppointmentEndTime(dto.getAppointmentEndTime());
    }
    if (dto.getAppointmentType() != null) {
        existing.setAppointmentType(dto.getAppointmentType());
    }
    if (dto.getAppointmentStatus() != null) {
        existing.setAppointmentStatus(dto.getAppointmentStatus());
    }

    existing.setAppointmentRescheduled(dto.getAppointmentRescheduled());

    // If the user wants to mark it specifically as "rescheduled", set the flag
    existing.setAppointmentRescheduled("true");

    // If businessId or customerId changed, update those too (optional).
    if (dto.getBusinessId() != null) {
        Business business = businessRepository.findById(dto.getBusinessId())
            .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));
        existing.setBusiness(business);
    }
    if (dto.getCustomerId() != null) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
        existing.setCustomer(customer);
    }

    Appointment updated = appointmentRepository.save(existing);
    return mapToDto(updated);
}



    public List<AppointmentDto> getAppointmentsByBusinessId(Long businessId) {
        List<Appointment> appointments = appointmentRepository.findByBusiness_BusinessId(businessId);
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Get all appointments associated with a business on a specific date
    public List<AppointmentDto> getAppointmentsByBusinessIdAndDate(Long businessId, String date) {
        List<Appointment> appointments = appointmentRepository.findByBusinessIdAndDate(businessId, date);
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    // DELETE
    public void deleteAppointment(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new RuntimeException("Appointment not found with ID: " + appointmentId);
        }
        appointmentRepository.deleteById(appointmentId);
    }

    // ================ Helper Methods =================

    private Appointment mapToEntity(AppointmentDto dto) {
        Appointment appointment = new Appointment();

        // ID is auto-generated, so we typically don't set appointmentId
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentStartTime(dto.getAppointmentStartTime());
        appointment.setAppointmentEndTime(dto.getAppointmentEndTime());
        appointment.setAppointmentType(dto.getAppointmentType());
        appointment.setAppointmentStatus(dto.getAppointmentStatus());


        appointment.setAppointmentRescheduled(dto.getAppointmentRescheduled());

        // Look up the Business and Customer from the IDs in the DTO
        if (dto.getBusinessId() != null) {
            Business business = businessRepository.findById(dto.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));
            appointment.setBusiness(business);
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
            appointment.setCustomer(customer);
        }

        return appointment;
    }

private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentStartTime(appointment.getAppointmentStartTime());
        dto.setAppointmentEndTime(appointment.getAppointmentEndTime());
        dto.setAppointmentType(appointment.getAppointmentType());
        dto.setAppointmentStatus(appointment.getAppointmentStatus());
        dto.setAppointmentRescheduled(appointment.getAppointmentRescheduled());

        if (appointment.getBusiness() != null) {
            dto.setBusinessId(appointment.getBusiness().getBusinessId());
        }
        if (appointment.getCustomer() != null) {
            dto.setCustomerId(appointment.getCustomer().getCustomerId());
            dto.setCustomerName( appointment.getCustomer().getCustName() );
        }
        return dto;
    }
}
