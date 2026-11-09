package org.example.inventorysysteamvo1.service.dto.response;
import lombok.Data;
@Data
public class SupplierResponseDTO {
    private Long supplierId;
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String email;
    private String otherDetails;
}