package org.example.inventorysysteamvo1.service;
import org.example.inventorysysteamvo1.service.dto.StaffChangePasswordRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.StaffResponseDTO;
import java.util.List;

public interface StaffService {

    // ── Create & Update ───────────────────────────────────────────────────

    StaffResponseDTO createStaff(StaffRequestDTO requestDTO);

    StaffResponseDTO updateStaff(Long staffId, StaffUpdateRequestDTO requestDTO);

    // Dedicated, separate endpoint for changing passwords
    void changePassword(Long staffId, StaffChangePasswordRequestDTO requestDTO);

    // ── Read ──────────────────────────────────────────────────────────────

    StaffResponseDTO getStaffById(Long staffId);

    StaffResponseDTO getStaffByUsername(String username);

    List<StaffResponseDTO> getAllStaff();

    // ── Delete ────────────────────────────────────────────────────────────

    void deleteStaff(Long staffId);
}
