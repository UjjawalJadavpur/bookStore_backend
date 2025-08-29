package com.example.bookStore.user.controller;

import com.example.bookStore.user.dto.LoginRequestDto;
import com.example.bookStore.user.dto.RegisterRequestDto;
import com.example.bookStore.user.dto.UserResponseDto;
import com.example.bookStore.user.model.User;
import com.example.bookStore.user.service.UserService;
import com.example.bookStore.user.mapper.UserMapper;
import com.example.bookStore.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserMapper userMapper, JwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);
        User savedUser = userService.saveUser(user);

        String token = jwtService.generateToken(savedUser);

        UserResponseDto response = userMapper.toResponseDto(savedUser);
        response.setToken(token);
        response.setMessage("Registered successfully");
        return response;
    }


    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        User user = userService.authenticate(requestDto.getEmail(), requestDto.getPassword());
        String token = jwtService.generateToken(user);

        UserResponseDto response = userMapper.toResponseDto(user);
        response.setToken(token);
        response.setMessage("Login successful");
        return response;
    }

}
