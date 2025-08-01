package com.femi.tickerdesk.controller;

import com.femi.tickerdesk.auth.JwtService;
import com.femi.tickerdesk.dto.*;
import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;
import com.femi.tickerdesk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!user.getIsActive()) {
                log.warn("Login attempt for inactive user: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Account is deactivated. Please contact administrator."));
            }
            String token = jwtService.generateToken(user);
            userService.updateLastLogin(user.getId());
            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .user(UserInfo.fromUser(user))
                    .build();

            log.info("Successful login for user: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));

        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication failed"));
        } catch (Exception e) {
            log.error("Login error for email: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed. Please try again."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());

        try {
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User with this email already exists"));
            }
            if (request.getEmployeeId() != null && userService.existsByEmployeeId(request.getEmployeeId())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User with this employee ID already exists"));
            }
            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(request.getPassword())
                    .department(request.getDepartment())
                    .site(request.getSite())
                    .role(request.getRole() != null ? request.getRole() : Role.USER)
                    .employeeId(request.getEmployeeId())
                    .isActive(true)
                    .build();

            User createdUser = userService.createUser(user);
            String token = jwtService.generateToken(createdUser);
            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .user(UserInfo.fromUser(createdUser))
                    .build();

            log.info("Successful registration for user: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Registration successful", authResponse));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Registration error for email: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed. Please try again."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh attempt");

        try {
            String token = request.getToken();
            String email = jwtService.extractUsername(token);
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (jwtService.isTokenValid(token, user)) {
                String newToken = jwtService.generateToken(user);

                AuthResponse authResponse = AuthResponse.builder()
                        .token(newToken)
                        .tokenType("Bearer")
                        .user(UserInfo.fromUser(user))
                        .build();

                return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid or expired token"));
            }

        } catch (Exception e) {
            log.error("Token refresh error: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token refresh failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("Logout attempt");

        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
        } catch (Exception e) {
            log.error("Logout error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed"));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        log.debug("Token validation attempt");

        try {
            String token = request.getToken();
            String email = jwtService.extractUsername(token);

            Optional<User> userOpt = userService.findByEmail(email);

            if (userOpt.isPresent() && jwtService.isTokenValid(token, userOpt.get())) {
                TokenValidationResponse response = TokenValidationResponse.builder()
                        .valid(true)
                        .email(email)
                        .build();

                return ResponseEntity.ok(ApiResponse.success("Token is valid", response));
            } else {
                TokenValidationResponse response = TokenValidationResponse.builder()
                        .valid(false)
                        .build();

                return ResponseEntity.ok(ApiResponse.success("Token is invalid", response));
            }

        } catch (Exception e) {
            log.error("Token validation error: ", e);
            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(false)
                    .build();

            return ResponseEntity.ok(ApiResponse.success("Token is invalid", response));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfo>> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(ApiResponse.success("User information retrieved", UserInfo.fromUser(user)));

        } catch (Exception e) {
            log.error("Get current user error: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Unable to retrieve user information"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());

        try {
            Optional<User> userOpt = userService.findByEmail(request.getEmail());

            if (userOpt.isPresent()) {
                log.info("Password reset requested for: {}", request.getEmail());
                return ResponseEntity.ok(ApiResponse.success("Password reset instructions sent to your email", null));
            } else {
                return ResponseEntity.ok(ApiResponse.success("If the email exists, password reset instructions have been sent", null));
            }

        } catch (Exception e) {
            log.error("Forgot password error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unable to process password reset request"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Password reset attempt");

        try {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(ApiResponse.error("Password reset functionality not implemented yet"));

        } catch (Exception e) {
            log.error("Reset password error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Password reset failed"));
        }
    }
}


