package org.example.inventorysysteamvo1.service.mapper;
import org.example.inventorysysteamvo1.domain.Customer;
import org.example.inventorysysteamvo1.service.dto.CustomerRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CustomerResponseDTO;
import org.springframework.stereotype.Component;
@Component
public class CustomerDtoMapper {

    /**
     * Converts incoming HTTP Request DTO into your core Domain Model.
     * ID is null because it hasn't been saved to the database yet.
     */
    public Customer toDomain(CustomerRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        // Uses your existing Customer domain constructor
        return new Customer(
                null, // customerId is null for new creations
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getAddress(),
                requestDTO.getPhone(),
                requestDTO.getEmail(),
                requestDTO.getStaffId()
        );
    }

    /**
     * Converts your core Domain Model into an outgoing HTTP Response DTO.
     */
    public CustomerResponseDTO toResponse(Customer domain) {
        if (domain == null) {
            return null;
        }

        CustomerResponseDTO responseDTO = new CustomerResponseDTO();
        responseDTO.setCustomerId(domain.getCustomerId());
        responseDTO.setFirstName(domain.getFirstName());
        responseDTO.setLastName(domain.getLastName());
        responseDTO.setAddress(domain.getAddress());
        responseDTO.setPhone(domain.getPhone());
        responseDTO.setEmail(domain.getEmail());
        responseDTO.setStaffId(domain.getStaffId());

        return responseDTO;
    }
}