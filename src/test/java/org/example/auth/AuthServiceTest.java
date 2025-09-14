package org.example.auth;

import org.example.model.User;
import org.example.repo.UserRepository;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtProvider;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUser() {
        User user = new User();
        user.setEmail("abc@mail.com");
        user.setPasswordHash("hashed-pass");

        when(encoder.encode("edffsff")).thenReturn("hashed-pass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = authService.register("abc@mail.com", "edffsff");

        assertEquals("abc@mail.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(encoder, times(1)).encode("edffsff");
    }

    @Test
    void shouldLoginUserAndReturnJwt() {
        User user = new User();
        user.setEmail("abc@mail.com");
        user.setPasswordHash("hashed-pass");

        when(userRepository.findByEmail("abc@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("edffsff", "hashed-pass")).thenReturn(true);
        when(jwtProvider.generateToken("abc@mail.com")).thenReturn("mock-jwt");

        String token = authService.login("abc@mail.com", "edffsff");

        assertEquals("mock-jwt", token);
        verify(jwtProvider, times(1)).generateToken("abc@mail.com");
        verify(encoder, times(1)).matches("edffsff", "hashed-pass");
    }
}
