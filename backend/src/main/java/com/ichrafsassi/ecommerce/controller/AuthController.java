package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.*;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.service.AuthService;
import com.ichrafsassi.ecommerce.util.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CurrentUser currentUser;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserDto me() {
        return authService.me(currentUser.require());
    }

    /** Placeholder until Google OAuth client credentials are configured. */
    @PostMapping("/google")
    public void googleSignIn() {
        throw new BadRequestException(
                "Google sign-in is not configured. Use email login or enable app.oauth.google in application.yml.");
    }
}
