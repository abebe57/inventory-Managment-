package org.example.inventorysysteamvo1.service.serviceIMPL;
import org.example.inventorysysteamvo1.domain.Category;
import org.example.inventorysysteamvo1.domain.Repo.CategoryRepository;
import org.example.inventorysysteamvo1.service.CategoryService;
import org.example.inventorysysteamvo1.service.dto.CategoryRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CategoryResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.CategoryDtoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {

    // Notice we inject the DOMAIN repository, not the JPA repository!
    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper dtoMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryDtoMapper dtoMapper) {
        this.categoryRepository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // 1. Business Logic Rule: No duplicate category names
        if (categoryRepository.existsByCategoryName(requestDTO.getCategoryName())) {
            throw new IllegalArgumentException("Category with name '" + requestDTO.getCategoryName() + "' already exists.");
        }

        // 2. Map DTO -> Domain Model
        Category category = dtoMapper.toDomain(requestDTO);

        // 3. Save via Domain Repository
        Category savedCategory = categoryRepository.save(category);

        // 4. Map Domain Model -> Response DTO
        return dtoMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO requestDTO) {
        // 1. Fetch the existing domain object (or throw error if not found)
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + categoryId + " not found."));

        // 2. Business Logic Rule: If they are changing the name, ensure the new name isn't taken
        if (!existingCategory.getCategoryName().equalsIgnoreCase(requestDTO.getCategoryName())
                && categoryRepository.existsByCategoryName(requestDTO.getCategoryName())) {
            throw new IllegalArgumentException("Category with name '" + requestDTO.getCategoryName() + "' already exists.");
        }

        // 3. Update the existing domain object (using the methods you built inside your domain class!)
        existingCategory.updateCategory(requestDTO.getCategoryName(), requestDTO.getDescription());

        // 4. Save and return mapped response
        Category updatedCategory = categoryRepository.save(existingCategory);
        return dtoMapper.toResponse(updatedCategory);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + categoryId + " not found."));

        return dtoMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        // The repository adapter handles the "exists" check and throws if not found
        categoryRepository.deleteById(categoryId);
    }
}
