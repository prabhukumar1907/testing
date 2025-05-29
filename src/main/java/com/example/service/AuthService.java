package com.example.service;


import com.example.dto.AuthResponse;
import com.example.request.LoginRequest;
import com.example.request.RegisterRequest;

public interface AuthService {
     AuthResponse register(RegisterRequest request);
     AuthResponse login(LoginRequest loginRequest);
}
