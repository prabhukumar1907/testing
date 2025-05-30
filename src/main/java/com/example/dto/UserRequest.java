package com.example.dto;

import com.example.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String status;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private Role role;
}
