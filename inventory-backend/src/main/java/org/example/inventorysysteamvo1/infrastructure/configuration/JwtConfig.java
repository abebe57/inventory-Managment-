package org.example.inventorysysteamvo1.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Binds the jwt.* properties from application.properties into a typed bean.
 *
 * Required properties:
 *   jwt.secret       — Base64-encoded HMAC-SHA256 key, at least 256 bits (32 raw bytes → ~44 Base64 chars)
 *   jwt.expiration-ms — Token lifetime in milliseconds (e.g. 86400000 = 24 h)
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * Base64-encoded secret key used to sign JWTs.
     * Generate a suitable value with:
     *   openssl rand -base64 32
     * Never commit the real secret to source control.
     */
    private String secret;

    /**
     * Token lifetime in milliseconds.
     * Default: 86 400 000 ms = 24 hours.
     */
    private long expirationMs = 86_400_000L;

    // ── Getters & Setters (required by @ConfigurationProperties) ─────────

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}