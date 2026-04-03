package com.finance.service;

import com.finance.dto.request.*;
import com.finance.dto.response.AuthResponse;
import com.finance.exception.DuplicateEmailException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import com.finance.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest req) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = tokenProvider.generateToken(auth);
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        return new AuthResponse(token, "Bearer", user.getId(),
                user.getName(), user.getEmail(), user.getRole());
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException(req.getEmail());
        }
        User user = User.builder()
                .name(req.getName()).email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole()).active(true).build();
        userRepository.save(user);
        // Auto-login after registration
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = tokenProvider.generateToken(auth);
        return new AuthResponse(token, "Bearer", user.getId(),
                user.getName(), user.getEmail(), user.getRole());
    }
}