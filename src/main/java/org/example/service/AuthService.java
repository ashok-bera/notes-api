package org.example.service;

import org.example.model.User;
import org.example.repo.UserRepository;
import org.example.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepo, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    public void register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(password));
        userRepo.save(user);
    }

    public String login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}

