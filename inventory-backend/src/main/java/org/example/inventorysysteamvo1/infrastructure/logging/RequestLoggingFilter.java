package org.example.inventorysysteamvo1.infrastructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that logs one line per HTTP request:
 *
 *   [192.168.1.1] POST /api/staff → 201 (34 ms)
 *
 * Runs at the very start of the filter chain (@Order(1)) so it captures
 * the total wall-clock time including Spring Security and serialisation.
 *
 * Sensitive paths (containing "password" or "login") still log the method
 * and status but suppress the body — the body is never read here anyway,
 * so no risk of leaking passwords.
 */
@Component
@Order(1)   // run before security filters so total time is accurate
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest  request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain         filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long     elapsed = System.currentTimeMillis() - start;
            String   method  = request.getMethod();
            String   uri     = request.getRequestURI();
            int      status  = response.getStatus();
            String   remote  = request.getRemoteAddr();

            // Use INFO for client errors (4xx) and server errors (5xx),
            // DEBUG for successful responses so normal traffic stays quiet.
            if (status >= 400) {
                log.warn("[{}] {} {} → {} ({} ms)", remote, method, uri, status, elapsed);
            } else {
                log.debug("[{}] {} {} → {} ({} ms)", remote, method, uri, status, elapsed);
            }
        }
    }

    /**
     * Skip logging for Spring Boot's internal async dispatch so each request
     * is logged exactly once.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }
}