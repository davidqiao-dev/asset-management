package com.qiao.assetmanagement.user;

import com.qiao.assetmanagement.security.JwtService;
import com.qiao.assetmanagement.user.dto.LoginRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository
            , PasswordEncoder passwordEncoder
            , JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest req) {
        AppUser user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtService.generateToken(user.getUsername(), user.getRole().name());
    }
}
