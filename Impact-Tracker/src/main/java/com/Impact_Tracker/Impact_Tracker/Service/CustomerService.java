package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.CustomerDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Customer;
import com.Impact_Tracker.Impact_Tracker.Repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * CREATE a new customer
     */
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = mapToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDto(savedCustomer);
    }

    /**
     * READ a single customer by ID
     */
    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found with ID: " + customerId));
        return mapToDto(customer);
    }

    /**
     * READ all customers
     */
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * UPDATE a customer by ID
     */
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found with ID: " + customerId));

        // Update fields
        existingCustomer.setCustName(customerDto.getCustName());
        existingCustomer.setPhNo(customerDto.getPhNo());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setZipCode(customerDto.getZipCode());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapToDto(updatedCustomer);
    }

    /**
     * DELETE a customer by ID
     */
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    // ---------------------------------------------------------------
    // Helper methods for converting between DTO and Entity
    // ---------------------------------------------------------------
    private CustomerDto mapToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCustName(customer.getCustName());
        dto.setPhNo(customer.getPhNo());
        dto.setEmail(customer.getEmail());
        dto.setZipCode(customer.getZipCode());
        return dto;
    }

    private Customer mapToEntity(CustomerDto dto) {
        Customer customer = new Customer();
        // ID is auto-generated, so we don't set it here.
        customer.setCustName(dto.getCustName());
        customer.setPhNo(dto.getPhNo());
        customer.setEmail(dto.getEmail());
        customer.setZipCode(dto.getZipCode());
        return customer;
    }
}
