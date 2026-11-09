package org.example.inventorysysteamvo1.service.mapper;
import org.example.inventorysysteamvo1.domain.Staff;
import org.example.inventorysysteamvo1.service.dto.StaffRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.StaffResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class StaffDtoMapper {

    /**
     * Converts incoming Request DTO into your core Domain Model.
     * IMPORTANT: This method expects an ALREADY HASHED password.
     * The service layer is responsible for hashing BEFORE calling this.
     */
    public Staff toDomain(StaffRequestDTO requestDTO, String hashedPassword) {
        if (requestDTO == null) {
            return null;
        }

        Staff staff = new Staff(
                null, // ID is null for new creations
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getEmail(),
                requestDTO.getUserName(),
                hashedPassword, // ✅ Always hashed, never plaintext
                requestDTO.getRoleId()
        );

        // Set the optional contact fields using domain methods
        staff.updateContactInfo(requestDTO.getPhone(), requestDTO.getAddress(), null);
        return staff;
    }

    /**
     * Converts your core Domain Model into an outgoing HTTP Response DTO.
     * Password is NEVER mapped to the response.
     */
    public StaffResponseDTO toResponse(Staff domain) {
        if (domain == null) {
            return null;
        }

        StaffResponseDTO responseDTO = new StaffResponseDTO();
        responseDTO.setStaffId(domain.getStaffId());
        responseDTO.setFirstName(domain.getFirstName());
        responseDTO.setLastName(domain.getLastName());
        responseDTO.setFullName(domain.getFullName());
        responseDTO.setAddress(domain.getAddress());
        responseDTO.setPhone(domain.getPhone());
        responseDTO.setEmail(domain.getEmail());
        responseDTO.setUserName(domain.getUserName());
        responseDTO.setRoleId(domain.getRoleId());
        // ⛔ Password intentionally excluded

        return responseDTO;
    }
}
