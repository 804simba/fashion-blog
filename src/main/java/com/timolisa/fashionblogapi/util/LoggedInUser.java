package com.timolisa.fashionblogapi.util;

import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoggedInUser {
    private final HttpSession session;
    private final UserRepository userRepository;

    public User findLoggedInUser() {
        Long id = (Long) session.getAttribute("userId");
        return userRepository.findById(id).get();
    }
}
