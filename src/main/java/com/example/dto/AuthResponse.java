package com.example.dto;

public record AuthResponse(String accessToken,String refreshToken,UserDto user) { }
