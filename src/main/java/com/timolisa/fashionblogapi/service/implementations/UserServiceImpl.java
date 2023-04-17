package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import com.timolisa.fashionblogapi.repository.UserRepository;
import com.timolisa.fashionblogapi.service.UserService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ResponseManager<UserResponseDTO> responseManager;
    private final HttpSession session;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final ResponseManager<UserResponseDTO> responseManager,
                           final HttpSession session) {
        this.userRepository = userRepository;
        this.responseManager = responseManager;
        this.session = session;
    }

    @Override
    public boolean usernameExists(String username) {
        boolean status;
        status = userRepository.existsByUsername(username);
        return status;
    }

    @Override
    public ApiResponse<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws UsernameExistsException {
        ApiResponse<UserResponseDTO> apiResponse;

        boolean usernameExistsStatus = usernameExists(userSignupDTO.getUsername());

        if (usernameExistsStatus) {
            throw new UsernameExistsException("Username already exists");
        }

        User user = userDtoToUser(userSignupDTO);
        user.setRole(Role.REGISTERED_USER);
        userRepository.save(user);

        UserResponseDTO userResponseDTO = userToUserDTO(user);
        apiResponse = responseManager.success(userResponseDTO);

        return apiResponse;
    }

    @Override
    public ApiResponse<UserResponseDTO> loginUser(UserLoginDTO userLoginDTO) throws UserDoesNotExistException {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        boolean isUserExists = userRepository
                .existsByUsernameAndPassword(username, password);

        if (!isUserExists) {
            throw new UserDoesNotExistException("User does not exist.");
        }
        User user = userRepository.findByUsernameAndPassword(username, password).orElse(null);
        assert user != null;
        UserResponseDTO userResponseDTO = userToUserDTO(user);
        session.setAttribute("userId", user.getUserId());

        return responseManager.success(userResponseDTO);
    }

    private User userDtoToUser(UserSignupDTO userSignupDTO) {
        return User.builder()
                .username(userSignupDTO.getUsername())
                .email(userSignupDTO.getEmail())
                .password(userSignupDTO.getPassword())
                .build();
    }

    private UserResponseDTO userToUserDTO(User user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}