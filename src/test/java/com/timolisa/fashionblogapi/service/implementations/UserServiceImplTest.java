package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.UserDTO;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.exception.UsernameExistsException;
import com.timolisa.fashionblogapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.timolisa.fashionblogapi.UserData.buildUser;
import static com.timolisa.fashionblogapi.UserData.buildUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldRegisterUser() throws Exception {
        UserDTO userDTO = buildUserDTO();

        User user = buildUser();

        when(userRepository.findByUsername("jeffHardy")).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(userDTO);

        verify(userRepository, times(1)).findByUsername("jeffHardy");
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals("jeffHardy", registeredUser.getUsername());
        assertEquals("jeff@gmail.com", registeredUser.getEmail());
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername() {
        UserDTO userDTO = buildUserDTO();

        User user = buildUser();

        when(userRepository.findByUsername("jeffHardy")).thenReturn(Optional.of(user));

        assertThrows(UsernameExistsException.class,
                () -> userService.registerUser(userDTO));

        verify(userRepository, times(1)).findByUsername("jeffHardy");
        verify(userRepository, never()).save(any(User.class));
    }
}