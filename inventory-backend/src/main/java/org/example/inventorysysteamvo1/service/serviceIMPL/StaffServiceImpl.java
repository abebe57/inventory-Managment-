package org.example.inventorysysteamvo1.service.serviceIMPL;
import org.example.inventorysysteamvo1.domain.Staff;
import org.example.inventorysysteamvo1.domain.Repo.RoleRepository;
import org.example.inventorysysteamvo1.domain.Repo.StaffRepository;
import org.example.inventorysysteamvo1.service.StaffService;
import org.example.inventorysysteamvo1.service.dto.StaffChangePasswordRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StaffUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.StaffResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.StaffDtoMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final StaffDtoMapper dtoMapper;

    // Spring Security BCrypt password encoder
    // Make sure you have a PasswordEncoder @Bean in your security config!
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(StaffRepository staffRepository,
                            RoleRepository roleRepository,
                            StaffDtoMapper dtoMapper,
                            PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.dtoMapper = dtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO requestDTO) {
        // 1. Validate username is unique
        if (staffRepository.existsByUsername(requestDTO.getUserName())) {
            throw new IllegalArgumentException("Username '" + requestDTO.getUserName() + "' is already taken.");
        }

        // 2. Validate that the Role exists
        if (!roleRepository.existsById(requestDTO.getRoleId())) {
            throw new IllegalArgumentException("Role with ID " + requestDTO.getRoleId() + " does not exist.");
        }

        // 3. Validate raw password strength using the domain's static method
        Staff.validatePasswordStrength(requestDTO.getRawPassword());

        // 4. Hash the password — ONLY the hash goes into the domain/database
        String hashedPassword = passwordEncoder.encode(requestDTO.getRawPassword());

        // 5. Map DTO -> Domain Model (passing the hash, not the raw password)
        Staff staff = dtoMapper.toDomain(requestDTO, hashedPassword);

        // 6. Save and return
        Staff savedStaff = staffRepository.save(staff);
        return dtoMapper.toResponse(savedStaff);
    }

    @Override
    public StaffResponseDTO updateStaff(Long staffId, StaffUpdateRequestDTO requestDTO) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff with ID " + staffId + " not found."));

        // Validate Role exists if it is being changed
        if (!existingStaff.getRoleId().equals(requestDTO.getRoleId())
                && !roleRepository.existsById(requestDTO.getRoleId())) {
            throw new IllegalArgumentException("Role with ID " + requestDTO.getRoleId() + " does not exist.");
        }

        // Update using domain business methods — password is untouched here!
        existingStaff.updateName(requestDTO.getFirstName(), requestDTO.getLastName());
        existingStaff.updateContactInfo(requestDTO.getPhone(), requestDTO.getAddress(), requestDTO.getEmail());

        // If role has changed, use the domain's assignRole() method
        Staff staffToSave = existingStaff;
        if (!existingStaff.getRoleId().equals(requestDTO.getRoleId())) {
            staffToSave = existingStaff.assignRole(requestDTO.getRoleId());
        }

        Staff updatedStaff = staffRepository.save(staffToSave);
        return dtoMapper.toResponse(updatedStaff);
    }

    @Override
    public void changePassword(Long staffId, StaffChangePasswordRequestDTO requestDTO) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff with ID " + staffId + " not found."));

        // 1. Verify the current password is correct before allowing the change
        if (!passwordEncoder.matches(requestDTO.getCurrentRawPassword(), existingStaff.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // 2. Validate the strength of the new password
        Staff.validatePasswordStrength(requestDTO.getNewRawPassword());

        // 3. Hash the new password
        String newHashedPassword = passwordEncoder.encode(requestDTO.getNewRawPassword());

        // 4. Update via domain method — only accepts a hashed value
        existingStaff.changePassword(newHashedPassword);

        staffRepository.save(existingStaff);
    }

    @Override
    public StaffResponseDTO getStaffById(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff with ID " + staffId + " not found."));
        return dtoMapper.toResponse(staff);
    }

    @Override
    public StaffResponseDTO getStaffByUsername(String username) {
        Staff staff = staffRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Staff with username " + username + " not found."));
        return dtoMapper.toResponse(staff);
    }

    @Override
    public List<StaffResponseDTO> getAllStaff() {
        return staffRepository.findAll()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaff(Long staffId) {
        staffRepository.deleteById(staffId);
    }
}