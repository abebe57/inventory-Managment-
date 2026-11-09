package org.example.inventorysysteamvo1.infrastructure.security;

import org.example.inventorysysteamvo1.infrastructure.StaffEntity;
import org.example.inventorysysteamvo1.infrastructure.RoleEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.RoleJpaRepository;
import org.example.inventorysysteamvo1.infrastructure.repository.StaffJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StaffUserDetailsService implements UserDetailsService {

    private final StaffJpaRepository staffJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public StaffUserDetailsService(
            StaffJpaRepository staffJpaRepository,
            RoleJpaRepository roleJpaRepository) {

        this.staffJpaRepository = staffJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        StaffEntity staff = staffJpaRepository
                .findByUserName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Staff with username '"
                                        + username
                                        + "' was not found."
                        )
                );

        String authority = buildAuthority(staff);

        return User.withUsername(staff.getUserName())
                .password(staff.getPassword())
                .authorities(authority)
                .build();
    }

    private String buildAuthority(StaffEntity staff) {

        if (staff.getRoleId() == null) {
            return "ROLE_STAFF";
        }

        RoleEntity role = roleJpaRepository
                .findById(staff.getRoleId())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Role with ID "
                                        + staff.getRoleId()
                                        + " was not found."
                        )
                );

        String roleName = role.getRoleName();

        if (roleName == null || roleName.isBlank()) {
            return "ROLE_STAFF";
        }

        roleName = roleName.trim().toUpperCase();

        // Prevent ROLE_ROLE_ADMIN if the DB already contains ROLE_ADMIN
        if (roleName.startsWith("ROLE_")) {
            return roleName;
        }

        return "ROLE_" + roleName;
    }
}