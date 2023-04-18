package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.repository.UserRepository;
import com.timolisa.fashionblogapi.util.ResponseManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.timolisa.fashionblogapi.UserData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Slf4j
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ResponseManager<UserResponseDTO> responseManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterUser_Success() throws Exception {
        UserSignupDTO signupDTO = buildUserSignUpDTO();

        log.info("SignUpDTO: {}", signupDTO);

        User savedUser = buildUser();
        savedUser.setUserId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO userResponseDTO =
                buildUserResponseDTO();
        ApiResponse<UserResponseDTO> mockedResponse =
                new ApiResponse<>("Registration successful", true, userResponseDTO);

        when(responseManager.success(any(UserResponseDTO.class))).thenReturn(mockedResponse);

        ApiResponse<UserResponseDTO> actualResponse =
                userService.registerUser(signupDTO);

        assertNotNull(actualResponse);
        assertTrue(actualResponse.isSuccess());
        UserResponseDTO responseDTO =
                actualResponse.getData();

        assertEquals(signupDTO.getUsername(), responseDTO.getUsername());
        assertEquals(signupDTO.getEmail(), responseDTO.getEmail());
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // create the test user
        User user = buildUser();
        user.setUserId(1L);

        // Set up the mock behaviour of the repository
        when(userRepository.existsByUsernameAndPassword(
                user.getUsername(),
                user.getPassword())).thenReturn(true);

        when(userRepository.findByUsernameAndPassword(
                user.getUsername(),
                user.getPassword())).thenReturn(Optional.of(user));

        UserResponseDTO responseDTO = buildUserResponseDTO();

        ApiResponse<UserResponseDTO> mockedResponse =
                new ApiResponse<>("Login successful", true, responseDTO);

        when(responseManager.success(any(UserResponseDTO.class))).thenReturn(mockedResponse);


        // Call the method being tested
        UserLoginDTO loginDTO =
                buildUserLoginDTO();
        ApiResponse<UserResponseDTO> actualResponse =
                userService.loginUser(loginDTO);


        assertTrue(actualResponse.isSuccess());
        UserResponseDTO data = actualResponse.getData();
        assertEquals(user.getUsername(), data.getUsername());
        assertEquals(user.getEmail(), data.getEmail());
    }
}