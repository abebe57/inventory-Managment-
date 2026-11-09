package org.example.inventorysysteamvo1.service.mapper;
import org.example.inventorysysteamvo1.domain.Role;
import org.example.inventorysysteamvo1.service.dto.RoleRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.RoleResponseDTO;
import org.springframework.stereotype.Component;
@Component
public class RoleDtoMapper {
    public Role toDomain(RoleRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return new Role(
                null, // ID is null for new creations
                requestDTO.getRoleName(),
                requestDTO.getDescription()
        );
    }
    public RoleResponseDTO toResponse(Role domain) {
        if (domain == null) {
            return null;
        }
        RoleResponseDTO responseDTO = new RoleResponseDTO();
        responseDTO.setRoleId(domain.getRoleId());
        responseDTO.setRoleName(domain.getRoleName());
        responseDTO.setDescription(domain.getDescription());
        return responseDTO;
    }
}