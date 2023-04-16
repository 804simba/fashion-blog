package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.UserDTO;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;

public interface UserService {
    User registerUser(UserDTO userDTO) throws UsernameExistsException;

    User loginUser(UserDTO userDTO);
}
