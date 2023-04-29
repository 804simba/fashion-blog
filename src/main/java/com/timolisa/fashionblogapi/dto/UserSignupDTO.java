package com.timolisa.fashionblogapi.dto;

import com.timolisa.fashionblogapi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDTO {
    private String username;
    private String email;
    private String password;
}
