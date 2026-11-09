package org.example.inventorysysteamvo1.service.dto.response;
import lombok.Data;
@Data
public class CustomerResponseDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private Long staffId;
}


