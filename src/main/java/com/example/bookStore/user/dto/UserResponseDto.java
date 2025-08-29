package com.example.bookStore.user.dto;

import com.example.bookStore.user.model.Role;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private Role role;       // "ADMIN" or "CUSTOMER"
    private LocalDateTime createdAt;
    // extra fields for login/register response
    private String token;
    private String message;
}


