package org.example.inventorysysteamvo1.service.serviceIMPL;
import org.example.inventorysysteamvo1.domain.Role;
import org.example.inventorysysteamvo1.domain.Repo.RoleRepository;
import org.example.inventorysysteamvo1.service.RoleService;
import org.example.inventorysysteamvo1.service.dto.RoleRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.RoleResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.RoleDtoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleDtoMapper dtoMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleDtoMapper dtoMapper) {
        this.roleRepository = roleRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO requestDTO) {
        // 1. Validation: Role names must be unique
        if (roleRepository.existsByRoleName(requestDTO.getRoleName())) {
            throw new IllegalArgumentException("Role with name '" + requestDTO.getRoleName() + "' already exists.");
        }

        // 2. Map & Save
        Role role = dtoMapper.toDomain(requestDTO);
        Role savedRole = roleRepository.save(role);
        return dtoMapper.toResponse(savedRole);
    }

    @Override
    public RoleResponseDTO updateRole(Long roleId, RoleRequestDTO requestDTO) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role with ID " + roleId + " not found."));

        // Validation: If changing name, ensure new name is unique
        if (!existingRole.getRoleName().equalsIgnoreCase(requestDTO.getRoleName())
                && roleRepository.existsByRoleName(requestDTO.getRoleName())) {
            throw new IllegalArgumentException("Role with name '" + requestDTO.getRoleName() + "' already exists.");
        }

        // Update Domain Model
        existingRole.updateRoleDetails(requestDTO.getRoleName(), requestDTO.getDescription());

        Role updatedRole = roleRepository.save(existingRole);
        return dtoMapper.toResponse(updatedRole);
    }

    @Override
    public RoleResponseDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role with ID " + roleId + " not found."));
        return dtoMapper.toResponse(role);
    }

    @Override
    public RoleResponseDTO getRoleByName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role with name " + roleName + " not found."));
        return dtoMapper.toResponse(role);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}