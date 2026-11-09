package org.example.inventorysysteamvo1.domain.Repo;
import org.example.inventorysysteamvo1.domain.Role;
import java.util.List;
import java.util.Optional;
public interface RoleRepository {
    Role save(Role role);

    Optional<Role> findById(Long roleId);

    Optional<Role> findByRoleName(String roleName);

    List<Role> findAll();

    boolean existsById(Long roleId);

    boolean existsByRoleName(String roleName);

    void deleteById(Long roleId);
}