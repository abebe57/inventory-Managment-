package org.example.inventorysysteamvo1.domain.Repo;
import org.example.inventorysysteamvo1.domain.Staff;
import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    // Save a new staff or update an existing staff
    Staff save(Staff staff);

    // Find staff by ID
    Optional<Staff> findById(Long staffId);

    // Find staff by username
    Optional<Staff> findByUsername(String username);

    // Get all staff
    List<Staff> findAll();

    // Check whether staff exists
    boolean existsById(Long staffId);

    // Check whether username already exists
    boolean existsByUsername(String username);

    // Delete staff by ID
    void deleteById(Long staffId);
}