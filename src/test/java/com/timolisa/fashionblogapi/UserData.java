package com.timolisa.fashionblogapi;

import com.timolisa.fashionblogapi.dto.UserDTO;
import com.timolisa.fashionblogapi.entity.User;

public class UserData {
    private UserData() {};

    public static User buildUser() {
        return User.builder()
                .userId(1L)
                .username("jeffHardy")
                .email("jeff@gmail.com")
                .password("1234")
                .build();
    }
    public static UserDTO buildUserDTO() {
        return UserDTO.builder()
                .username("jeffHardy")
                .email("jeff@gmail.com")
                .password("1234")
                .build();
    }
}
