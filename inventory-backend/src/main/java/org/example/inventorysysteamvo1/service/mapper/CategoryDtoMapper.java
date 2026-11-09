package org.example.inventorysysteamvo1.service.mapper;
import org.example.inventorysysteamvo1.service.dto.CategoryRequestDTO;
import org.example.inventorysysteamvo1.domain.Category;
import org.example.inventorysysteamvo1.service.dto.response.CategoryResponseDTO;
import org.springframework.stereotype.Component;
@Component
public class CategoryDtoMapper {

    /**
     * Converts incoming HTTP Request DTO into your core Domain Model.
     * ID is null because it hasn't been saved to the database yet.
     */
    public Category toDomain(CategoryRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return new Category(
                null, // ID is null for new creations
                requestDTO.getCategoryName(),
                requestDTO.getDescription()
        );
    }

    /**
     * Converts your core Domain Model into an outgoing HTTP Response DTO.
     */
    public CategoryResponseDTO toResponse(Category domain) {
        if (domain == null) {
            return null;
        }

        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setCategoryId(domain.getCategoryId());
        responseDTO.setCategoryName(domain.getCategoryName());
        responseDTO.setDescription(domain.getDescription());

        return responseDTO;
    }
}
