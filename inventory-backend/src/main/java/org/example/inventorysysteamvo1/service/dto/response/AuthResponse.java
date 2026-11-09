package org.example.inventorysysteamvo1.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body returned by POST /api/auth/login.
 * Contains the signed JWT the client must send in every subsequent request
 * via the "Authorization: Bearer <token>" header.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** The signed JWT string. */
    private String token;

    /** Always "Bearer" — tells the client how to send the token. */
    private String tokenType = "Bearer";

    /** Authenticated staff's username, mirrored for convenience. */
    private String username;

    /** Authenticated staff's database ID. */
    private Long staffId;

    /** The role assigned to this staff member. */
    private Long roleId;
}