package org.example.inventorysysteamvo1.service;
import org.example.inventorysysteamvo1.service.dto.RoleRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.RoleResponseDTO;
import java.util.List;

public interface RoleService {

    // ── Create & Update ───────────────────────────────────────────────────

    RoleResponseDTO createRole(RoleRequestDTO requestDTO);

    RoleResponseDTO updateRole(Long roleId, RoleRequestDTO requestDTO);

    // ── Read ──────────────────────────────────────────────────────────────

    RoleResponseDTO getRoleById(Long roleId);

    RoleResponseDTO getRoleByName(String roleName);

    List<RoleResponseDTO> getAllRoles();

    // ── Delete ────────────────────────────────────────────────────────────

    void deleteRole(Long roleId);
}

