package org.example.inventorysysteamvo1.infrastructure.repository;
import org.example.inventorysysteamvo1.infrastructure.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    // Optional: Add custom query methods if needed later, e.g.,
    // Optional<CategoryEntity> findByCategoryName(String categoryName);
    // Needed by the domain contract findByCategoryName()
    Optional<CategoryEntity> findByCategoryName(String categoryName);

    // Needed by the domain contract existsByCategoryName()
    boolean existsByCategoryName(String categoryName);

}









