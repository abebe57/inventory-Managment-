package org.example.inventorysysteamvo1.service.serviceIMPL;

import org.example.inventorysysteamvo1.infrastructure.StaffEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.StaffJpaRepository;
import org.example.inventorysysteamvo1.infrastructure.security.JwtService;
import org.example.inventorysysteamvo1.service.AuthService;
import org.example.inventorysysteamvo1.service.dto.response.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Handles authentication by delegating credential verification to Spring
 * Security's AuthenticationManager (which internally uses BCrypt + the
 * StaffUserDetailsService), then issues a JWT on success.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager  authenticationManager;
    private final JwtService             jwtService;
    private final StaffJpaRepository     staffJpaRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           StaffJpaRepository staffJpaRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService            = jwtService;
        this.staffJpaRepository    = staffJpaRepository;
    }

    @Override
    public AuthResponse login(String username, String rawPassword) {

        // 1. Authenticate — throws BadCredentialsException / LockedException etc.
        //    if the credentials are wrong. Spring Security handles the 401.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword));

        // 2. At this point authentication succeeded — load the staff record
        //    from the JPA layer to populate the response body.
        StaffEntity staff = staffJpaRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Staff not found after authentication: " + username));

        // 3. Generate and return the signed JWT
        String token = jwtService.generateToken(
                staff.getUserName(), staff.getStaffId(), staff.getRoleId());

        return new AuthResponse(token, "Bearer", staff.getUserName(),
                staff.getStaffId(), staff.getRoleId());
    }
}