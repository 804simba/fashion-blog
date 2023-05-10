package com.timolisa.fashionblogapi.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminSignupDTO {
    private String username;
    private String password;
    private String email;
}
