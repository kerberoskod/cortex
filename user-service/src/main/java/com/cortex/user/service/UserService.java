package com.cortex.user.service;

import com.cortex.common.dto.AuthRequest;
import com.cortex.common.dto.AuthResponse;
import com.cortex.common.dto.UserDTO;
import com.cortex.common.event.UserRegisteredEvent;
import com.cortex.common.util.JwtUtil;
import com.cortex.user.dto.RegisterRequest;
import com.cortex.user.model.User;
import com.cortex.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        var hashedPassword = passwordEncoder.encode(request.getPassword());
        var user = new User(request.getEmail(), hashedPassword, request.getName(), Set.of("USER"));
        user = userRepository.save(user);

        var dto = toDTO(user);
        var token = JwtUtil.generateToken(user.getId(), user.getEmail(), user.getRoles());

        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getEmail()));

        return new AuthResponse(token, dto);
    }

    public AuthResponse login(AuthRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var dto = toDTO(user);
        var token = JwtUtil.generateToken(user.getId(), user.getEmail(), user.getRoles());

        return new AuthResponse(token, dto);
    }

    public UserDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getRoles());
    }
}
