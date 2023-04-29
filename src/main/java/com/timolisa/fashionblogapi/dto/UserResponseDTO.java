package com.timolisa.fashionblogapi.dto;

import com.timolisa.fashionblogapi.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private String token;
    @CreationTimestamp
    private Timestamp createdAt;
}
