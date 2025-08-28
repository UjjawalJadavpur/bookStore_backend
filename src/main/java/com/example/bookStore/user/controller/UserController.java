package com.example.bookStore.user.controller;

import com.example.bookStore.user.dto.UserRequestDto;
import com.example.bookStore.user.dto.UserResponseDto;
import com.example.bookStore.user.model.User;
import com.example.bookStore.user.service.UserService;
import com.example.bookStore.user.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);
        User savedUser = userService.saveUser(user);
        return userMapper.toResponseDto(savedUser);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{email}")
    public UserResponseDto getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.toResponseDto(user);
    }
}
