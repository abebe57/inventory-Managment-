package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.SupplierService;
import org.example.inventorysysteamvo1.service.dto.SupplierRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.SupplierResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Suppliers", description = "Supplier management")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * POST /api/suppliers
     */
    @PostMapping
    @Operation(summary = "Register a new supplier")
    public ResponseEntity<SupplierResponseDTO> createSupplier(
            @Valid @RequestBody SupplierRequestDTO requestDTO) {

        SupplierResponseDTO created = supplierService.createSupplier(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * GET /api/suppliers/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a supplier by ID")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    /**
     * GET /api/suppliers/name/{name}
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Find a supplier by name")
    public ResponseEntity<SupplierResponseDTO> getSupplierByName(
            @PathVariable String name) {

        return ResponseEntity.ok(supplierService.getSupplierByName(name));
    }

    /**
     * GET /api/suppliers
     */
    @GetMapping
    @Operation(summary = "Get all suppliers")
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // ── Update ────────────────────────────────────────────────────────────

    /**
     * PUT /api/suppliers/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update supplier details")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO requestDTO) {

        return ResponseEntity.ok(supplierService.updateSupplier(id, requestDTO));
    }

    // ── Delete ────────────────────────────────────────────────────────────

    /**
     * DELETE /api/suppliers/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplier by ID")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}