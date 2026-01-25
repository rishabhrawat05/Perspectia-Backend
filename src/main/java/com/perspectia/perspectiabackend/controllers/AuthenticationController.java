package com.perspectia.perspectiabackend.controllers;

import com.perspectia.perspectiabackend.requests.*;
import com.perspectia.perspectiabackend.responses.RefreshTokenResponse;
import com.perspectia.perspectiabackend.responses.UserResponse;
import com.perspectia.perspectiabackend.services.AuthenticationService;
import com.perspectia.perspectiabackend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/perspectia/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    private UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.login(loginRequest, response));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest) {
        return ResponseEntity.ok(authenticationService.verifyOtp(verifyRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authenticationService.signup(signupRequest));
    }

    @PostMapping("/resend/otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
        return ResponseEntity.ok(authenticationService.resendOtp(resendOtpRequest));
    }

    @PostMapping("/refreshtoken/generate")
    public ResponseEntity<?> generateRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.generateRefreshToken(refreshTokenRequest));
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest,
                                         HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.googleLogin(googleLoginRequest.getAccessToken(), response));
    }

    @PostMapping("/github-login")
    public ResponseEntity<?> githubLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String code = request.get("code");
        return ResponseEntity.ok(authenticationService.githubLogin(code, response));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request, response);
        return ResponseEntity.ok(refreshTokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String message = authenticationService.logout(request, response);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        // Get authenticated user
        return ResponseEntity.ok(userService.findByEmail(principal.getName()));
    }
}

