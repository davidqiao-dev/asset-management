package com.qiao.assetmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // no header -> just proceed, DON'T reject
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
            Claims claims = jwtService.parseToken(token); // verifies signature + expiry
            var authorities = List.of(new SimpleGrantedAuthority(
                    "ROLE_" + claims.get("role", String.class)));

            var auth = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException e) {
            // invalid /expired/ tampered -> do NOT reject, do NOT authenticate
            // leave the context empty; the authorization filter will reject with 401

        }

        chain.doFilter(request, response);
    }

}
