package org.example.inventorysysteamvo1.service;

import org.example.inventorysysteamvo1.service.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Authenticates a staff member by username and raw password.
     * Returns a signed JWT wrapped in an {@link AuthResponse} on success.
     * Throws {@link org.springframework.security.core.AuthenticationException}
     * (e.g. {@code BadCredentialsException}) on failure — Spring Security
     * will convert that to an HTTP 401 automatically.
     *
     * @param username the staff member's username
     * @param rawPassword the plain-text password submitted by the client
     * @return a response containing the JWT token and basic profile info
     */
    AuthResponse login(String username, String rawPassword);
}