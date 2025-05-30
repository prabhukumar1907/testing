package com.example.dto;

import com.example.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String status;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
