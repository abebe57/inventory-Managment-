package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.StaffService;
import org.example.inventorysysteamvo1.service.dto.StaffChangePasswordRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.StaffResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "Staff account management")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * POST /api/staff
     * Creates a new staff account. The raw password in the request body is
     * validated and hashed by the service layer before storage.
     */
    @PostMapping
    @Operation(summary = "Create a new staff account")
    public ResponseEntity<StaffResponseDTO> createStaff(
            @Valid @RequestBody StaffRequestDTO requestDTO) {

        StaffResponseDTO created = staffService.createStaff(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * GET /api/staff/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a staff member by ID")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    /**
     * GET /api/staff/username/{username}
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Find a staff member by username")
    public ResponseEntity<StaffResponseDTO> getStaffByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(staffService.getStaffByUsername(username));
    }

    /**
     * GET /api/staff
     */
    @GetMapping
    @Operation(summary = "Get all staff members")
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // ── Update ────────────────────────────────────────────────────────────

    /**
     * PUT /api/staff/{id}
     * Updates name, contact info, and role. Password is NOT changed here.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a staff member's profile (excluding password)")
    public ResponseEntity<StaffResponseDTO> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody StaffUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(staffService.updateStaff(id, requestDTO));
    }

    /**
     * PATCH /api/staff/{id}/password
     *
     * Dedicated, separate endpoint for password changes.
     * Requires current password verification before accepting the new one.
     */
    @PatchMapping("/{id}/password")
    @Operation(summary = "Change a staff member's password (requires current password)")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody StaffChangePasswordRequestDTO requestDTO) {

        staffService.changePassword(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    // ── Delete ────────────────────────────────────────────────────────────

    /**
     * DELETE /api/staff/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a staff account by ID")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}