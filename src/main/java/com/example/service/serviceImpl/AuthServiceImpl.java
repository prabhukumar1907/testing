package com.example.service.serviceImpl;

import com.example.dto.AuthResponse;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.repository.UserRepository;
import com.example.request.LoginRequest;
import com.example.request.RegisterRequest;
import com.example.service.AuthService;
import com.example.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zipcode(request.getZipcode())
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(String.valueOf(user.getEmail()));
        String refreshToken = jwtService.generateRefreshToken(String.valueOf(user.getEmail()));
        return new AuthResponse(accessToken, refreshToken,new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken  = jwtService.generateToken(String.valueOf(user));
        String refreshToken = jwtService.generateRefreshToken(String.valueOf(user.getEmail()));

        return new AuthResponse(accessToken,refreshToken, new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }
}
