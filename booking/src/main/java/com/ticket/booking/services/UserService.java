package com.ticket.booking.services;

import com.ticket.booking.models.Role;
import com.ticket.booking.models.User;
import com.ticket.booking.repositories.UserRepository;
import com.ticket.booking.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRole() == null){
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    public String login(String email, String password) {
        Optional<User> user =  userRepository.findByEmail(email);

        if(user.isPresent()) {
            User userObject = user.get();
            if(passwordEncoder.matches(password, userObject.getPassword())) {
                return jwtUtil.generateToken(email, userObject.getRole().name());
            }
        }
        return "Invalid credentials";
    }
}
