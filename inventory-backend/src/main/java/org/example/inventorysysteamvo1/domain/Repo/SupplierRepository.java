package org.example.inventorysysteamvo1.domain.Repo;
import org.example.inventorysysteamvo1.domain.Supplier;
import java.util.List;
import java.util.Optional;
public interface SupplierRepository {
    Supplier save(Supplier supplier);

    Optional<Supplier> findById(Long supplierId);

    Optional<Supplier> findByName(String name);

    List<Supplier> findAll();

    boolean existsById(Long supplierId);

    boolean existsByName(String name);

    void deleteById(Long supplierId);
}