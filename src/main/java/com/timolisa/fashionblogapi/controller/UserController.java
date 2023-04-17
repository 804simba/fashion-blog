package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import com.timolisa.fashionblogapi.service.UserService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fashion-blog/users")
public class UserController {
    private final UserService userService;
    private final ResponseManager<UserResponseDTO> responseManager;

    @Autowired
    public UserController(UserService userService,
                          ResponseManager<UserResponseDTO> responseManager) {
        this.userService = userService;
        this.responseManager = responseManager;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody UserSignupDTO userSignupDTO) {
        ApiResponse<UserResponseDTO> apiResponse;
        try {
            apiResponse = userService.registerUser(userSignupDTO);
        } catch (UsernameExistsException e) {
            apiResponse = responseManager.error("Username already exists", false);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@RequestBody UserLoginDTO userLoginDTO) {
        ApiResponse<UserResponseDTO> apiResponse;
        try {
           apiResponse =
                    userService.loginUser(userLoginDTO);
           return ResponseEntity.ok(apiResponse);
        } catch (UserDoesNotExistException e) {
            ApiResponse<UserResponseDTO> errorResponse =
                    new ApiResponse<>(e.getMessage(), false, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
