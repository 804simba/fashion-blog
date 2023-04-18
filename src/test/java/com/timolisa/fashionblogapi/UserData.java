package com.timolisa.fashionblogapi;

import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Role;

public class UserData {
    private UserData() {};

    public static User buildUser() {
        return User.builder()
                .username("jeffHardy")
                .email("jeff@gmail.com")
                .password("1234")
                .role(Role.USER)
                .build();
    }
    public static UserSignupDTO buildUserSignUpDTO() {
        return UserSignupDTO.builder()
                .username("jeffHardy")
                .email("jeff@gmail.com")
                .password("1234")
                .build();
    }

    public static UserLoginDTO buildUserLoginDTO() {
        return UserLoginDTO.builder()
                .username("jeffHardy")
                .password("1234")
                .build();
    }

    public static UserResponseDTO buildUserResponseDTO() {
        return UserResponseDTO.builder()
                .username("jeffHardy")
                .email("jeff@gmail.com")
                .build();
    }
}
