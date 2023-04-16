package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.UserDTO;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import com.timolisa.fashionblogapi.repository.UserRepository;
import com.timolisa.fashionblogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(UserDTO userDTO) throws UsernameExistsException {
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());

        if (user.isPresent()) {
            throw new UsernameExistsException("Username already exists");
        }
        return userRepository.save(userDtoToUser(userDTO));
    }

    @Override
    public User loginUser(UserDTO userDTO) {
        return null;
    }

    private User userDtoToUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

    private UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
