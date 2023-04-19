package com.timolisa.fashionblogapi.entity;

import com.timolisa.fashionblogapi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private String username;
    private String password;
    private String email;
    private Role role;
}
