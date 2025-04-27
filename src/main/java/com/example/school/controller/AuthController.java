package com.example.school.controller;

import com.example.school.dto.AuthRequest;
import com.example.school.dto.AuthResponse;
import com.example.school.model.Tenant;
import com.example.school.repository.TenantRepository;
import com.example.school.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, TenantRepository tenantRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Tenant tenant = tenantRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));

            // Manually validate password
            if (!passwordEncoder.matches(authRequest.getPassword(), tenant.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // If success, generate token containing roles + allowed services
            final String jwtToken = jwtService.generateToken(tenant);

            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
