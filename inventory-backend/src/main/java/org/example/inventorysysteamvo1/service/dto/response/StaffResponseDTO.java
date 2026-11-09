package org.example.inventorysysteamvo1.service.dto.response;
import lombok.Data;
@Data
public class StaffResponseDTO {
    private Long staffId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String userName;
    private Long roleId;
    // ⛔ Password is NEVER in the response DTO
}