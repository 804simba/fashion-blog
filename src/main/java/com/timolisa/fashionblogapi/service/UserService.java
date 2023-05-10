package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.request.AdminSignupDTO;
import com.timolisa.fashionblogapi.dto.request.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.response.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.request.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long userId);
    UserDetails loadByUsername(String username);
    boolean usernameExists(String username);
    APIResponse<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws UsernameExistsException;
    APIResponse<UserResponseDTO> registerAdmin(AdminSignupDTO adminSignUpDTO) throws UnauthorizedAccessException, InvalidInputsException, UsernameExistsException;
    APIResponse<UserResponseDTO> loginUser(UserLoginDTO userLoginDTO) throws UserDoesNotExistException;
}
