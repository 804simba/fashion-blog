package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.AdminSignupDTO;
import com.timolisa.fashionblogapi.dto.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
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
    public APIResponse<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws UsernameExistsException {
        APIResponse<UserResponseDTO> apiResponse;

        boolean usernameExistsStatus = usernameExists(userSignupDTO.getUsername());

        if (usernameExistsStatus) {
            throw new UsernameExistsException("Username already exists");
        }

        User user = userDtoToUser(userSignupDTO);
        user.setRole(Role.USER);
        userRepository.save(user);

        UserResponseDTO userResponseDTO = userToUserDTO(user);
        apiResponse = responseManager.success(userResponseDTO);

        return apiResponse;
    }

    @Override
    public APIResponse<UserResponseDTO> registerAdmin(AdminSignupDTO adminSignUpDTO) throws UsernameExistsException {
        String username = adminSignUpDTO.getUsername();

        APIResponse<UserResponseDTO> apiResponse;

        boolean usernameExists =
                userRepository.existsByUsername(username);

        if (usernameExists) {
            throw new UsernameExistsException("Username exists already");
        }
        User user = adminDtoToUser(adminSignUpDTO);
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        UserResponseDTO userResponseDTO = userToUserDTO(user);
        apiResponse = responseManager.success(userResponseDTO);
        return apiResponse;
    }

    @Override
    public APIResponse<UserResponseDTO> loginUser(UserLoginDTO userLoginDTO) throws UserDoesNotExistException {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        boolean isUserExists = userRepository
                .existsByUsernameAndPassword(username, password);

        if (!isUserExists) {
            throw new UserDoesNotExistException("User does not exist.");
        }
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> {
                    String message = "User does not exist";
                    return new UserDoesNotExistException(message);
                });
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
                .role(user.getRole())
                .build();
    }
    private User adminDtoToUser(AdminSignupDTO adminSignupDTO) {
        return User.builder()
                .username(adminSignupDTO.getUsername())
                .email(adminSignupDTO.getEmail())
                .password(adminSignupDTO.getPassword())
                .build();
    }
}
