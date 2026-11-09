package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.CustomerService;
import org.example.inventorysysteamvo1.service.dto.CustomerRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CustomerResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * POST /api/customers
     */
    @PostMapping
    @Operation(summary = "Register a new customer")
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO created = customerService.createCustomer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    /**
     * GET /api/customers/phone/{phone}
     */
    @GetMapping("/phone/{phone}")
    @Operation(summary = "Find a customer by phone number")
    public ResponseEntity<CustomerResponseDTO> getCustomerByPhone(
            @PathVariable String phone) {

        return ResponseEntity.ok(customerService.getCustomerByPhone(phone));
    }

    /**
     * GET /api/customers/email/{email}
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Find a customer by email address")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    /**
     * GET /api/customers
     */
    @GetMapping
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * GET /api/customers/staff/{staffId}
     */
    @GetMapping("/staff/{staffId}")
    @Operation(summary = "Get all customers managed by a specific staff member")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersByStaffId(
            @PathVariable Long staffId) {

        return ResponseEntity.ok(customerService.getCustomersByStaffId(staffId));
    }

    // ── Update ────────────────────────────────────────────────────────────

    /**
     * PUT /api/customers/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update customer details")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        return ResponseEntity.ok(customerService.updateCustomer(id, requestDTO));
    }

    // ── Delete ────────────────────────────────────────────────────────────

    /**
     * DELETE /api/customers/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer by ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}