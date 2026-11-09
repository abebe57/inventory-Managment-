package org.example.inventorysysteamvo1.service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class StaffChangePasswordRequestDTO {

    @NotBlank(message = "Current password is required")
    private String currentRawPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newRawPassword;
}

