package com.qiao.assetmanagement.user;

import com.qiao.assetmanagement.user.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody
                                   LoginRequest loginRequest) {
        return ResponseEntity.ok(Map.of("result", authService.login(loginRequest)));
    }
}
