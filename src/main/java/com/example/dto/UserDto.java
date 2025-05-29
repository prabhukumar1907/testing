package com.example.dto;

import com.example.enums.Role;

public record UserDto(
        Long id,
        String username,
        String email,
        Role role
) {
}