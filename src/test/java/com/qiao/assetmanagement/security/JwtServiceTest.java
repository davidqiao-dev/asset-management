package com.qiao.assetmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "hih9NiHi6aBepDxUBltZWjcXR0NnWCWA58COHr3Xqrc=",
                3_600_000
        );
    }

    @Test
    void generatesAndParsesToken() {
        String token = jwtService.generateToken("admin", "ADMIN");
        Claims claims = jwtService.parseToken(token);
        assertThat(claims.getSubject()).isEqualTo("admin");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    }

    @Test
    void payloadIsReadableButSignatureIsNot() {
        String token = jwtService.generateToken("admin", "ADMIN");
        String[] parts = token.split("\\.");
        String payload = new String((Base64.getUrlDecoder().decode(parts[1])));
        System.out.println("payload: " + payload);
        // expect {"sub": "admin", "role": "ADMIN", "iat":..., "exp":...}

    }

    @Test
    void tamperedTokenIsRejected() {
        String token = jwtService.generateToken("admin", "ADMIN");
        String[] parts = token.split("\\.");

        // flip the payload to claim OPERATOR instead of ADMIN
        String forgedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                ("{\"sub\": \"admin\", \"role\": \"OPERTOR\"" +
                        ", \"exp\": 9999999999}").getBytes(StandardCharsets.UTF_8));

        String forged = parts[0] + "." + forgedPayload + "." + parts[2];
        assertThatThrownBy(() -> jwtService.parseToken(forged))
                .isInstanceOf(JwtException.class)
                .satisfies(e ->
                        System.out.println(e.getClass().getName()));
    }
}
