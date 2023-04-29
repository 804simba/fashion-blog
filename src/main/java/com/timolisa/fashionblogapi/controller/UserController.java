package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.dto.AdminSignupDTO;
import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import com.timolisa.fashionblogapi.service.UserService;
import com.timolisa.fashionblogapi.utils.ResponseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fashion-blog/auth")
public class UserController {
    private final UserService userService;
    private final ResponseManager<UserResponseDTO> responseManager;

    @Autowired
    public UserController(UserService userService,
                          ResponseManager<UserResponseDTO> responseManager) {
        this.userService = userService;
        this.responseManager = responseManager;
    }

    @PostMapping("/admin/sign-up")
    public ResponseEntity<APIResponse<UserResponseDTO>> adminRegistration(@RequestBody AdminSignupDTO signupDTO) {
        APIResponse<UserResponseDTO> apiResponse;
        try {
            apiResponse = userService.registerAdmin(signupDTO);
        } catch (UsernameExistsException | UnauthorizedAccessException | InvalidInputsException e) {
            apiResponse = responseManager.error("Bad credentials", false);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/user/sign-up")
    public ResponseEntity<APIResponse<UserResponseDTO>> register(@RequestBody UserSignupDTO userSignupDTO) {
        APIResponse<UserResponseDTO> apiResponse;
        try {
            apiResponse = userService.registerUser(userSignupDTO);
        } catch (UsernameExistsException e) {
            apiResponse = responseManager.error("Username already exists", false);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/user/login")
    public ResponseEntity<APIResponse<UserResponseDTO>> login(@RequestBody UserLoginDTO userLoginDTO) {
        APIResponse<UserResponseDTO> apiResponse;
        try {
           apiResponse =
                    userService.loginUser(userLoginDTO);
           return ResponseEntity.ok(apiResponse);
        } catch (UserDoesNotExistException e) {
            APIResponse<UserResponseDTO> errorResponse =
                    new APIResponse<>(e.getMessage(), false, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
