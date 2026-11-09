package org.example.inventorysysteamvo1.service.mapper;

import org.example.inventorysysteamvo1.domain.Product;
import org.example.inventorysysteamvo1.service.dto.ProductRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {

    public Product toDomain(ProductRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return new Product(
                null,
                requestDTO.getProductName(),
                requestDTO.getProductPrice(),
                requestDTO.getProductQuantity(),
                requestDTO.getSupplierId(),
                requestDTO.getCategoryId()
        )
                .withDescription(requestDTO.getProductDescription())
                .withUnit(requestDTO.getProductUnit())
                .withOtherDetails(requestDTO.getOtherDetails());
    }

    public ProductResponseDTO toResponse(Product domain) {
        if (domain == null) {
            return null;
        }

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductId(domain.getProductId());
        responseDTO.setProductName(domain.getProductName());
        responseDTO.setProductDescription(domain.getProductDescription());
        responseDTO.setProductUnit(domain.getProductUnit());
        responseDTO.setProductPrice(domain.getProductPrice());
        responseDTO.setProductQuantity(domain.getProductQuantity());
        responseDTO.setProductStatus(domain.getProductStatus());
        responseDTO.setOtherDetails(domain.getOtherDetails());
        responseDTO.setSupplierId(domain.getSupplierId());
        responseDTO.setCategoryId(domain.getCategoryId());
        return responseDTO;
    }
}
