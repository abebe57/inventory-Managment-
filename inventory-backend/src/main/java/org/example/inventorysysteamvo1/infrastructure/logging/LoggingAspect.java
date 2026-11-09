package org.example.inventorysysteamvo1.infrastructure.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP aspect that automatically logs method entry, exit, elapsed time,
 * and exceptions for every class in the service implementation layer.
 *
 * ⚠️  DEPENDENCY REQUIRED — add to pom.xml if not already present:
 *
 *   <dependency>
 *       <groupId>org.springframework.boot</groupId>
 *       <artifactId>spring-boot-starter-aop</artifactId>
 *   </dependency>
 *
 * Without spring-boot-starter-aop (which pulls in aspectjweaver),
 * the @Aspect annotation has no effect and this class does nothing.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ── Pointcut Definitions ──────────────────────────────────────────────

    /** Matches every method in every service implementation class. */
    @Pointcut("within(org.example.inventorysysteamvo1.service.serviceIMPL..*)")
    public void serviceImplLayer() {}

    /** Matches every method in every repository adapter class. */
    @Pointcut("within(org.example.inventorysysteamvo1.infrastructure.adapter..*)")
    public void repositoryAdapterLayer() {}

    // ── Advice ───────────────────────────────────────────────────────────

    /**
     * Wraps every service-impl method: logs entry with arguments,
     * exit with return value (type only — never the value of sensitive
     * fields like passwords), and elapsed time on exit or exception.
     */
    @Around("serviceImplLayer()")
    public Object logAroundService(ProceedingJoinPoint pjp) throws Throwable {
        return logAround(pjp, "SERVICE");
    }

    /**
     * Same treatment for the repository adapter layer — useful for
     * debugging mapping issues without touching domain or JPA code.
     */
    @Around("repositoryAdapterLayer()")
    public Object logAroundRepository(ProceedingJoinPoint pjp) throws Throwable {
        return logAround(pjp, "REPO");
    }

    // ── Private Helper ────────────────────────────────────────────────────

    private Object logAround(ProceedingJoinPoint pjp, String layer) throws Throwable {
        String className  = pjp.getSignature().getDeclaringType().getSimpleName();
        String methodName = pjp.getSignature().getName();
        Object[] args     = pjp.getArgs();

        if (log.isDebugEnabled()) {
            log.debug("[{}] >> {}.{}() — args: {}",
                    layer, className, methodName, safeArgs(methodName, args));
        }

        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long elapsed  = System.currentTimeMillis() - start;

            if (log.isDebugEnabled()) {
                log.debug("[{}] << {}.{}() — completed in {} ms — returned: {}",
                        layer, className, methodName, elapsed,
                        result == null ? "void/null" : result.getClass().getSimpleName());
            }
            return result;

        } catch (Throwable ex) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("[{}] !! {}.{}() — threw {} after {} ms — message: {}",
                    layer, className, methodName,
                    ex.getClass().getSimpleName(), elapsed, ex.getMessage());
            throw ex;   // re-throw so the caller still sees the exception
        }
    }

    /**
     * Converts method arguments to a string for logging.
     * Replaces password-related parameters with "***REDACTED***"
     * so no sensitive data ever appears in log files.
     */
    private String safeArgs(String methodName, Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        // Suppress all argument values for password-related methods
        if (methodName.toLowerCase().contains("password")
                || methodName.toLowerCase().contains("login")) {
            return "[***REDACTED***]";
        }
        return Arrays.toString(args);
    }
}