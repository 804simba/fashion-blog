package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.AdminSignupDTO;
import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;

public interface UserService {
    boolean usernameExists(String username);
    ApiResponse<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws UsernameExistsException;
    ApiResponse<UserResponseDTO> registerAdmin(AdminSignupDTO adminSignUpDTO) throws UnauthorizedAccessException, InvalidInputsException, UsernameExistsException;
    ApiResponse<UserResponseDTO> loginUser(UserLoginDTO userLoginDTO) throws UserDoesNotExistException;
}
