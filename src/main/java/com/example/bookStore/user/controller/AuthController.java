package com.example.bookStore.user.controller;

import com.example.bookStore.user.dto.LoginRequestDto;
import com.example.bookStore.user.dto.RegisterRequestDto;
import com.example.bookStore.user.dto.UserResponseDto;
import com.example.bookStore.user.model.RefreshToken;
import com.example.bookStore.user.model.User;
import com.example.bookStore.user.repository.RefreshTokenRepository;
import com.example.bookStore.user.service.UserService;
import com.example.bookStore.user.mapper.UserMapper;
import com.example.bookStore.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    // ----------------- REGISTER -----------------
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        User savedUser = userService.saveUser(userMapper.toEntity(requestDto));

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshTokenStr = jwtService.generateRefreshToken(savedUser);

        // Capture device info
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String deviceId = userAgent + " | " + ip; // you can hash it if needed

        // Save refresh token in DB with device info
        saveRefreshToken(savedUser, refreshTokenStr, deviceId);

        // Set HttpOnly cookie
        addRefreshTokenCookie(response, refreshTokenStr);

        // Log device info (optional)
        System.out.println("REGISTER -> User-Agent: " + userAgent);
        System.out.println("REGISTER -> IP: " + ip);

        // Prepare response DTO
        UserResponseDto resDto = userMapper.toResponseDto(savedUser);
        resDto.setToken(accessToken);
        resDto.setMessage("Registered successfully");

        return ResponseEntity.ok(resDto);
    }

    // ----------------- LOGIN -----------------
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        User user = userService.authenticate(requestDto.getEmail(), requestDto.getPassword());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenStr = jwtService.generateRefreshToken(user);

        // Build deviceId = userAgent + IP
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String deviceId = userAgent + "_" + ip;

        // Save refresh token in DB with deviceId
        saveRefreshToken(user, refreshTokenStr, deviceId);

        // Set HttpOnly cookie
        addRefreshTokenCookie(response, refreshTokenStr);

        // Debug log
        System.out.println("User-Agent: " + userAgent);
        System.out.println("IP: " + ip);
        System.out.println("DeviceId: " + deviceId);

        // Build response
        UserResponseDto resDto = userMapper.toResponseDto(user);
        resDto.setToken(accessToken);
        resDto.setMessage("Login successful");

        return ResponseEntity.ok(resDto);
    }

    // ----------------- REFRESH TOKEN -----------------
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshTokenStr) {
        try {
            RefreshToken dbToken = validateAndGetRefreshToken(refreshTokenStr);
            String newAccessToken = jwtService.generateAccessToken(dbToken.getUser());
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // ----------------- LOGOUT -----------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshTokenStr,
                                    HttpServletResponse response) {
        if (refreshTokenStr != null) {
            try {
                RefreshToken dbToken = validateAndGetRefreshToken(refreshTokenStr);
                dbToken.setRevoked(true);
                refreshTokenRepository.save(dbToken);
            } catch (RuntimeException ignored) {
                // Invalid token, still proceed to delete cookie
            }
            deleteRefreshTokenCookie(response);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ----------------- HELPER METHODS -----------------
    private void saveRefreshToken(User user, String tokenStr, String deviceId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenStr)
                .user(user)
                .deviceId(deviceId) // <-- store device info
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken validateAndGetRefreshToken(String refreshTokenStr) {
        if (refreshTokenStr == null || !jwtService.validateRefreshToken(refreshTokenStr)) {
            throw new RuntimeException("Invalid or missing refresh token");
        }

        RefreshToken dbToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (dbToken.isRevoked() || dbToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired or revoked");
        }

        return dbToken;
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true if using HTTPS
        cookie.setPath("/api/auth/refresh-token");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh-token");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
