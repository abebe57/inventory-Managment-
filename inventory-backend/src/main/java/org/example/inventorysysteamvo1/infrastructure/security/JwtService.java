package org.example.inventorysysteamvo1.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.inventorysysteamvo1.infrastructure.configuration.JwtConfig;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Handles JWT creation and validation using jjwt 0.12.x.
 *
 * Note on jjwt 0.12 API:
 *   - Jwts.builder()  uses .subject() / .issuedAt() / .expiration()
 *     (not the deprecated .setSubject() / .setIssuedAt() / .setExpiration())
 *   - Jwts.parser()   uses .verifyWith(key).build().parseSignedClaims(token)
 *     (not the deprecated .setSigningKey() / .parseClaimsJws())
 */
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // ── Token Generation ──────────────────────────────────────────────────

    /**
     * Generates a signed JWT containing the username as the subject,
     * plus staffId and roleId as custom claims.
     */
    public String generateToken(String username, Long staffId, Long roleId) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtConfig.getExpirationMs());

        return Jwts.builder()
                .subject(username)
                .claim("staffId", staffId)
                .claim("roleId",  roleId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    // ── Token Validation ──────────────────────────────────────────────────

    /**
     * Returns true if the token is signed correctly, not expired,
     * and belongs to the given username.
     */
    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = parseClaims(token);
            return expectedUsername.equals(claims.getSubject())
                    && !isExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ── Claim Extraction ──────────────────────────────────────────────────

    /** Extracts the username (subject) from the token. */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /** Extracts the staffId custom claim from the token. */
    public Long extractStaffId(String token) {
        Object value = parseClaims(token).get("staffId");
        return value instanceof Number n ? n.longValue() : null;
    }

    /** Extracts the roleId custom claim from the token. */
    public Long extractRoleId(String token) {
        Object value = parseClaims(token).get("roleId");
        return value instanceof Number n ? n.longValue() : null;
    }

    // ── Private Helpers ───────────────────────────────────────────────────

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * Decodes the Base64 secret from JwtConfig and returns an HMAC-SHA key.
     * Called on every use instead of caching so config refreshes are respected.
     */
    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}