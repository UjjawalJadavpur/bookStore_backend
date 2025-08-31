package com.example.bookStore.user.mapper;


import com.example.bookStore.user.dto.RegisterRequestDto;
import com.example.bookStore.user.dto.UserResponseDto;
import com.example.bookStore.user.model.User;
import com.example.bookStore.user.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Request DTO -> Entity
    public User toEntity(RegisterRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword());
        if (dto.getRole() == null || dto.getRole().isBlank()) {
            user.setRole(Role.CUSTOMER);
        } else {
            user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        }
        return user;
    }

    // Entity -> Response DTO
    public UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        return UserResponseDto.builder()
//                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
