package org.example.inventorysysteamvo1.domain.Repo;

import org.example.inventorysysteamvo1.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long customerId);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByStaffId(Long staffId);

    List<Customer> findAll();

    boolean existsById(Long customerId);

    boolean existsByPhone(String phone);

    void deleteById(Long customerId);
}