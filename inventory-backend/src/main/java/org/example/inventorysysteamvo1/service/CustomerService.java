package org.example.inventorysysteamvo1.service;
import org.example.inventorysysteamvo1.service.dto.CustomerRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CustomerResponseDTO;
import java.util.List;

public interface CustomerService {

    // ── Create & Update ───────────────────────────────────────────────────

    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO requestDTO);

    // ── Read ──────────────────────────────────────────────────────────────

    CustomerResponseDTO getCustomerById(Long customerId);

    CustomerResponseDTO getCustomerByPhone(String phone);

    CustomerResponseDTO getCustomerByEmail(String email);

    List<CustomerResponseDTO> getAllCustomers();

    List<CustomerResponseDTO> getCustomersByStaffId(Long staffId);

    // ── Delete ────────────────────────────────────────────────────────────

    void deleteCustomer(Long customerId);
}