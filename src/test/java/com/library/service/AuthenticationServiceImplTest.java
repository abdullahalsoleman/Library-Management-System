package com.library.service;

import com.library.IRepository.UserRepository;
import com.library.dto.LoginUserDto;
import com.library.dto.RegisterUserDto;
import com.library.model.User;
import com.library.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        RegisterUserDto dto = new RegisterUserDto("test@test.com", "ddA2sasa", "user");
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = authenticationService.signup(dto);

        assertNotNull(savedUser);
        assertEquals("test@test.com", savedUser.getEmail());
    }

    @Test
    void testAuthenticate() {
        LoginUserDto dto = new LoginUserDto("test@test.com", "password");
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        authenticationService.authenticate(dto);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
    }
}
