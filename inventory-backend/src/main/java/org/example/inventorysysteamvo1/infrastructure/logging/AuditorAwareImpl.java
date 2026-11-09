package org.example.inventorysysteamvo1.infrastructure.logging;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Supplies the currently authenticated username to Spring Data JPA's
 * auditing mechanism so that @CreatedBy / @LastModifiedBy columns on
 * entity classes are automatically populated.
 *
 * Activation — add @EnableJpaAuditing to a @Configuration class (already
 * done in ModelMapperConfig) with auditorAwareRef = "auditorAwareImpl".
 *
 * Usage on entity classes:
 *
 *   @EntityListeners(AuditingEntityListener.class)
 *   public class SomeEntity {
 *       @CreatedBy   private String createdBy;
 *       @LastModifiedBy private String lastModifiedBy;
 *       @CreatedDate private LocalDateTime createdAt;
 *       @LastModifiedDate private LocalDateTime updatedAt;
 *   }
 */
@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String SYSTEM_USER   = "SYSTEM";
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            // No active session (e.g., during data initialisation)
            return Optional.of(SYSTEM_USER);
        }

        String name = auth.getName();

        // Spring Security uses "anonymousUser" for unauthenticated requests
        // that still reach a filter. Treat this as the system user.
        if (ANONYMOUS_USER.equals(name)) {
            return Optional.of(SYSTEM_USER);
        }

        return Optional.of(name);
    }
}