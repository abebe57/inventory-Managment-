package org.example.inventorysysteamvo1.service.mapper;

import org.example.inventorysysteamvo1.domain.Supplier;
import org.example.inventorysysteamvo1.service.dto.SupplierRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.SupplierResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SupplierDtoMapper {

    public Supplier toDomain(SupplierRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return new Supplier(
                null,
                requestDTO.getName(),
                requestDTO.getAddress(),
                requestDTO.getPhone(),
                requestDTO.getFax(),
                requestDTO.getEmail(),
                requestDTO.getOtherDetails()
        );
    }

    public SupplierResponseDTO toResponse(Supplier domain) {
        if (domain == null) {
            return null;
        }

        SupplierResponseDTO responseDTO = new SupplierResponseDTO();
        responseDTO.setSupplierId(domain.getSupplierId());
        responseDTO.setName(domain.getName());
        responseDTO.setAddress(domain.getAddress());
        responseDTO.setPhone(domain.getPhone());
        responseDTO.setFax(domain.getFax());
        responseDTO.setEmail(domain.getEmail());
        responseDTO.setOtherDetails(domain.getOtherDetails());
        return responseDTO;
    }
}
