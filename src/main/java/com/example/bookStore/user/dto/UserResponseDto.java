package com.example.bookStore.user.dto;

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
    private String role;        // "ADMIN" or "CUSTOMER"
    private LocalDateTime createdAt;
}
