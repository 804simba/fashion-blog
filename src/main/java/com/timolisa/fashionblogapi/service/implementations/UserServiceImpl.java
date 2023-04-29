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
import com.timolisa.fashionblogapi.security.jwt.JwtTokenProvider;
import com.timolisa.fashionblogapi.utils.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseManager<UserResponseDTO> responseManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username %s, not found", username)));
    }

    @Override
    public boolean usernameExists(String username) {
        boolean status;
        status = userRepository.existsByUsername(username);
        return status;
    }

    @Override
    public APIResponse<UserResponseDTO> registerUser(UserSignupDTO userSignupDTO) throws UsernameExistsException {
        String username = userSignupDTO.getUsername();

        APIResponse<UserResponseDTO> apiResponse;

        boolean usernameExistsStatus = usernameExists(username);

        if (usernameExistsStatus) {
            throw new UsernameExistsException("Username already exists");
        }

        User user = userDtoToUser(userSignupDTO);
        user.setRole(Role.USER);
        userRepository.save(user);
        // UsernamePasswordAuthenticationToken is a simple representation of Username and password
        String token = jwtTokenProvider
                .generateToken(new UsernamePasswordAuthenticationToken(user, null));

        UserResponseDTO userResponseDTO = userToUserDTO(user);
        userResponseDTO.setToken(token);
        apiResponse = responseManager.success(userResponseDTO);

        apiResponse.setMessage("User Registered successfully");
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userResponseDTO);

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
        String token = jwtTokenProvider
                .generateToken(new UsernamePasswordAuthenticationToken(user, null));

        UserResponseDTO userResponseDTO = userToUserDTO(user);
        userResponseDTO.setToken(token);
        apiResponse = responseManager.success(userResponseDTO);
        apiResponse.setMessage("User Registered successfully");
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userResponseDTO);
        return apiResponse;
    }

    @Override
    public UserDetails loadByUsername(String username) {
        return loadUserByUsername(username);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public APIResponse<UserResponseDTO> loginUser(UserLoginDTO userLoginDTO) throws UserDoesNotExistException {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        UserDetails userDetails;
        try {
            userDetails = loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new UserDoesNotExistException("User does not exist");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Wrong username or password");
        }
        User user = (User) userDetails;
        String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user, null));
        UserResponseDTO userResponseDTO = userToUserDTO(user);
        userResponseDTO.setToken(token);

        return responseManager.success(userResponseDTO);
    }

    private User userDtoToUser(UserSignupDTO userSignupDTO) {
        return User.builder()
                .username(userSignupDTO.getUsername())
                .email(userSignupDTO.getEmail())
                .password(passwordEncoder.encode(userSignupDTO.getPassword()))
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
                .password(passwordEncoder.encode(adminSignupDTO.getPassword()))
                .build();
    }
}
