package org.example.inventorysysteamvo1.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Runs once per request, before Spring Security's own filters.
 *
 * Flow:
 *   1. Read the "Authorization: Bearer <token>" header.
 *   2. Extract the username from the token.
 *   3. Load the UserDetails from the database.
 *   4. Validate the token (signature + expiry + username match).
 *   5. If valid → populate the SecurityContext so the request is authenticated.
 *
 * If the header is absent, or the token is invalid/expired, the filter
 * simply does nothing — downstream security rules then reject the request.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX      = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService              jwtService;
    private final StaffUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService,
                     StaffUserDetailsService userDetailsService) {
        this.jwtService         = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest  request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain         filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Skip if there is no Bearer token
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token    = authHeader.substring(BEARER_PREFIX.length());
            String username = jwtService.extractUsername(token);

            // Only proceed if we got a username AND the context is not yet authenticated
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, username)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,                        // credentials — not needed after auth
                                    userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception ex) {
            // Invalid token (expired, tampered, etc.) — do NOT set the context;
            // the request will be rejected by the security filter chain as unauthenticated.
            // Avoid leaking error details in the log at WARN level or above.
            logger.debug("JWT validation failed: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}